package com.sololiving.domain.media.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sololiving.domain.article.enums.MediaType;
import com.sololiving.domain.media.exception.MediaErrorCode;
import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.domain.media.vo.MediaVo;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.aws.S3Uploader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaUploadService {

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

    private final S3Uploader s3Uploader;
    private final MediaMapper mediaMapper;

    public int attachFilesToArticle(Long articleId, List<String> uploadedFileUrls) {
        int mediaTypeBitmask = 0; // 초기 비트마스크, 미디어 타입 없음

        for (String tempFileUrl : uploadedFileUrls) {
            String s3FileKey = extractS3Key(tempFileUrl);
            String newFilePath = moveFileToArticleDirectory(articleId, s3FileKey);
            String newS3FileKey = extractS3Key(newFilePath);

            // 파일의 실제 타입을 결정하고, 해당 타입의 비트를 활성화
            MediaType mediaType = determineMediaType(newS3FileKey);
            mediaTypeBitmask |= mediaType.getBitValue(); // 비트 마스크에 해당 타입 비트 추가

            // DB에 저장할 MediaVo 객체 생성 및 저장
            MediaVo mediaVo = MediaVo.builder()
                    .articleId(articleId)
                    .mediaUrl(newFilePath)
                    .mediaType(mediaType.getBitValue())
                    .mediaName(extractFileName(newS3FileKey))
                    .fileSize(getFileSize(newS3FileKey))
                    .build();
            insertMedia(mediaVo);
        }
        return mediaTypeBitmask; // 모든 파일을 처리한 후 최종 비트마스크 반환
    }

    @Transactional
    private void insertMedia(MediaVo mediaVo) {
        mediaMapper.insertMedia(mediaVo);
    }

    // 임시로 업로드된 파일을 실제 article 디렉토리로 이동
    private String moveFileToArticleDirectory(Long articleId, String s3FileKey) {
        String newFilePath = "media/articles/" + articleId + "/" + extractFileName(s3FileKey);
        s3Uploader.moveS3File(s3FileKey, newFilePath); // 파일 이동
        return s3Uploader.getS3Url(newFilePath); // 새로운 S3 URL 반환
    }

    // 파일의 MIME 타입을 기반으로 MediaType Enum을 결정
    private MediaType determineMediaType(String s3FileKey) {
        String extension = getFileExtension(s3FileKey);
        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "tiff":
                return MediaType.IMAGE; // 이미지 파일 형식
            case "mp4":
            case "mkv":
            case "flv":
            case "avi":
            case "mov":
            case "wmv":
                return MediaType.VIDEO; // 비디오 파일 형식
            case "mp3":
            case "wav":
            case "flac":
            case "aac":
            case "ogg":
                return MediaType.AUDIO; // 오디오 파일 형식
            default:
                throw new ErrorException(MediaErrorCode.UNSUPPORTED_FORMAT);
        }
    }

    // 파일 확장자를 추출하는 메소드
    private String getFileExtension(String s3FileKey) {
        return s3FileKey.substring(s3FileKey.lastIndexOf(".") + 1).toLowerCase();
    }

    // URL에서 파일명을 추출하는 유틸리티 메소드
    private String extractFileName(String s3FileKey) {
        return s3FileKey.substring(s3FileKey.lastIndexOf("/") + 1);
    }

    // 파일 크기를 반환하는 메소드 (옵션)
    private long getFileSize(String s3FileKey) {
        return s3Uploader.getFileSize(s3FileKey); // S3에서 파일 크기를 얻어오는 로직
    }

    // URL에서 S3 키를 추출하는 메소드
    private String extractS3Key(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
    }

    // 파일 크기 검증 메소드
    public void validateFileSize(MultipartFile multipartFile) {
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            throw new ErrorException(MediaErrorCode.FILE_SIZE_EXCEEDS_LIMIT);
        }
    }

}
