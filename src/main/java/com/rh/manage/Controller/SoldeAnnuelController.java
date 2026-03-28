package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.SoldeAnnuel;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.SoldeAnnuelService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solde-annuel")
public class SoldeAnnuelController {

    private final SoldeAnnuelService service;
    @Autowired
    EmployeService employeService;

    public SoldeAnnuelController(SoldeAnnuelService service) {
        this.service = service;
    }

    @GetMapping
    public List<SoldeAnnuel> getAll() {
        return service.getAllSolde();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoldeAnnuel> getById(@PathVariable String id) {
        return service.getSoldeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employe/{idEmploye}")
    public List<SoldeAnnuel> getByEmploye(@PathVariable String idEmploye) {
        return employeService.getById(idEmploye)
                .map(service::getSoldeByEmploye)
                .orElse(List.of());
    }

    @PostMapping("/cloture/employe/{idEmploye}/annee/{annee}")
    public ResponseEntity<?> clotureParEmp(@PathVariable String idEmploye, @PathVariable int annee) {
        try {
            Employe employe = employeService.getById(idEmploye).get();
            SoldeAnnuel soldeAnnuel = service.reporterCongeAnnuel(employe, annee);
            return ResponseEntity.ok(soldeAnnuel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la clôture pour l'employé " + idEmploye + " et l'année " + annee + ": " + e.getMessage());
        }
    }

    @PostMapping("/cloture/globale/{annee}")
    public ResponseEntity<?> clotureGlobale(@PathVariable int annee) {
        try {
            System.out.println("annee : " + annee);
            List<SoldeAnnuel> result = service.clotureGlobale(annee);
            
            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("Aucun solde à clôturer");
            }
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result);
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Année invalide : " + e.getMessage());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la clôture globale : " + e.getMessage());
        }
    }

    @PostMapping
    public SoldeAnnuel create(@RequestBody SoldeAnnuel solde) {
        return service.saveSolde(solde);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoldeAnnuel> update(@PathVariable String id, @RequestBody SoldeAnnuel solde) {
        return service.getSoldeById(id).map(existing -> {
            solde.setId(id);
            return ResponseEntity.ok(service.saveSolde(solde));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteSolde(id);
        return ResponseEntity.noContent().build();
    }

    // ⭐⭐ NOUVEL ENDPOINT : Récupérer les soldes par année
    @GetMapping("/annee/{annee}")
    public ResponseEntity<?> getByAnnee(@PathVariable Integer annee) {
        System.out.println("=== GET /solde-annuel/annee/" + annee + " ===");
        System.out.println("📅 Requête reçue pour l'année : " + annee);
        System.out.println("⏰ Heure : " + LocalDateTime.now());
        
        try {
            // Validation simple
            if (annee == null || annee < 2000 || annee > LocalDate.now().getYear() + 1) {
                System.out.println("❌ Année invalide : " + annee);
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Année invalide. Doit être entre 2000 et " + (LocalDate.now().getYear() + 1));
                error.put("annee", annee);
                return ResponseEntity.badRequest().body(error);
            }
            
            System.out.println("🔍 Recherche des soldes pour l'année " + annee + "...");
            List<SoldeAnnuel> soldes = service.getSoldeByAnnee(annee);
            
            System.out.println("📊 " + (soldes != null ? soldes.size() : 0) + 
                             " soldes trouvés pour l'année " + annee);
            
            if (soldes == null || soldes.isEmpty()) {
                System.out.println("ℹ️  Aucun solde trouvé pour l'année " + annee);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Aucun solde trouvé pour l'année " + annee);
                response.put("annee", annee);
                response.put("count", 0);
                response.put("soldes", List.of());
                response.put("timestamp", LocalDateTime.now());
                return ResponseEntity.ok(response);
            }
            
            // Afficher quelques infos de débogage
            System.out.println("🔍 Détails (max 3) :");
            int maxAfficher = Math.min(soldes.size(), 3);
            for (int i = 0; i < maxAfficher; i++) {
                SoldeAnnuel solde = soldes.get(i);
                String employeId = solde.getEmploye() != null ? solde.getEmploye().getId() : "null";
                System.out.println("   " + (i+1) + ". " + employeId + 
                                 " - Total: " + solde.getNbCongeTotal() + 
                                 ", Pris: " + solde.getNbCongePris());
            }
            if (soldes.size() > 3) {
                System.out.println("   ... et " + (soldes.size() - 3) + " autres");
            }
            
            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", soldes.size() + " soldes trouvés");
            response.put("annee", annee);
            response.put("count", soldes.size());
            response.put("soldes", soldes);
            response.put("timestamp", LocalDateTime.now());
            
            System.out.println("✅ Requête traitée avec succès");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌❌❌ ERREUR lors de la recherche par année " + annee);
            System.out.println("📛 Message : " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur serveur lors de la recherche");
            error.put("annee", annee);
            error.put("error", e.getMessage());
            error.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Endpoint pour récupérer les années disponibles
    @GetMapping("/annees/disponibles")
    public ResponseEntity<?> getAnneesDisponibles() {
        System.out.println("=== GET /solde-annuel/annees/disponibles ===");
        
        try {
            List<Integer> annees = service.getAnneesDisponibles();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", annees.size() + " années trouvées");
            response.put("count", annees.size());
            response.put("annees", annees);
            response.put("timestamp", LocalDateTime.now());
            
            System.out.println("✅ " + annees.size() + " années disponibles retournées");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la récupération des années : " + e.getMessage());
            
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la récupération des années");
            error.put("error", e.getMessage());
            
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
