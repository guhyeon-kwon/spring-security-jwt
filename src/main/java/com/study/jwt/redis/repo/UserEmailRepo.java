package com.study.jwt.redis.repo;

import com.study.jwt.redis.domain.UserEmail;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserEmailRepo extends CrudRepository<UserEmail, String> {
    Optional<UserEmail> findByEmail(String email);
}
