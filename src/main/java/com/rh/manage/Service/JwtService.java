package com.rh.manage.Service;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {
    private static final String SECRET_KEY =
            "ma-cle-super-secrete-pour-le-jwt-qui-doit-etre-longue";

    private final UserRoleService userRoleService;

    public JwtService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(User user, String role) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", role)
                .claim("idEmploye", user.getEmploye().getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7200000)) // 2h
                // .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
