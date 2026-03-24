package com.rh.manage.Controller;

import com.rh.manage.Model.Competence;
import com.rh.manage.Service.CompetenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/competences")
// @CrossOrigin(origins = "*")

public class CompetenceController {
    
    @Autowired
    private CompetenceService competenceService;
    

    // @PutMapping
    // public ResponseEntity<?> updateCompetence(@PathVariable String id, @RequestBody Competence competenceDetails){
    //     try {
    //     Competence updatedCompetence = competenceService.update(id, competenceDetails);
    //     return ResponseEntity.ok(updatedCompetence);
    // } catch (RuntimeException e) {
    //     if (e.getMessage().contains("non trouvée")) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //                 .body(createErrorResponse("Non trouvé", e.getMessage()));
    //     } else {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                 .body(createErrorResponse("Erreur de validation", e.getMessage()));
    //     }
    // } catch (Exception e) {
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body(createErrorResponse("Erreur interne", "Impossible de mettre à jour la compétence"));
    // }
    
    // === CREATE ===
    @PostMapping
    public ResponseEntity<?> createCompetence(@RequestBody Competence competence) {
        try {
            Competence savedCompetence = competenceService.create(competence);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCompetence);
            
        } catch (RuntimeException e) {
            System.out.println("=== ERREUR RUNTIME ===");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Erreur de validation", e.getMessage()));
        } catch (Exception e) {
            System.out.println("=== ERREUR GENERALE ===");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de créer la compétence: " + e.getMessage()));
        }
    }
    
    // === READ ALL ===
    @GetMapping
    public ResponseEntity<List<Competence>> getAllCompetences() {
        List<Competence> competences = competenceService.getAll();
        return ResponseEntity.ok(competences);
    }
    
    // === READ BY ID ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompetenceById(@PathVariable String id) {
        Optional<Competence> competence = competenceService.getById(id);
        if (competence.isPresent()) {
            return ResponseEntity.ok(competence.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Compétence non trouvée avec l'ID: " + id));
        }
    }
    
    // === READ BY CATEGORIE ===
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<Competence>> getCompetencesByCategorie(@PathVariable String categorieId) {
        List<Competence> competences = competenceService.getByCategorie(categorieId);
        return ResponseEntity.ok(competences);
    }
    
    // === READ BY NOM ===
    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getCompetenceByNom(@PathVariable String nom) {
        Optional<Competence> competence = competenceService.getByNom(nom);
        if (competence.isPresent()) {
            return ResponseEntity.ok(competence.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Compétence non trouvée avec le nom: " + nom));
        }
    }
    
    // === UPDATE ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompetence(@PathVariable String id, @RequestBody Competence competenceDetails) {
        try {
            Competence updatedCompetence = competenceService.update(id, competenceDetails);
            return ResponseEntity.ok(updatedCompetence);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvée")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Non trouvé", e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Erreur de validation", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de mettre à jour la compétence"));
        }
    } 
    
    // === DELETE ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompetence(@PathVariable String id) {
        try {
            competenceService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Compétence supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de supprimer la compétence"));
        }
    }
    
    // === RECHERCHE ===
    @GetMapping("/search")
    public ResponseEntity<List<Competence>> searchCompetences(@RequestParam String q) {
        List<Competence> competences = competenceService.search(q);
        return ResponseEntity.ok(competences);
    }
    
    // === RECHERCHE PAR CATEGORIE ===
    @GetMapping("/search/categorie/{categorieId}")
    public ResponseEntity<List<Competence>> searchCompetencesByCategorie(
            @PathVariable String categorieId, 
            @RequestParam String q) {
        List<Competence> competences = competenceService.searchByCategorie(q, categorieId);
        return ResponseEntity.ok(competences);
    }
    
    // === VÉRIFICATION EXISTENCE ===
    @GetMapping("/exists/{nom}")
    public ResponseEntity<Map<String, Boolean>> checkIfExists(@PathVariable String nom) {
        boolean exists = competenceService.existsByNom(nom);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // === COMPTAGE ===
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCompetences() {
        long count = competenceService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // === COMPTAGE PAR CATEGORIE ===
    @GetMapping("/count/categorie/{categorieId}")
    public ResponseEntity<Map<String, Long>> countCompetencesByCategorie(@PathVariable String categorieId) {
        long count = competenceService.countByCategorie(categorieId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // === SANS CATEGORIE ===
    @GetMapping("/sans-categorie")
    public ResponseEntity<List<Competence>> getCompetencesWithoutCategorie() {
        List<Competence> competences = competenceService.getWithoutCategorie();
        return ResponseEntity.ok(competences);
    }
    
    // Méthode utilitaire pour les réponses d'erreur
    private Map<String, String> createErrorResponse(String type, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", type);
        errorResponse.put("message", message);
        return errorResponse;
    }
}