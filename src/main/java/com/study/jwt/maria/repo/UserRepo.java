package com.study.jwt.maria.repo;

import com.study.jwt.maria.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
