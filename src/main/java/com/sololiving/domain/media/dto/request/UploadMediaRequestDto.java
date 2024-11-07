package com.sololiving.domain.media.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadMediaRequestDto {
    MultipartFile multipartFile;
    Long articleId;
}
