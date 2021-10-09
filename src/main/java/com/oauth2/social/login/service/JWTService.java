package com.oauth2.social.login.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class JWTService {

    private String jwtSecretKey = "kT2MJt-7NxPM6b-7biRysPw-FcEQHSKf-7Ywae3CU";
    private int jwtExpirationHr = 6;

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }

        return false;
    }

    public String getSubjectDataFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date ())
                .setExpiration(getTokenExpiredTime())
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    public Date getTokenExpiredTime() {
        Calendar instance = Calendar.getInstance ();
        instance.setTime (new Date ());
        instance.add (Calendar.HOUR, jwtExpirationHr);
        return instance.getTime ();
    }
}
