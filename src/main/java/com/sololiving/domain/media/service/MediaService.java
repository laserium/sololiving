package com.sololiving.domain.media.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sololiving.domain.media.mapper.MediaMapper;
import com.sololiving.global.util.aws.S3Uploader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaService {

    private final MediaMapper mediaMapper;
    private final MediaUploadService mediaUploadService;
    private final S3Uploader s3Uploader;

    public void updateMediaInArticle(Long articleId, List<String> updatedMediaUrls) {
        // 1. 기존의 미디어 파일 목록을 조회 (DB 또는 S3에 저장된 목록에서 가져옴)
        List<String> existingMediaUrls = mediaMapper.selectMediaUrlsByArticleId(articleId);
        // 2. updatedMediaUrls가 null이거나 비어있는 경우 -> 모든 기존 파일 삭제
        if (updatedMediaUrls == null || updatedMediaUrls.isEmpty()) {
            deleteAllExistingMedia(existingMediaUrls, articleId); // 모든 기존 파일 삭제
            return;
        }
        // 3. 삭제해야 할 파일 목록 추출 (updatedMediaUrls에 없는 기존 파일) 후 삭제
        List<String> filesToDelete = existingMediaUrls.stream()
                .filter(url -> !updatedMediaUrls.contains(url))
                .collect(Collectors.toList());
        deleteMediaByName(filesToDelete);
        // 4. 새로 추가된 파일 추출 (existingMediaUrls에 없는 파일) 후 있으면 추가
        List<String> newMediaUrls = updatedMediaUrls.stream()
                .filter(url -> !existingMediaUrls.contains(url))
                .collect(Collectors.toList());

        if (!newMediaUrls.isEmpty()) {
            mediaUploadService.attachFilesToArticle(articleId, newMediaUrls);
            log.info("새로 추가된 파일 목록: {}", newMediaUrls);
        }

    }

    // 기존 미디어 파일 전체 삭제 함수
    private void deleteAllExistingMedia(List<String> existingMediaUrls, Long articleId) {
        if (existingMediaUrls != null && !existingMediaUrls.isEmpty()) {
            for (String url : existingMediaUrls) {
                try {
                    s3Uploader.deleteS3(url); // S3에서 파일 삭제
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mediaMapper.deleteMediaUrlsByArticleId(articleId);
        }
    }

    // Media name으로 미디어 삭제 함수
    private void deleteMediaByName(List<String> mediaUrls) {
        for (String url : mediaUrls) {
            try {
                // URL에서 media_name 추출
                String mediaName = url.substring(url.lastIndexOf("/") + 1);

                // S3에서는 전체 URL로 파일 삭제
                s3Uploader.deleteS3(url);

                // DB에서는 media_name으로 삭제
                mediaMapper.deleteMediaByName(mediaName);
            } catch (Exception e) {
                log.error("미디어 삭제 중 오류 발생: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
