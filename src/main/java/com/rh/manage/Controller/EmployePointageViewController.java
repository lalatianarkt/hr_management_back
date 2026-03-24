package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Service.EmployePointageViewService;
import com.rh.manage.View.EmployePointageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employes-pointage")
public class EmployePointageViewController {
    
    @Autowired
    private EmployePointageViewService employePointageViewService;
    
    /**
     * Récupérer tous les employés actifs
     */
    @GetMapping("/all")
    public ResponseEntity<List<EmployePointageView>> getAllEmployesActifs() {
        try {
            List<EmployePointageView> employes = employePointageViewService.getAllEmployesActifs();
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer tous les employés actifs (paginated)
     */
    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllEmployesActifsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
            
            Page<EmployePointageView> employesPage = employePointageViewService.getAllEmployesActifs(pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", employesPage.getContent());
            response.put("currentPage", employesPage.getNumber());
            response.put("totalPages", employesPage.getTotalPages());
            response.put("totalElements", employesPage.getTotalElements());
            response.put("pageSize", employesPage.getSize());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la récupération des employés"));
        }
    }
    
    /**
     * Récupérer un employé par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployePointageView> getEmployeById(@PathVariable String id) {
        try {
            Optional<EmployePointageView> employe = employePointageViewService.getEmployeById(id);
            
            if (employe.isPresent() && employe.get().isActif()) {
                return ResponseEntity.ok(employe.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer un employé par matricule
     */
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<EmployePointageView> getEmployeByMatricule(@PathVariable String matricule) {
        try {
            Optional<EmployePointageView> employe = employePointageViewService.getEmployeByMatricule(matricule);
            
            if (employe.isPresent() && employe.get().isActif()) {
                return ResponseEntity.ok(employe.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Rechercher des employés
     */
    @GetMapping("/search")
    public ResponseEntity<List<EmployePointageView>> searchEmployes(
            @RequestParam(required = false) String q) {
        
        try {
            List<EmployePointageView> employes = employePointageViewService.searchEmployes(q);
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Recherche avec filtres avancés
     */
    @GetMapping("/search/filters")
    public ResponseEntity<List<EmployePointageView>> searchWithFilters(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String departement,
            @RequestParam(required = false) String poste) {
        
        try {
            List<EmployePointageView> employes = employePointageViewService.searchWithFilters(
                    matricule, nom, prenom, departement, poste);
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    /**
     * Récupérer les employés par département
     */
    @GetMapping("/departement/{departement}")
    public ResponseEntity<List<EmployePointageView>> getEmployesByDepartement(
            @PathVariable String departement) {
        
        try {
            List<EmployePointageView> employes = employePointageViewService.getEmployesByDepartement(departement);
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer les employés par poste
     */
    @GetMapping("/poste/{poste}")
    public ResponseEntity<List<EmployePointageView>> getEmployesByPoste(@PathVariable String poste) {
        try {
            List<EmployePointageView> employes = employePointageViewService.getEmployesByPoste(poste);
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer les statistiques
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        try {
            Map<String, Long> statsByDepartement = employePointageViewService.getStatsByDepartement();
            Map<String, Long> statsByPoste = employePointageViewService.getStatsByPoste();
            List<String> departements = employePointageViewService.getAllDepartements();
            List<String> postes = employePointageViewService.getAllPostes();
            long total = employePointageViewService.countEmployesActifs();
            
            Map<String, Object> response = new HashMap<>();
            response.put("totalEmployes", total);
            response.put("statsByDepartement", statsByDepartement);
            response.put("statsByPoste", statsByPoste);
            response.put("departements", departements);
            response.put("postes", postes);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la récupération des statistiques"));
        }
    }
    
    /**
     * Récupérer les départements distincts
     */
    @GetMapping("/departements")
    public ResponseEntity<List<String>> getAllDepartements() {
        try {
            List<String> departements = employePointageViewService.getAllDepartements();
            return ResponseEntity.ok(departements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer les postes distincts
     */
    @GetMapping("/postes")
    public ResponseEntity<List<String>> getAllPostes() {
        try {
            List<String> postes = employePointageViewService.getAllPostes();
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer les employés pour dropdown (pour les filtres)
     */
    // @GetMapping("/dropdown")
    // public ResponseEntity<List<Map<String, Object>>> getEmployesForDropdown() {
    //     try {
    //         List<Map<String, Object>> employes = employePointageViewService.getEmployesForDropdown();
    //         return ResponseEntity.ok(employes);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(null);
    //     }
    // }
    
    /**
     * Vérifier si un matricule existe
     */
    @GetMapping("/exists/matricule/{matricule}")
    public ResponseEntity<Map<String, Boolean>> checkMatriculeExists(@PathVariable String matricule) {
        try {
            boolean exists = employePointageViewService.matriculeExists(matricule);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("exists", false));
        }
    }
    
    /**
     * Récupérer plusieurs employés par leurs IDs
     */
    @PostMapping("/by-ids")
    public ResponseEntity<List<EmployePointageView>> getEmployesByIds(@RequestBody List<String> ids) {
        try {
            List<EmployePointageView> employes = employePointageViewService.getAllEmployesActifs()
                    .stream()
                    .filter(e -> ids.contains(e.getId()))
                    .collect(java.util.stream.Collectors.toList());
            
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
