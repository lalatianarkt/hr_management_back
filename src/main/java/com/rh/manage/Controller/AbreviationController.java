package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Abreviation;
import com.rh.manage.Service.AbreviationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/abreviations")
// @CrossOrigin(origins = "*")

public class AbreviationController {
    
    @Autowired
    private AbreviationService abreviationService;

    // Dans AbreviationController.java
    @GetMapping("/search/available")
    public ResponseEntity<List<Abreviation>> getAvailableAbreviations(
            @RequestParam(required = false) String term,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<Abreviation> abreviations;
        
        if (term != null && !term.trim().isEmpty()) {
            abreviations = abreviationService.searchAbreviations(term);
        } else {
            abreviations = abreviationService.getAllOrderedByAbreviation();
        }
        
        // Limiter les résultats
        if (abreviations.size() > limit) {
            abreviations = abreviations.subList(0, limit);
        }
        
        return ResponseEntity.ok(abreviations);
    }
    
    // Créer une nouvelle abréviation
    @PostMapping
    public ResponseEntity<?> createAbreviation(@RequestBody Abreviation abreviation) {
        try {
            Abreviation createdAbreviation = abreviationService.createAbreviation(abreviation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAbreviation);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la création de l'abréviation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Récupérer toutes les abréviations
    @GetMapping
    public ResponseEntity<List<Abreviation>> getAllAbreviations() {
        List<Abreviation> abreviations = abreviationService.getAllAbreviations();
        return ResponseEntity.ok(abreviations);
    }
    
    // Récupérer une abréviation par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAbreviationById(@PathVariable Long id) {
        return abreviationService.getAbreviationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Récupérer une abréviation par son code
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getAbreviationByCode(@PathVariable String code) {
        return abreviationService.getAbreviationByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Mettre à jour une abréviation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAbreviation(@PathVariable Long id, @RequestBody Abreviation abreviation) {
        try {
            Abreviation updatedAbreviation = abreviationService.updateAbreviation(id, abreviation);
            return ResponseEntity.ok(updatedAbreviation);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la mise à jour de l'abréviation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Supprimer une abréviation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAbreviation(@PathVariable Long id) {
        try {
            abreviationService.deleteAbreviation(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Abréviation supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la suppression de l'abréviation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Rechercher des abréviations par mot-clé
    @GetMapping("/search")
    public ResponseEntity<List<Abreviation>> searchAbreviations(@RequestParam String keyword) {
        List<Abreviation> abreviations = abreviationService.searchAbreviations(keyword);
        return ResponseEntity.ok(abreviations);
    }
    
    // Récupérer les abréviations triées par code
    @GetMapping("/ordered/by-code")
    public ResponseEntity<List<Abreviation>> getAbreviationsOrderedByCode() {
        List<Abreviation> abreviations = abreviationService.getAllOrderedByAbreviation();
        return ResponseEntity.ok(abreviations);
    }
    
    // Vérifier si une abréviation existe
    @GetMapping("/exists/{abreviation}")
    public ResponseEntity<Map<String, Boolean>> checkAbreviationExists(@PathVariable String abreviation) {
        boolean exists = abreviationService.existsByAbreviation(abreviation);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}