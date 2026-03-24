package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.InfosAdministratives;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.Token;
import com.rh.manage.Service.InfosProfessionnellesService;
import com.rh.manage.Service.JwtService;
import com.rh.manage.Service.ManagerService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;
import com.rh.manage.Service.UserService;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/infosPro")
public class InfosProfessionnellesController { 
    @Autowired
    private InfosProfessionnellesService infosProService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private JwtService jwtService;

    private ResponseEntity<Map<String, Object>> createResponse(Object data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", status == HttpStatus.OK || status == HttpStatus.CREATED);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/infosEmp")
    public ResponseEntity<?> getInfosProByEmploye(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Claims claims = jwtService.validateToken(token);
            String idEmploye = claims.get("idEmploye", String.class);
            InfosProfessionnelles infosPro = infosProService.findInfosProfessionnellesByIdEmploye(idEmploye);
            if (infosPro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "status", 404,
                        "message", "Aucune information professionnelle trouvée pour l'employé ID: " + idEmploye,
                        "timestamp", LocalDateTime.now()
                    ));
            }
            return ResponseEntity.ok(infosPro);
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            // Erreur d'accès à la base de données
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "status", 500,
                    "message", "Erreur d'accès à la base de données",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
                
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            // Erreur de validation
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", 400,
                    "message", "Paramètre invalide",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
                
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "status", 500,
                    "message", "Une erreur interne est survenue",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
        }
    }
    

    @GetMapping("/infosEmp/{idEmploye}")
    public ResponseEntity<?> getInfosProfessionnellesByEmployeId(@PathVariable String idEmploye) {
        try {
            // Validation de l'ID
            if (idEmploye == null || idEmploye.trim().isEmpty()) {
                System.out.println("id : " + idEmploye);
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "status", 400,
                        "message", "L'ID de l'employé est requis",
                        "timestamp", LocalDateTime.now()
                    ));
            }

            // Récupération des informations professionnelles
            InfosProfessionnelles infosPro = infosProService.findInfosProfessionnellesByIdEmploye(idEmploye);
            
            // Vérification si des infos pro existent
            if (infosPro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "status", 404,
                        "message", "Aucune information professionnelle trouvée pour l'employé ID: " + idEmploye,
                        "timestamp", LocalDateTime.now()
                    ));
            }

            // Réponse avec les données
            return ResponseEntity.ok(infosPro);
            
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            // Erreur d'accès à la base de données
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "status", 500,
                    "message", "Erreur d'accès à la base de données",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
                
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            // Erreur de validation
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "status", 400,
                    "message", "Paramètre invalide",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
                
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "status", 500,
                    "message", "Une erreur interne est survenue",
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now()
                ));
        }
    }

    @GetMapping("/dernierInfo/{idEmploye}")
    public ResponseEntity<?> getDernierInfoProParEmp(@RequestHeader("Authorization") String authHeader,
                                                    @PathVariable String idEmploye) {
        try {
            tokenService.validateToken(authHeader);
            return ResponseEntity.ok(infosProService.getDernierInfosParProEmploye(idEmploye));
        }  catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        }
    }

    @GetMapping("/manager/dep")
    public ResponseEntity<?> getDepManager(@RequestBody Manager manager){
        try {
            InfosProfessionnelles infosPro = infosProService.getDepartementActuel(manager);
            return ResponseEntity.ok(infosPro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur pendant la récupération de département de managerId :" + manager.getId());
        }
    }

    @GetMapping("/manager/emp")
    @PreAuthorize("hasRole('MANAGER')")  
    public ResponseEntity<?> getEmpParManager(Authentication authentication) {
        
        // Vérification explicite de l'authentification
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "status", 401,
                        "message", "Utilisateur non authentifié",
                        "error", "UNAUTHENTICATED"
                    ));
        }
        
        String userId = (String) authentication.getPrincipal();
        
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "status", 401,
                        "message", "ID utilisateur invalide",
                        "error", "INVALID_PRINCIPAL"
                    ));
        }
        
        try {
            List<InfosProfessionnelles> lesInfos = managerService.findAllInfosParManager(userId);            
            if (lesInfos == null) {
                return ResponseEntity.ok(new ArrayList<>()); 
            }
            
            return ResponseEntity.ok(lesInfos);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                        "status", 404,
                        "message", e.getMessage(),
                        "error", "NOT_FOUND"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", 500,
                        "message", "Erreur interne du serveur",
                        "error", "SERVER_ERROR"
                    ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createInfosPro(@RequestBody InfosProfessionnelles infosPro) {
        try {
            InfosProfessionnelles savedInfosPro = infosProService.create(infosPro);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInfosPro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la création: " + e.getMessage());
        }
    }

    @GetMapping("/allEmpNotAssignedToManager")
    public ResponseEntity<List<InfosProfessionnelles>> getAllInfosProWithoutManager() {
        List<InfosProfessionnelles> infosProList = infosProService.getInfoProForNonManagers();
        return ResponseEntity.ok(infosProList);
    }   
    
    // === READ ALL ===
    @GetMapping
    public ResponseEntity<List<InfosProfessionnelles>> getAllInfosPro() {
        List<InfosProfessionnelles> infosProList = infosProService.getAll();
        return ResponseEntity.ok(infosProList);
    }

    // === READ BY ID ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getInfosProById(@PathVariable String id) {
        try {
            InfosProfessionnelles infosPro = infosProService.getById(id).get();
            return ResponseEntity.ok(infosPro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Infos professionnelles non trouvées pour l'ID: " + id);
        }
    }

    // === changement de département de l'employé ===
    @PostMapping("/changeDep")
    public ResponseEntity<?> updateInfosPro(@RequestBody InfosProfessionnelles infosPro, @PathVariable String idEmp) {
        try {
            InfosProfessionnelles infoProActuel = infosProService.getByIdEmploye(idEmp).get();
            // infoProActuel.setDepartement(infosPro.getDepartement());
            InfosProfessionnelles updatedInfosPro = infosProService.update(infoProActuel);
            return ResponseEntity.ok(updatedInfosPro); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    // === DELETE ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInfosPro(@PathVariable String id) {
        try {
            infosProService.deleteById(id);
            return ResponseEntity.ok("Infos professionnelles supprimées avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur lors de la suppression: " + e.getMessage());
        }
    } 

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInfoPro(@PathVariable String id, @RequestBody InfosProfessionnelles infosProfessionnelles) {
        try {
            // Forcer l'ID depuis le path pour éviter les incohérences
            infosProfessionnelles.setId(id);
            // InfosProfessionnelles infosPro = infosProService.update_info_pro(infosProfessionnelles, id);
            InfosProfessionnelles infosPro = infosProService.update(infosProfessionnelles);
            return createResponse(infosPro, "Information professionnelle modifiée avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return createResponse(null, "Erreur modification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/emp/{id}")
    public ResponseEntity<?> getEmployeInfos(@PathVariable String id) {
        try {
            return ResponseEntity.ok(infosProService.getInfosProfessionnellesByEmployeId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur lors de la récupération des infos pros: " + e.getMessage());
        }
    }

    // @GetMapping("/emp/")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }
    
    
}