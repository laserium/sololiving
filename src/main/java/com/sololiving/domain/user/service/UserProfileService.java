package com.sololiving.domain.user.service;

import java.util.List;

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
            String filePath = "media/profile-image/" + userId;

            deleteExistingProfileImage(userId);

            String imageUrl;
            try {
                imageUrl = s3Uploader.uploadFileToS3(file, filePath);
            } catch (Exception e) {
                throw new RuntimeException("UPLOAD PROFILE IMAGE TO S3 : FAILED", e);
            }

            UserProfileImageResponseDto profileImageDto = UserProfileImageResponseDto.builder()
                    .userId(userId)
                    .imageUrl(imageUrl)
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .build();

            userProfileMapper.updateUserProfileImage(profileImageDto);
        }
    }

    private void deleteExistingProfileImage(String userId) {
        String filePath = "media/profile-image/" + userId + "/";
        try {
            List<String> fileKeys = s3Uploader.listFilesInDirectory(filePath);
            for (String key : fileKeys) {
                s3Uploader.deleteProfileImageS3(key);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete existing profile image from S3", e);
        }
    }

    @Transactional
    public void updateUserBio(String userId, String bio) {
        userProfileMapper.updateProfileBio(userId, bio);
    }
}
