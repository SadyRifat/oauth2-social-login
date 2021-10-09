package com.oauth2.social.login.controller;

import com.oauth2.social.login.model.user.UserPrincipal;
import com.oauth2.social.login.security.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TestController {
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        log.info("success");
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
