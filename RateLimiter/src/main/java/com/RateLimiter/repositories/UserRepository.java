package com.RateLimiter.repositories;

import com.RateLimiter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailId(String emailId);

    User findByUserName(String userName);
}
