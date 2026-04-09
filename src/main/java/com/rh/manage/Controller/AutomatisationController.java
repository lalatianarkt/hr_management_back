package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.rh.manage.Dto.AttestationCongeData;
import com.rh.manage.Model.Mouvement;
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

    // @PostMapping("/sendEmailBienvenue")
    // public ResponseEntity<String> declencheMailBienvenue() {
    //     try {
    //         automatisationService.sendMailDeBienvenueAutomatique();
    //         return ResponseEntity.ok("Email de bienvenue envoyé avec succès !");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                             .body("Erreur lors de l'envoi de l'email : " + e.getMessage());
    //     } 
    // } 

    // @PostMapping("/sendEmailAttestation")
    // public ResponseEntity<String> envoyerAttestationConge(@RequestBody AttestationCongeData data) {
    //     try {
    //         automatisationService.sendEmailNotificationDemandeConge(data);
    //         return ResponseEntity.ok("Email avec attestation envoyé avec succès !");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Erreur lors de l'envoi : " + e.getMessage());
    //     }
    // }

    @PostMapping("/sendEmailMouvement")
    public ResponseEntity<String> envoyerNotificationMouvement(@RequestBody Mouvement data) {
        try {
            System.out.println("ato eeeeeeeeeeeeeeeee");
            automatisationService.sendEmailNotificationDemandeMvt(data);
            return ResponseEntity.ok("Email de mouvement envoy? avec succ?s !");
        } catch (Exception e) {
            System.out.println("aiza eee+++++++++++++++++++++++ : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi du mouvement : " + e.getMessage());
        }
    }


}
