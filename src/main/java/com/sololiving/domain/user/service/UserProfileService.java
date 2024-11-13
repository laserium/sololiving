package com.sololiving.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sololiving.domain.user.dto.response.UserProfileImageResponseDto;
import com.sololiving.domain.user.mapper.UserProfileMapper;
import com.sololiving.global.util.aws.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final S3Uploader s3Uploader;
    private final UserService userService;
    private final UserProfileMapper userProfileMapper;

    public void uploadProfileImage(String userId, MultipartFile file) {

        if (file == null) {
            userProfileMapper.updateUserProfileImage(userService.createUserProfileImage(userId));

        } else {
            // S3 경로 설정
            String filePath = "media/profile-image/" + userId;

            // S3에 이미지 파일 업로드
            String imageUrl;
            try {
                imageUrl = s3Uploader.uploadFileToS3(file, filePath);
            } catch (Exception e) {
                throw new RuntimeException("UPLOAD PROFILE IMAGE TO S3 : FAILED", e);
            }

            // 이미지 메타데이터 생성
            UserProfileImageResponseDto profileImageDto = UserProfileImageResponseDto.builder()
                    .userId(userId)
                    .imageUrl(imageUrl)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .build();

            // DB에 프로필 이미지 정보 저장
            userProfileMapper.updateUserProfileImage(profileImageDto);
        }

    }

    @Transactional
    public void updateUserBio(String userId, String bio) {
        userProfileMapper.updateProfileBio(userId, bio);
    }
}
