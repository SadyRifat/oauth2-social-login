package com.oauth2.social.login.repository;

import com.oauth2.social.login.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    @Query("SELECT user FROM UserInfo user WHERE user.username=:username")
    Optional<UserInfo> findByUsername(String username);

    @Query("SELECT user FROM UserInfo user WHERE user.email=:email")
    Optional<UserInfo> findByUserEmail(String email);
}
