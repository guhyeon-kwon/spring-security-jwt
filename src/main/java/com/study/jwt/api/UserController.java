package com.study.jwt.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.jwt.domain.Role;
import com.study.jwt.domain.SignUpForm;
import com.study.jwt.domain.User;
import com.study.jwt.repo.UserRepo;
import com.study.jwt.service.UserService;
import com.study.jwt.utils.ConsoleMailSender;
import com.study.jwt.utils.ReturnObject;
import com.study.jwt.utils.SignUpFormValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// created() - 201 반환
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    // 유저 등록
    @PostMapping("/user")
    public ResponseEntity<ReturnObject> saveUser(@RequestBody @Valid SignUpForm signUpForm, Errors errors){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        if (errors.hasErrors()) {
            System.out.println("검증실패");
            ReturnObject object = ReturnObject.builder()
                    .msg(errors.getFieldError().getDefaultMessage())
                    .type(errors.getFieldError().getCode())
                    .build();
            return ResponseEntity.badRequest().body(object);
        } else{
            User user = saveNewUser(signUpForm);

            ReturnObject object = ReturnObject.builder()
                    .msg("ok")
                    .data(user).build();

            return ResponseEntity.created(uri).body(object);
        }
    }

    // 유저 생성
    private User saveNewUser(SignUpForm signUpForm) {
        User user = new User();
        user.setUsername(signUpForm.getUsername());
        user.setPassword(signUpForm.getPassword());
        user.setName(signUpForm.getName());
        user.setEmail(signUpForm.getEmail());
        user.setAddress(signUpForm.getAddress());
        user.setPhone(signUpForm.getPhone());
        user.generateEmailCheckToken();

        return userService.saveUser(user);
    }

    @PostMapping("/role")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    // 토큰 재발급
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                // 토크만 추출 하도록 type부분 제거
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                // JWT 검증용 객체 생성(토큰 생성할때와 동일한 알고리즘 적용)
                JWTVerifier verifier = JWT.require(algorithm).build();
                // 토큰 검증
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        // 토큰 이름
                        .withSubject(user.getUsername())
                        // 토큰 만료일
                        // 10분
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        // 토큰 발행자
                        .withIssuer(request.getRequestURI().toString())
                        // 토큰 payload 작성
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        // 토큰 서명
                        .sign(algorithm);

                Map<String, String> token = new HashMap<>();
                token.put("access_token", access_token);
                token.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), token);

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                // response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh 토큰이 없습니다.");
        }
    }

    // 회원탈퇴
    @DeleteMapping("/user")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                userService.deleteUser(username);
            } catch (Exception e){
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("요청 처리에 필요한 토큰값이 없습니다.");
        }
    }

    // 인증 메일 확인
    @GetMapping("/check-email-token")
    public ResponseEntity<ReturnObject> checkEmailToken(String token, String email, String username, Model model){
        User user = userService.getUser(username);
        ReturnObject object;
        if(user == null){
            object = ReturnObject.builder()
                    .type("wrong.username")
                    .msg("이메일 확인 링크가 정확하지 않습니다.")
                    .build();
            return ResponseEntity.badRequest().body(object);
        }

        if(!user.getEmailCheckToken().equals(token)){
            object = ReturnObject.builder()
                    .type("wrong.token")
                    .msg("이메일 확인 링크가 정확하지 않습니다.")
                    .build();
            return ResponseEntity.badRequest().body(object);
        }

        user.completeSignUp();
        user.setEmailVerified(true);
        user.setJoinedAt(LocalDateTime.now());
        userService.updateUser(user);

        model.addAttribute("username", username);

        object = ReturnObject.builder()
                .msg("ok")
                .data(model)
                .build();

        return ResponseEntity.ok().body(object);

    }

}


@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}