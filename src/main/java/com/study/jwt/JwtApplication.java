package com.study.jwt;

import com.study.jwt.domain.Role;
import com.study.jwt.domain.User;
import com.study.jwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null, "GuHyeonKown", "ghkwon", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "Kisun", "kisun", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "HyeonOuk", "hyok", "1234", new ArrayList<>()));
            userService.saveUser(new User(null, "HanNari", "hnari", "1234", new ArrayList<>()));

            userService.addRoleToUser("ghkwon", "ROLE_USER");
            userService.addRoleToUser("kisun", "ROLE_MANAGER");
            userService.addRoleToUser("hyok", "ROLE_ADMIN");
            userService.addRoleToUser("hnari", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("hnari", "ROLE_ADMIN");
            userService.addRoleToUser("hnari", "ROLE_USER");

        };
    }

}
