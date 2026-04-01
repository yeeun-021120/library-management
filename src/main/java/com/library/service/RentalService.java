package com.library.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.domain.Rental;
import com.library.domain.Reservation;
import com.library.mapper.BookCopyMapper;
import com.library.mapper.UserMapper;
import com.library.mapper.RentalMapper;
import com.library.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 대여 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalService {

    private final RentalMapper rentalMapper;
    private final UserMapper userMapper;
    private final BookCopyMapper bookCopyMapper;
    private final ReservationMapper reservationMapper;

    // ========== 일반 사용자 메서드 ==========

    /**
     * 사용자의 대여 목록 조회
     * ✅ 대여중(RENTED) 최우선
     * ✅ 그 외는 최신 대여일 순
     */
    public List<Rental> getUserRentals(String email) {
        Long userId = userMapper.findByEmail(email).getUserId();
        List<Rental> rentals = rentalMapper.findByUserId(userId);

        rentals.sort(
            Comparator
                // RENTED 먼저 (false → 앞)
                .comparing((Rental r) -> !"RENTED".equals(r.getRentalStatus()))
                // 같은 상태면 최신 대여일 우선
                .thenComparing(Rental::getRentalDate, Comparator.reverseOrder())
        );

        return rentals;
    }

    /**
     * 도서 대여
     */
    @Transactional
    public void rentBook(Long bookId, UserDetails userDetails) {
        String email = userDetails.getUsername();
        Long userId = userMapper.findByEmail(email).getUserId();

        log.info("=== 대여 시작: userId={}, bookId={} ===", userId, bookId);

        int activeRentals = rentalMapper.countActiveRentalByUser(userId);
        if (activeRentals >= 3) {
            throw new IllegalStateException("최대 3권까지만 대여 가능합니다.");
        }

        int sameBookRentals = rentalMapper.countActiveRentalByUserAndBook(userId, bookId);
        if (sameBookRentals > 0) {
            throw new IllegalStateException("이미 대여중인 도서입니다.");
        }

        Long copyId = rentalMapper.findAvailableCopyId(bookId);
        if (copyId == null) {
            throw new IllegalStateException("대여 가능한 도서가 없습니다.");
        }

        bookCopyMapper.updateStatus(copyId, "RENTED");

        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setCopyId(copyId);
        rental.setRentalDate(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(14));
        rental.setRentalStatus("RENTED");

        rentalMapper.insert(rental);

        log.info("대여 완료: rentalId={}", rental.getRentalId());
    }

    /**
     * 예약된 도서 대여 (예약자 전용)
     */
    @Transactional
    public void rentReservedBook(Long bookId, UserDetails userDetails) {
        String email = userDetails.getUsername();
        Long userId = userMapper.findByEmail(email).getUserId();

        log.info("=== 예약 도서 대여 시작: userId={}, bookId={} ===", userId, bookId);

        int activeRentals = rentalMapper.countActiveRentalByUser(userId);
        if (activeRentals >= 3) {
            throw new IllegalStateException("최대 3권까지만 대여 가능합니다.");
        }

        List<Reservation> userReservations =
                reservationMapper.findActiveByUserAndBook(userId, bookId);
        if (userReservations.isEmpty()) {
            throw new IllegalStateException("예약 정보가 없습니다.");
        }

        Reservation reservation = userReservations.get(0);

        if (reservation.getExpiresAt() == null ||
            reservation.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalStateException("예약이 만료되었습니다.");
        }

        Long copyId = rentalMapper.findReservedCopyId(bookId);
        if (copyId == null) {
            throw new IllegalStateException("예약된 도서가 준비되지 않았습니다.");
        }

        bookCopyMapper.updateStatus(copyId, "RENTED");

        Rental rental = new Rental();
        rental.setUserId(userId);
        rental.setCopyId(copyId);
        rental.setRentalDate(LocalDate.now());
        rental.setDueDate(LocalDate.now().plusDays(14));
        rental.setRentalStatus("RENTED");

        rentalMapper.insert(rental);

        reservationMapper.complete(reservation.getReservationId());

        log.info("예약 도서 대여 완료: rentalId={}, reservationId={}",
                rental.getRentalId(), reservation.getReservationId());
    }

    /**
     * 도서 반납
     */
    @Transactional
    public void returnBook(Long rentalId) {
        log.info("=== 반납 처리 시작: rentalId={} ===", rentalId);

        Rental rental = rentalMapper.findById(rentalId);
        if (rental == null) {
            throw new IllegalStateException("대여 정보를 찾을 수 없습니다.");
        }

        if (!"RENTED".equals(rental.getRentalStatus())) {
            throw new IllegalStateException("이미 반납된 도서입니다.");
        }

        rentalMapper.updateReturn(rentalId, LocalDate.now());
        log.info("반납 처리 완료: rentalId={}", rentalId);

        List<Reservation> activeReservations =
                reservationMapper.findActiveByBookId(rental.getBookId());

        if (!activeReservations.isEmpty()) {
            Reservation nextReservation = activeReservations.get(0);

            bookCopyMapper.updateStatus(rental.getCopyId(), "RESERVED");
            reservationMapper.setExpiryTime(nextReservation.getReservationId());
        } else {
            bookCopyMapper.updateStatus(rental.getCopyId(), "AVAILABLE");
        }

        log.info("=== 반납 처리 완료 ===");
    }

    /**
     * 사용자의 특정 도서 활성 예약 여부 확인
     */
    public boolean hasActiveReservation(String email, Long bookId) {
        Long userId = userMapper.findByEmail(email).getUserId();
        return reservationMapper.countActiveByUserAndBook(userId, bookId) > 0;
    }

    /**
     * 사용자의 특정 도서 활성 대여 여부 확인
     */
    public boolean hasActiveRental(String email, Long bookId) {
        Long userId = userMapper.findByEmail(email).getUserId();
        return rentalMapper.countActiveRentalByUserAndBook(userId, bookId) > 0;
    }

    // ========== 관리자용 메서드 ==========

    public List<Rental> getAllRentals() {
        return rentalMapper.findAll();
    }

    public List<Rental> getRentalsByStatus(String status) {
        return rentalMapper.findByStatus(status);
    }

    public List<Rental> searchRentals(String keyword) {
        return rentalMapper.searchRentals(keyword);
    }

    public List<Rental> getOverdueRentals() {
        return rentalMapper.findOverdueRentals();
    }

    @Transactional
    public void adminReturnBook(Long rentalId) {
        returnBook(rentalId);
    }

    public int getActiveRentalsCount() {
        return rentalMapper.countByStatus("RENTED");
    }

    public int getOverdueRentalsCount() {
        return rentalMapper.countByStatus("OVERDUE");
    }

    public int getReturnedRentalsCount() {
        return rentalMapper.countByStatus("RETURNED");
    }

    public int getTotalRentalsCount() {
        return rentalMapper.countAll();
    }
}