package com.library.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 파일 업로드 유틸리티
 */
public class FileUploadUtil {
    
    // 업로드 디렉토리 (프로젝트 루트 기준)
    private static final String UPLOAD_DIR = "src/main/resources/static/images/books/";
    
    /**
     * 파일 업로드
     * @param file 업로드할 파일
     * @return 저장된 파일 경로 (DB에 저장할 상대 경로)
     */
    public static String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        
        // 디렉토리 생성
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // 파일명 생성 (UUID + 원본 확장자)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString() + extension;
        
        // 파일 저장
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // DB에 저장할 상대 경로 반환
        return "/images/books/" + newFilename;
    }
    
    /**
     * 파일 삭제
     * @param filePath 삭제할 파일 경로
     */
    public static void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        
        try {
            Path path = Paths.get("src/main/resources/static" + filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // 파일 삭제 실패 시 로그만 남기고 계속 진행
            System.err.println("파일 삭제 실패: " + filePath);
        }
    }
}