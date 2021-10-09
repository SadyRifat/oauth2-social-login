package com.oauth2.social.login.repository;

import com.oauth2.social.login.entity.Role;
import com.oauth2.social.login.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, String> {
    @Query("select role from Role role where role.name=:roleName")
    Role getRoleByName(ERole roleName);
}
