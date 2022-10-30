package com.study.jwt.utils;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    // 랜덤 숫자 생성기
    public static String getRandomNumber(int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int n = (int) (Math.random() * 10);
            buffer.append(n);
        }
        return buffer.toString();
    }

}
