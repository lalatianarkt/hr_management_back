package com.rh.manage.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import com.rh.manage.Service.JwtService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.UserRoleService;
import com.rh.manage.Service.UserService;
import com.rh.manage.Service.UserService.AuthenticationException;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import jakarta.transaction.Transactional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/resendToken")
    public ResponseEntity<Map<String, String>> resendToken(@RequestParam String email) {
        try {
            String token = emailService.reSendToken(email);
            return ResponseEntity.ok(Map.of(
                "status", "200",
                "message", "📩 Un nouveau token a été envoyé à " + email
            ));
        } catch (Exception e) {
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
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

            System.out.println("-------------------tonga ato eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee---------------------------------");
            System.out.println("Email: " + email);
            System.out.println("Token: " + tokenInput);
            System.out.println("---------------------------------------------------------------------------------------------------");


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

    @Transactional
    @PostMapping("/all")
    public ResponseEntity<?> registerAll(@RequestBody List<UserRole> users) {
        try {
            if (users == null || users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La liste des utilisateurs est vide");
            }

            List<User> savedUsers = new ArrayList<>();
            List<UserRole> savedUserRoles = new ArrayList<>();

            for (UserRole userRole : users) {
                User user = userRole.getUser();
                
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Email obligatoire pour chaque utilisateur");
                }

                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Mot de passe obligatoire pour chaque utilisateur");
                }
                
                Optional<User> existingUser = userService.findByEmail(user.getEmail());
                User savedUser;
                
                if (existingUser.isPresent()) {
                    savedUser = existingUser.get();
                    System.out.println("Utilisateur existant: " + savedUser.getEmail());
                } else {
                    User newUser = new User();
                    newUser.setEmail(user.getEmail());
                    String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                    newUser.setPassword(hashedPassword);
                    // newUser.setPassword(user.getPassword());
                    newUser.setEmploye(user.getEmploye());
                    newUser.setStatut(user.getStatut() != null ? user.getStatut() : 1);
                    savedUser = userService.save(newUser);
                    System.out.println("Nouvel utilisateur créé: " + savedUser.getEmail());
                }
                
                UserRole newUserRole = new UserRole();
                newUserRole.setUser(savedUser);
                newUserRole.setTypeUser(userRole.getTypeUser());
                newUserRole.setStatut(userRole.getStatut() != null ? userRole.getStatut() : 1);
                newUserRole.setCreatedAt(userRole.getCreatedAt() != null ? userRole.getCreatedAt() : LocalDateTime.now());
                
                UserRole savedUserRole = userRoleService.create(newUserRole);
                savedUserRoles.add(savedUserRole);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                        "message", "Utilisateurs créés avec succès",
                        "count", savedUserRoles.size(),
                        "userRoles", savedUserRoles
                    ));
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
 
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody UserRequest userRequest) {
        try { 
            // System.out.println("user id : ");
            Map<String, Object> authResponse = userService.authenticateUser(userRequest);
            return ResponseEntity.ok(authResponse);
            
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of(
                "status", 401,
                "message", e.getMessage()
            )); 
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
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

    @PostMapping("/switch-role")
    public ResponseEntity<?> switchRole(@RequestBody Map<String, Object> request, Authentication authentication) {
        try {
            System.out.println("tonga ato eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            // String userId = (String) request.get("userId");
            String newRole = (String) request.get("roleType");
            System.out.println(", newRole: " + newRole);

            if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "status", 401,
                        "message", "Utilisateur non authentifié",
                        "error", "UNAUTHENTICATED"
                    ));
            }
            
            String userId = (String) authentication.getPrincipal();
            
            // Vérifier que l'utilisateur a bien ce rôle
            boolean hasRole = userRoleService.hasActiveRole(userId);
            if (!hasRole) {
                return ResponseEntity.status(403).body(Map.of(
                    "status", 403,
                    "message", "Vous n'avez pas accès à ce rôle"
                ));
            }
            
            // Récupérer l'utilisateur
            User user = userService.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
            
            // Générer un nouveau token avec le nouveau rôle
            String jwt = jwtService.generateToken(user, newRole);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("token", jwt);
            response.put("role", newRole);
            response.put("path",userService.determineRedirectPath(newRole));
            response.put("message", "Rôle changé avec succès");
            
            return ResponseEntity.ok(response);
            
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body(Map.of(
                "status", 404,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "status", 500,
                "message", "Erreur lors du changement de rôle"
            ));
        }
    }

    // Endpoint pour la sélection du rôle (pour les utilisateurs multi-rôles)
    @PostMapping("/select-role")
    public ResponseEntity<?> selectRole(@RequestBody Map<String, Object> roleSelectionRequest) {
        try {
            String userId = (String) roleSelectionRequest.get("userId");
            String roleType = (String) roleSelectionRequest.get("roleType");
            
            Map<String, Object> response = userService.selectUserRole(userId, roleType);
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
                "message", "Erreur lors de la recuperation des utilisateurs : " + e.getMessage()
            ));
        }
    }

    @GetMapping("/admin/list/paged")
    public ResponseEntity<?> getAdminUserListPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> result = userService.getAllUsersForAdminListPaged(page, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "message", "Erreur lors de la recuperation des utilisateurs : " + e.getMessage()
            ));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Utilisateur supprime avec succes",
                "userId", id
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", 500,
                "message", "Erreur lors de la suppression de l'utilisateur : " + e.getMessage()
            ));
        }
    }
    @PatchMapping("/{id}/roles")
    public ResponseEntity<?> toggleUserRole(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            Object typeUserIdValue = request.get("typeUserId");
            Object authorizedValue = request.get("authorized");

            if (typeUserIdValue == null || authorizedValue == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "message", "typeUserId et authorized sont requis"
                ));
            }

            Integer typeUserId = Integer.valueOf(typeUserIdValue.toString());
            boolean authorized = Boolean.parseBoolean(authorizedValue.toString());

            userService.toggleUserRole(id, typeUserId, authorized);

            return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", authorized ? "Role assigne avec succes" : "Role retire avec succes",
                "userId", id,
                "typeUserId", typeUserId,
                "authorized", authorized
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
                "message", "Erreur lors de la mise a jour des roles : " + e.getMessage()
            ));
        }
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> updateUserStatut(@PathVariable String id, @RequestBody UserDecisionRequest request) {
        try {
            User updatedUser = userService.updateUserStatut(id, request.getStatut());
            return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Statut utilisateur mis a jour avec succes",
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
                "message", "Erreur lors de la mise a jour du statut : " + e.getMessage()
            ));
        }
    }
}




