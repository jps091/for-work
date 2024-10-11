package project.forwork.api.mock;

import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.controller.port.S3Service;
import project.forwork.api.common.error.S3ErrorCode;
import project.forwork.api.common.exception.ApiException;

import java.io.IOException;
import java.util.*;

public class FakeS3Service implements S3Service {

    private Map<String, byte[]> storage = new HashMap<>();
    private String bucket = "fake-bucket";
    @Override
    public String saveFile(MultipartFile file) {
        String randomFileName = generateRandomFileName(file);
        try {
            storage.put(randomFileName, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
        return "http://localhost/" + bucket + "/" + randomFileName;
    }

    @Override
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        storage.remove(fileName);
    }

    private String generateRandomFileName(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        return "1a2a3a4a5" + "." + fileExtension;
    }

    private String validateFileExtension(String originFileName){
        String fileExtension = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "jpeg");

        if(!allowedExtensions.contains(fileExtension)){
            throw new ApiException(S3ErrorCode.VALID_FILE_FORMAT);
        }
        return fileExtension;
    }
}

