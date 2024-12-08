package project.forwork.api.common.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.controller.port.S3Service;
import project.forwork.api.common.error.S3ErrorCode;
import project.forwork.api.common.exception.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.url}")
    private String cloudFrontUrl;  // CloudFront URL 추가

    public String generatePresignedUrl(String originalFilename) {
        // 랜덤 파일 이름 생성
        String randomFileName = generateRandomFileName(originalFilename);

        // URL 유효 기간 설정 (10분)
        Date expiration = new Date();
        expiration.setTime(System.currentTimeMillis() + 1000 * 60 * 10);

        // Pre-signed URL 요청 생성
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, randomFileName)
                .withMethod(HttpMethod.PUT) // PUT 요청
                .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(request);

        // 클라이언트에서 S3 업로드 후 파일 접근 경로 반환
        return presignedUrl.toString();
    }


    public String saveFile(MultipartFile file){
        String randomFileName = generateRandomFileName(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try{
            amazonS3.putObject(bucket, randomFileName, file.getInputStream(), metadata);
        }catch(AmazonS3Exception e){
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new ApiException(S3ErrorCode.S3_ERROR, e);
        }catch (SdkClientException e){
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new ApiException(S3ErrorCode.AWS_SDK_ERROR, e);
        }catch (IOException e){
            log.error("IO error while uploading file: " + e.getMessage());
            throw new ApiException(S3ErrorCode.IO_ERROR, e);
        }

        return cloudFrontUrl + "/" + randomFileName;
    }

    public void deleteFile(String fileUrl){
        String objectKey = fileUrl.replace(cloudFrontUrl + "/", "");

        if(!amazonS3.doesObjectExist(bucket, objectKey)){
            log.error("S3 버킷에 파일이 존재 하지 않습니다.");
            throw new ApiException(S3ErrorCode.NOT_FOUND_FILE);
        }

        try{
            amazonS3.deleteObject(bucket, objectKey);
        }catch(AmazonS3Exception e){
            log.error("File delete fail : " + e.getMessage());
            throw new ApiException(S3ErrorCode.S3_ERROR, e);
        }catch (SdkClientException e) {
            log.error("AWS SDK client error : " + e.getMessage());
            throw new ApiException(S3ErrorCode.AWS_SDK_ERROR, e);
        }
    }

    public void saveFileByStream(String fileName, InputStream inputStream, long contentLength) {
        try {
            // S3 업로드를 위한 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            metadata.setContentType("application/octet-stream"); // 기본 MIME 타입

            // S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch (AmazonS3Exception e) {
            throw new ApiException(S3ErrorCode.S3_ERROR, e);
        } catch (SdkClientException e) {
            throw new ApiException(S3ErrorCode.AWS_SDK_ERROR, e);
        }
    }

    private String generateRandomFileName(String originalFilename){
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
    }

    private String generateRandomFileName(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
    }

    private String validateFileExtension(String originFileName){
        String fileExtension = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "jpeg", "jar", "mov");

        if(!allowedExtensions.contains(fileExtension)){
            throw new ApiException(S3ErrorCode.VALID_FILE_FORMAT);
        }
        return fileExtension;
    }
}
