package com.sololiving.global.util;

import java.util.Random;
import java.util.UUID;

public class RandomGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String makeRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    // 랜덤 12자리 ID 생성
    public static String generateRandomId() {
        return "deletedUser_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    // 랜덤 비밀번호 생성 (16자리)
    public static String generateRandomPassword() {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(CHARACTERS.charAt(rnd.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    // 랜덤 연락처 생성 (12자리)
    public static String generateRandomContact() {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 12; i++) {
            sb.append(CHARACTERS.charAt(rnd.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
