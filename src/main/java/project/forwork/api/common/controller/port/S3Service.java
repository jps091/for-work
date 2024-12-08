package project.forwork.api.common.controller.port;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String saveFile(MultipartFile file);
    String generatePresignedUrl(String originalFilename);
    void deleteFile(String fileUrl);
}
