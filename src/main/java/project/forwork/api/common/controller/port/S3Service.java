package project.forwork.api.common.controller.port;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String saveFile(MultipartFile file);
    void deleteFile(String fileUrl);
}
