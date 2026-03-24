package com.rh.manage.Controller;

import com.rh.manage.Model.Formation;
import com.rh.manage.Service.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/formations")
// @CrossOrigin(origins = "*")

public class FormationController {
    
    @Autowired
    private FormationService formationService;
    
    // === CREATE ===
    @PostMapping
    public ResponseEntity<?> createFormation(@RequestBody Formation formation) {
        try {
            Formation savedFormation = formationService.create(formation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFormation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Erreur de validation", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de créer la formation"));
        }
    }
    
    // === READ ALL ===
    @GetMapping
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAll();
        return ResponseEntity.ok(formations);
    }
    
    // === READ BY ID ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getFormationById(@PathVariable String id) {
        Optional<Formation> formation = formationService.getById(id);
        if (formation.isPresent()) {
            return ResponseEntity.ok(formation.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Formation non trouvée avec l'ID: " + id));
        }
    }
    
    // === READ BY NOM ===
    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getFormationByNom(@PathVariable String nom) {
        Optional<Formation> formation = formationService.getByNom(nom);
        if (formation.isPresent()) {
            return ResponseEntity.ok(formation.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Formation non trouvée avec le nom: " + nom));
        }
    }
    
    // === UPDATE ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormation(@PathVariable String id, @RequestBody Formation formationDetails) {
        try {
            Formation updatedFormation = formationService.update(id, formationDetails);
            return ResponseEntity.ok(updatedFormation);
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
                    .body(createErrorResponse("Erreur interne", "Impossible de mettre à jour la formation"));
        }
    }
    
    // === DELETE ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFormation(@PathVariable String id) {
        try {
            formationService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Formation supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de supprimer la formation"));
        }
    }
    
    // === RECHERCHE ===
    @GetMapping("/search")
    public ResponseEntity<List<Formation>> searchFormations(@RequestParam String q) {
        List<Formation> formations = formationService.search(q);
        return ResponseEntity.ok(formations);
    }
    
    // === FILTRES ===
    @GetMapping("/filtre/duree-min/{dureeMin}")
    public ResponseEntity<List<Formation>> getFormationsByDureeMin(@PathVariable Integer dureeMin) {
        List<Formation> formations = formationService.getByDureeMin(dureeMin);
        return ResponseEntity.ok(formations);
    }
    
    @GetMapping("/filtre/duree-max/{dureeMax}")
    public ResponseEntity<List<Formation>> getFormationsByDureeMax(@PathVariable Integer dureeMax) {
        List<Formation> formations = formationService.getByDureeMax(dureeMax);
        return ResponseEntity.ok(formations);
    }
    
    @GetMapping("/filtre/cout-max/{coutMax}")
    public ResponseEntity<List<Formation>> getFormationsByCoutMax(@PathVariable BigDecimal coutMax) {
        List<Formation> formations = formationService.getByCoutMax(coutMax);
        return ResponseEntity.ok(formations);
    }
    
    // === AVEC/ SANS PREREQUIS ===
    @GetMapping("/avec-prerequis")
    public ResponseEntity<List<Formation>> getFormationsAvecPrerequis() {
        List<Formation> formations = formationService.getAvecPrerequis();
        return ResponseEntity.ok(formations);
    }
    
    @GetMapping("/sans-prerequis")
    public ResponseEntity<List<Formation>> getFormationsSansPrerequis() {
        List<Formation> formations = formationService.getSansPrerequis();
        return ResponseEntity.ok(formations);
    }
    
    // === VÉRIFICATION EXISTENCE ===
    @GetMapping("/exists/{nom}")
    public ResponseEntity<Map<String, Boolean>> checkIfExists(@PathVariable String nom) {
        boolean exists = formationService.existsByNom(nom);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // === COMPTAGE ===
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countFormations() {
        long count = formationService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // === STATISTIQUES ===
    @GetMapping("/statistiques/cout-moyen")
    public ResponseEntity<Map<String, BigDecimal>> getCoutMoyen() {
        BigDecimal coutMoyen = formationService.getCoutMoyen();
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("coutMoyen", coutMoyen);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/statistiques/duree-moyenne")
    public ResponseEntity<Map<String, Double>> getDureeMoyenne() {
        Double dureeMoyenne = formationService.getDureeMoyenne();
        Map<String, Double> response = new HashMap<>();
        response.put("dureeMoyenne", dureeMoyenne);
        return ResponseEntity.ok(response);
    }
    
    // Méthode utilitaire pour les réponses d'erreur
    private Map<String, String> createErrorResponse(String type, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", type);
        errorResponse.put("message", message);
        return errorResponse;
    }
}