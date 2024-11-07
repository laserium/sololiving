package com.sololiving.domain.media.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MediaType {
    IMAGE("이미지"),
    VIDEO("동영상"),
    AUDIO("오디오");

    private final String description;

}
