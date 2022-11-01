package com.study.jwt.redis.repo;

import com.study.jwt.redis.domain.UserSMS;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSMSRepo extends CrudRepository<UserSMS, String> {
    Optional<UserSMS> findByPhoneAndPurpose(String phone, String purpose);
}
