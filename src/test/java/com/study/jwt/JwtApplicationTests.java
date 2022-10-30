package com.study.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.study.jwt.utils.CommonUtil.getRandomNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(getRandomNumber(4));
        assertEquals(4, getRandomNumber(4).length());
    }

}
