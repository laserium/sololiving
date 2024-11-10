package com.sololiving.domain.media.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sololiving.domain.media.exception.MediaErrorCode;
import com.sololiving.domain.media.service.MediaUploadService;
import com.sololiving.domain.user.exception.UserErrorCode;
import com.sololiving.domain.user.service.UserAuthService;
import com.sololiving.global.exception.error.ErrorException;
import com.sololiving.global.util.SecurityUtil;
import com.sololiving.global.util.aws.S3Uploader;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class MediaController {

    private final UserAuthService userAuthService;
    private final MediaUploadService mediaUploadService;
    private final S3Uploader s3Uploader;

    // 미디어 파일 임시 저장
    @PostMapping("/upload/temp")
    public ResponseEntity<?> uploadMedia(HttpServletRequest httpServletRequest,
            @RequestParam("multipartFiles") List<MultipartFile> multipartFiles) {

        // 회원 유무 검증
        String userId = SecurityUtil.getCurrentUserId();
        if (userAuthService.isUserIdAvailable(userId)) {
            throw new ErrorException(UserErrorCode.USER_ID_NOT_FOUND);
        }
        try {
            List<String> uploadedFileUrls = new ArrayList<>();

            // 각각의 파일을 S3에 업로드하고 URL을 리스트에 추가
            for (MultipartFile multipartFile : multipartFiles) {
                mediaUploadService.validateFileSize(multipartFile);
                String fileUrl = s3Uploader.uploadFileToS3(multipartFile, "media/articles/temp");
                uploadedFileUrls.add(fileUrl);
            }

            // 성공 메시지와 함께 업로드된 파일들의 URL 리스트 반환
            return ResponseEntity.status(HttpStatus.OK).body(uploadedFileUrls);

        } catch (Exception e) {
            throw new ErrorException(MediaErrorCode.FAIL_TO_UPLOAD_FILE);
        }
    }

}
