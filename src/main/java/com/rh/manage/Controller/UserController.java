package com.rh.manage.Controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;
import com.rh.manage.Service.EmailService;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.UserService;
import com.rh.manage.Service.UserService.AuthenticationException;
import com.rh.manage.Service.UserService.ResourceNotFoundException;


@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired 
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeService employeService;

    @PostMapping("/resendToken")
    public ResponseEntity<Map<String, String>> resendToken(@RequestParam String email) {
        try {
            
            // Appel du service pour renvoyer le token par email
            String token = emailService.reSendToken(email);

            // Réponse réussie
            return ResponseEntity.ok(Map.of(
                "status", "200",
                "message", "📩 Un nouveau token a été envoyé à " + email
            ));
        } catch (Exception e) {
            // En cas d'erreur
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "500",
                "message", "❌ Erreur lors de l'envoi du token : " + e.getMessage()
            ));
        }
    } 

    @PostMapping("/sendToken")
    public ResponseEntity<?> sendToken(@RequestBody User user, @RequestParam String matricule) {
        try {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email obligatoire");
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Mot de passe obligatoire");
            }
            
            userService.register(user, matricule);
            return ResponseEntity.ok(Map.of(
                "message", "Token envoyé avec succès à " + user.getEmail(),
                "success", true
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de l'envoi du token : " + e.getMessage()));
        }
    }

    @PostMapping("/confirmUser")
    public ResponseEntity<?> confirmUser(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String tokenInput = request.get("token");

            if (email == null || tokenInput == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email et token requis"));
            }

            userService.validateCountByEmail(email, tokenInput);

            return ResponseEntity.ok(Map.of("message", "Compte confirmé avec succès"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de la confirmation : " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email obligatoire");
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Mot de passe obligatoire");
            }

            // userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Utilisateur créé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody User userRequest) {
        try {
            Map<String, Object> authResponse = userService.authenticateUser(userRequest);
            return ResponseEntity.ok(authResponse);
            
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of(
                "status", 401,
                "message", e.getMessage()
            ));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", e.getMessage()
            ));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "status", 500,
                "message", e.getMessage()
            ));
        }
    }
}
