package com.oauth2.social.login.repository;

import com.oauth2.social.login.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserInfo, String>{
}
