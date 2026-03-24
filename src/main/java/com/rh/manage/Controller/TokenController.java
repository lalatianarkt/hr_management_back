package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Model.User;
import com.rh.manage.Service.TokenService;

@RestController
@RequestMapping("/api/token")
public class TokenController {
    @Autowired
    TokenService tokenService;

    // @GetMapping
    // public ResponseEntity<?> generateToken(User user, String type) {
    //     tokenService.generateToken(user, type);
    //     return new String();
    // }

    // @GetMapping("/{userId}")
    // public ResponseEntity<?> getTokenByToken(@RequestParam String token) {
    //     return new String();
    // }
    
    
}
