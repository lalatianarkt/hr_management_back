package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.rh.manage.Model.Employe;
import com.rh.manage.Service.AutomatisationService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/automatisation")
// @CrossOrigin(origins = "*")

public class AutomatisationController {
    @Autowired 
    AutomatisationService automatisationService;

    @PostMapping("/sendEmailBienvenue")
    public ResponseEntity<String> declencheMailBienvenue(@RequestBody Employe employe) {
        try {
            automatisationService.sendMailDeBienvenueAutomatique(employe);
            return ResponseEntity.ok("Email de bienvenue envoyé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    } 
}
