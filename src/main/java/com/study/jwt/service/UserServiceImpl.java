package com.study.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.jwt.domain.Role;
import com.study.jwt.domain.User;
import com.study.jwt.mail.EmailMessage;
import com.study.jwt.mail.EmailService;
import com.study.jwt.repo.RoleRepo;
import com.study.jwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final TemplateEngine templateEngine;

    @Override
    public User saveUser(User user) {
        log.info("새로운 유저 정보를 DB에 저장했습니다 : ", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepo.findByName("ROLE_USER");
        user.getRoles().add(role);
        sendSignUpConfirmEmail(user);
        return userRepo.save(user);
    }

    @Override
    public void updateUser(User user) {
        log.info("유저 정보를 수정했습니다 : ", user.getUsername());
        userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("새로운 Role 정보를 DB에 저장했습니다 : ", role.getName());
        return roleRepo.save(role);
    }

//    @Override
//    public void addRoleToUser(String username, String roleName) {
//        log.info("사용자 {} 에게 {} 권한을 추가했습니다.", username, roleName);
//        User user = userRepo.findByUsername(username);
//        Role role = roleRepo.findByName(roleName);
//        user.getRoles().add(role);
//    }

    @Override
    public User getUser(String username) {
        log.info("사용자 {}의 상세 정보를 가져왔습니다.", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("모든 유저 정보를 가져옵니다.");
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(String username) {
        log.info("사용자 {} 를 삭제하였습니다.", username);
        userRepo.deleteByUsername(username);
    }

    private void sendSignUpConfirmEmail(User user) {

        Context context = new Context();
        context.setVariable("link", "/api/check-email-token?token=" + user.getEmailCheckToken() +
                "&username=" + user.getUsername());
        context.setVariable("username", user.getUsername());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "버튼을 클릭하시면 이메일 인증 및 회원가입이 완료됩니다.");
        context.setVariable("host", "http://localhost:8080");
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(user.getEmail())
                .subject("스프링 프로젝트, 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
