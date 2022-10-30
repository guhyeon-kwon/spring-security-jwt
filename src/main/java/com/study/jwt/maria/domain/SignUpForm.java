package com.study.jwt.maria.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data @NoArgsConstructor @AllArgsConstructor
public class SignUpForm {
    @NotBlank
    private String name;
    @NotBlank
    @Pattern(regexp = "^[a-z]+[a-z0-9]{5,10}$",
            message = "아이디는 최소 5자 이상, 10자 이하여야 하고 영어로 시작하는 영어+숫자 조합으로 구성되어야 합니다")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 하나 이상의 문자 및 숫자, 하나 이상의 특수문자가 포함되어야 합니다")
    private String password;
    @NotBlank
    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3,4}-[0-9]{4}", message = "올바른 형식의 휴대폰 번호를 입력해주세요")
    private String phone;
}
