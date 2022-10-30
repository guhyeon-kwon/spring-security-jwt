package com.study.jwt.maria.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.GenerationType.AUTO;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name; // 닉네임
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private String email;
    private String address;
    private String phone;
    private boolean smsVerified; // 문자 인증 여부
    private LocalDateTime joinedAt; // 로그인한 시간

    // 회원가입 완료 처리
    public void completeSignUp() {
        this.smsVerified = true;
        this.joinedAt = LocalDateTime.now();
    }
}
