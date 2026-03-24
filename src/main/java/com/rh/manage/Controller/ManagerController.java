package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.ManagerDTO;
import com.rh.manage.Dto.ManagerUpdateRequest;
import com.rh.manage.Model.Manager;
import com.rh.manage.Service.DepartementManagerService;
import com.rh.manage.Service.JwtService;
import com.rh.manage.Service.ManagerService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @Autowired
    TokenService tokenService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/by-departement/{idDepartement}")
    public ResponseEntity<Manager> getManagerActuelParDepartement(@PathVariable String idDepartement) {
        try {
            // Validation
            if (idDepartement == null || idDepartement.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Recherche
            Manager manager = managerService.findManagerActuelByDepartement(idDepartement);
            
            // Retour
            return manager != null 
                    ? ResponseEntity.ok(manager)
                    : ResponseEntity.notFound().build();
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    } 

    /**
     * Retirer un manager de tous ses employés
     * PUT /api/managers/{id}/remove-from-employees
     */
    @PutMapping("/{id}/remove-from-employees")
    public ResponseEntity<Map<String, Object>> removeManagerFromEmployees(@PathVariable String id) {
        Map<String, Object> result = managerService.removeManagerFromEmployees(id);
        
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * Obtenir la liste des employés d'un manager
     * GET /api/managers/{id}/employes
     */
    @GetMapping("/{id}/employes")
    public ResponseEntity<Map<String, Object>> getEmployesByManager(@PathVariable String id) {
        Map<String, Object> result = managerService.getEmployesByManager(id);
        
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    // tokenService.validateToken(authHeader);

    @GetMapping
    public ResponseEntity<?> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/{id}")
    public Manager getManagerById(@PathVariable String id) {
        return managerService.getManagerById(id)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé avec id : " + id));
    }

    @PostMapping("/insertManagerDepartment")
    public ResponseEntity<?> insertManagerToDepartement(@RequestBody Manager manager) {
        try {
            // departementManagerService.insertManagerWithDepartement(managerDTO);
            managerService.saveManager(manager);
            // ✅ SUCCÈS
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Manager affecté au département avec succès");
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            // ❌ ERREUR DE VALIDATION
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("type", "VALIDATION_ERROR");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            // ❌ ERREUR TECHNIQUE
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur interne du serveur lors de l'affectation");
            errorResponse.put("type", "INTERNAL_ERROR");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            // Log technique détaillé (en développement)
            System.err.println("Erreur technique: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // POST /api/managers
    @PostMapping
    public ResponseEntity<?> createManager(@RequestBody Manager manager, @RequestHeader("Authorization") String authHeader) {
        try {
            tokenService.validateToken(authHeader);
            return ResponseEntity.ok(managerService.affecter(manager));      
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // PUT /api/managers/{id}
    // Archiver 
    @PutMapping("/update/{id}")
    public Manager updateManager(@PathVariable String id, @RequestBody Manager manager) {
        manager.setId(id);
        return managerService.archiver(manager);
    }

    // DELETE /api/managers/{id}
    @DeleteMapping("/{id}")
    public void deleteManager(@PathVariable String id) {
        managerService.deleteManager(id);
    } 

    /**
     * Archiver un manager
     * POST /api/managers/{id}/archiver
     */
    @PostMapping("/{id}/archiver")
    public ResponseEntity<Map<String, Object>> archiverManager(@PathVariable String id) {
        Map<String, Object> result = managerService.archiverManager(id);
        
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * Réactiver un manager
     * POST /api/managers/{id}/reactiver
     */
    @PostMapping("/{id}/reactiver")
    public ResponseEntity<Map<String, Object>> reactiverManager(@PathVariable String id) {
        Map<String, Object> result = managerService.reactiverManager(id);
        
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    } 

    /**
     * Lister les managers actifs
     * GET /api/managers/actifs
     */
    @GetMapping("/actifs")
    public ResponseEntity<List<Manager>> getManagersActifs() {
        List<Manager> managers = managerService.getManagersActifs();
        return ResponseEntity.ok(managers);
    } 

    // ManagerController.java
    @PutMapping("/departements/{departementId}/affecter-manager")
    public ResponseEntity<Map<String, Object>> affecterManagerADepartement(
            @PathVariable String departementId,
            @RequestBody Map<String, String> request) {
        
        String managerId = request.get("managerId");
        Map<String, Object> result = managerService.affecterManagerADepartement(managerId, departementId);
        
        boolean success = (boolean) result.get("success");
        
        if (success) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    } 

    
}
