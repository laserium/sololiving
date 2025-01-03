package com.sololiving.global.util.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sololiving.domain.media.exception.MediaErrorCode;
import com.sololiving.global.exception.error.ErrorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 로컬 경로에 저장
    public String uploadFileToS3(MultipartFile multipartFile, String filePath) {
        // MultipartFile -> File 로 변환
        File uploadFile = null;
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = ""; // 확장자 추출 변수

        try {
            // 파일 확장자 추출 (예: .jpg, .png 등)
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            // 로컬에 파일 업로드 MultipartFile을 File로 변환
            uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new ErrorException(MediaErrorCode.FAIL_TO_CONVERT_MULTIPARTFILE_TO_FILE));
        } catch (IOException e) {
            log.error("BACKEND ERROR : MultipartFile => File Converting Failed", e);
        }

        // S3에 저장될 파일 이름 (확장자 포함)
        String fileName = filePath + "/" + UUID.randomUUID() + extension;

        // S3로 업로드 후 로컬 파일 삭제
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    // S3에 파일 업로드
    public String putS3(File uploadFile, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("putS3 FAILED : S3에 파일 업로드 실패", e);
        }
    }

    // S3에 있는 파일 삭제 (영어 파일만 삭제 가능)
    public void deleteS3(String filePath) throws Exception {
        try {
            String key = filePath.substring(filePath.indexOf("/", 8) + 1); // 폴더/파일.확장자
            try {
                amazonS3Client.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3Uploader] : S3에 있는 파일 삭제");
    }

    // Delete a specific file from S3
    public void deleteProfileImageS3(String fileKey) throws Exception {
        try {
            amazonS3Client.deleteObject(bucket, fileKey);
            log.info("[S3Uploader] : Deleted file from S3 - {}", fileKey);
        } catch (AmazonServiceException e) {
            log.error("Failed to delete file from S3 - {}", fileKey, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    // 로컬에 저장된 파일 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("[S3Uploader] : 로컬 파일 삭제 성공");
            return;
        }
        log.info("[S3Uploader] : 로컬 파일 삭제 실패");
    }

    // 로컬에 파일 업로드 및 변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        File convFile = new File(tmpDir, file.getOriginalFilename());

        // 디렉토리가 없는 경우 생성
        if (!convFile.getParentFile().exists()) {
            convFile.getParentFile().mkdirs();
        }

        // 파일 생성
        if (convFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convFile);
        }
        return Optional.empty();
    }

    // S3에서 임시 파일을 article 경로로 이동
    public void moveS3File(String sourceKey, String destinationKey) {
        // 1. 파일 복사
        amazonS3Client.copyObject(bucket, sourceKey, bucket, destinationKey);

        // 2. 원본 파일 삭제
        amazonS3Client.deleteObject(bucket, sourceKey);

    }

    // S3에서 파일의 URL을 반환
    public String getS3Url(String fileKey) {
        return amazonS3Client.getUrl(bucket, fileKey).toString();
    }

    // S3에서 파일 크기를 반환하는 메소드 (옵션)
    public long getFileSize(String fileKey) {
        return amazonS3Client.getObjectMetadata(bucket, fileKey).getContentLength();
    }

    public List<String> listFilesInDirectory(String directoryPath) {
        List<String> fileKeys = new ArrayList<>();
        try {
            ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucket).withPrefix(directoryPath);
            ListObjectsV2Result result;

            do {
                result = amazonS3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    fileKeys.add(objectSummary.getKey());
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());

        } catch (AmazonServiceException e) {
            throw new RuntimeException("Failed to list files in directory: " + directoryPath, e);
        }
        return fileKeys;
    }

}
