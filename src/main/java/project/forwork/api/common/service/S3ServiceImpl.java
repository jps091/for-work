package project.forwork.api.common.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.controller.port.S3Service;
import project.forwork.api.common.error.S3ErrorCode;
import project.forwork.api.common.exception.ApiException;

import java.io.IOException;
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

    private String generateRandomFileName(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
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
