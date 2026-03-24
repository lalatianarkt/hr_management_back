package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Service.EmailService;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-token")
    public String sendToken(@RequestParam String email) {
        emailService.sendTokenEmail(email);
        return "Email envoyé avec succès à " + email;
    }
}

