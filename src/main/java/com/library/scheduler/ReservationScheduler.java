package com.library.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.library.service.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 예약 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;

    /**
     * 매일 자정에 만료된 예약 처리
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void expireReservations() {
        log.info("=== 예약 만료 처리 시작 ===");
        reservationService.expireReservations();
        log.info("=== 예약 만료 처리 완료 ===");
    }
}