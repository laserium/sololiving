package com.sololiving.domain.article.enums;

public enum MediaType {
    NONE(0),
    IMAGE(1), // 이미지 타입은 1
    AUDIO(2), // 오디오 타입은 2
    VIDEO(4); // 비디오 타입은 4

    private final int bitValue;

    MediaType(int bitValue) {
        this.bitValue = bitValue;
    }

    public int getBitValue() {
        return bitValue;
    }
}