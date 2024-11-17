package com.sololiving.global.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DecodeParameterUtil {

    public static String decodeSearchParameter(String inputData) {
        if (inputData != null) {
            inputData = URLDecoder.decode(inputData, StandardCharsets.UTF_8);
            return inputData;
        }
        return "";
    }
}
