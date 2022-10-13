package com.study.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data @NoArgsConstructor @AllArgsConstructor
public class SignUpForm {
    private String name;
    @NotBlank
    @Pattern(regexp = "^[a-z]+[a-z0-9]{5,10}$")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String password;
}
