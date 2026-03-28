package com.rh.manage.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.UserDecisionRequest;
import com.rh.manage.Dto.UserListDTO;
import com.rh.manage.Dto.UserRequest;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
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

    @PostMapping("/all")
    public ResponseEntity<?> registerAll(@RequestBody List<UserRole> users) {
        try {
            
            if (users == null || users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La liste des utilisateurs est vide");
            }

            for (UserRole user : users) {
                if (user.getUser().getEmail() == null || user.getUser().getEmail().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Email obligatoire pour chaque utilisateur");
                }

                if (user.getUser().getPassword() == null || user.getUser().getPassword().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe obligatoire pour chaque utilisateur");
                }
                userService.save(user.getUser());
                userService.registerUserRole(user);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Utilisateurs cr\u00e9\u00e9s avec succ\u00e8s");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'inscription en lot : " + e.getMessage());
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

            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Utilisateur créé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    

    // @PostMapping("/auth")
    // public ResponseEntity<?> authenticate(@RequestBody UserRequest userRequest) {
    //     try { 
    //         Map<String, Object> authResponse = userService.authenticateUser(userRequest);
    //         return ResponseEntity.ok(authResponse);
            
    //     } catch (AuthenticationException e) {
    //         return ResponseEntity.status(401).body(Map.of(
    //             "status", 401,
    //             "message", e.getMessage()
    //         ));
            
    //     } catch (ResourceNotFoundException e) {
    //         return ResponseEntity.status(404).body(Map.of(
    //             "status", 404,
    //             "message", e.getMessage()
    //         ));
            
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).body(Map.of(
    //             "status", 500,
    //             "message", e.getMessage()
    //         ));
    //     }
    // }

    public ResponseEntity<?> authenticate(@RequestBody UserRequest userRequest) {
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

    // Endpoint 2: Sélection du rôle (pour les utilisateurs multi-rôles)
    @PostMapping("/select-role")
    public ResponseEntity<?> selectRole(@RequestBody UserRole roleSelectionRequest) {
        try {
            Map<String, Object> response = userService.selectUserRole(
                roleSelectionRequest.getUser().getId(), 
                roleSelectionRequest.getTypeUser().getType()
            );
            return ResponseEntity.ok(response);
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

    @GetMapping("/admin/list")
    public ResponseEntity<?> getAdminUserList() {
        try {
            List<UserListDTO> users = userService.getAllUsersForAdminList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "message", "Erreur lors de la récupération des utilisateurs : " + e.getMessage()
            ));
        }
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> updateUserStatut(@PathVariable String id, @RequestBody UserDecisionRequest request) {
        try {
            User updatedUser = userService.updateUserStatut(id, request.getStatut());
            return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Statut utilisateur mis à jour avec succès",
                "userId", updatedUser.getId(),
                "statut", updatedUser.getStatut()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "message", "Erreur lors de la mise à jour du statut : " + e.getMessage()
            ));
        }
    }
}
