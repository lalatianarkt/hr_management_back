package com.rh.manage.Controller;

import com.rh.manage.Model.CompetenceEmploye;
import com.rh.manage.Service.CompetenceEmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/competences-employes")
// @CrossOrigin(origins = "*")

public class CompetenceEmployeController {
    
    @Autowired
    private CompetenceEmployeService competenceEmployeService;
    
    // === CREATE ===
    @PostMapping
    public ResponseEntity<?> createCompetenceEmploye(@RequestBody CompetenceEmploye competenceEmploye) {
        try {
            CompetenceEmploye savedCompetenceEmploye = competenceEmployeService.create(competenceEmploye);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCompetenceEmploye);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Erreur de validation", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de créer l'association compétence-employé"));
        }
    }
    
    // === CREATE SIMPLIFIÉ ===
    @PostMapping("/employe/{employeId}/competence/{competenceId}")
    public ResponseEntity<?> addCompetenceToEmploye(
            @PathVariable String employeId,
            @PathVariable String competenceId,
            @RequestBody Map<String, Object> request) {
        
        try {
            Integer niveau = (Integer) request.get("niveau");
            String dateAcquisitionStr = (String) request.get("dateAcquisition");
            LocalDate dateAcquisition = LocalDate.parse(dateAcquisitionStr);
            
            CompetenceEmploye competenceEmploye = competenceEmployeService.addCompetenceToEmploye(
                employeId, competenceId, niveau, dateAcquisition);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(competenceEmploye);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Erreur de validation", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible d'ajouter la compétence à l'employé"));
        }
    }
    
    // === READ ALL ===
    @GetMapping
    public ResponseEntity<List<CompetenceEmploye>> getAllCompetencesEmployes() {
        List<CompetenceEmploye> competencesEmployes = competenceEmployeService.getAll();
        return ResponseEntity.ok(competencesEmployes);
    }
    
    // === READ BY ID ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompetenceEmployeById(@PathVariable String id) {
        Optional<CompetenceEmploye> competenceEmploye = competenceEmployeService.getById(id);
        if (competenceEmploye.isPresent()) {
            return ResponseEntity.ok(competenceEmploye.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Association compétence-employé non trouvée avec l'ID: " + id));
        }
    }
    
    // === READ BY EMPLOYE ===
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<CompetenceEmploye>> getCompetencesByEmploye(@PathVariable String employeId) {
        List<CompetenceEmploye> competencesEmployes = competenceEmployeService.getByEmploye(employeId);
        return ResponseEntity.ok(competencesEmployes);
    }
    
    // === READ BY COMPETENCE ===
    @GetMapping("/competence/{competenceId}")
    public ResponseEntity<List<CompetenceEmploye>> getEmployesByCompetence(@PathVariable String competenceId) {
        List<CompetenceEmploye> competencesEmployes = competenceEmployeService.getByCompetence(competenceId);
        return ResponseEntity.ok(competencesEmployes);
    }
    
    // === READ SPECIFIQUE ===
    @GetMapping("/employe/{employeId}/competence/{competenceId}")
    public ResponseEntity<?> getCompetenceEmploye(@PathVariable String employeId, @PathVariable String competenceId) {
        Optional<CompetenceEmploye> competenceEmploye = competenceEmployeService.getByEmployeAndCompetence(employeId, competenceId);
        if (competenceEmploye.isPresent()) {
            return ResponseEntity.ok(competenceEmploye.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Association non trouvée pour cet employé et cette compétence"));
        }
    }
    
    // === UPDATE ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompetenceEmploye(@PathVariable String id, @RequestBody CompetenceEmploye competenceEmployeDetails) {
        try {
            CompetenceEmploye updatedCompetenceEmploye = competenceEmployeService.update(id, competenceEmployeDetails);
            return ResponseEntity.ok(updatedCompetenceEmploye);
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
                    .body(createErrorResponse("Erreur interne", "Impossible de mettre à jour l'association compétence-employé"));
        }
    }
    
    // === DELETE ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompetenceEmploye(@PathVariable String id) {
        try {
            competenceEmployeService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Association compétence-employé supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de supprimer l'association compétence-employé"));
        }
    }
    
    // === DELETE SPECIFIQUE ===
    @DeleteMapping("/employe/{employeId}/competence/{competenceId}")
    public ResponseEntity<?> deleteCompetenceEmploye(@PathVariable String employeId, @PathVariable String competenceId) {
        try {
            Optional<CompetenceEmploye> competenceEmploye = competenceEmployeService.getByEmployeAndCompetence(employeId, competenceId);
            if (competenceEmploye.isPresent()) {
                competenceEmployeService.deleteById(competenceEmploye.get().getId());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Compétence retirée de l'employé avec succès");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Non trouvé", "Association non trouvée pour cet employé et cette compétence"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de retirer la compétence de l'employé"));
        }
    }
    
    // === STATISTIQUES ===
    @GetMapping("/employe/{employeId}/moyenne")
    public ResponseEntity<Map<String, Double>> getMoyenneNiveauByEmploye(@PathVariable String employeId) {
        Double moyenne = competenceEmployeService.getAverageNiveauByEmploye(employeId);
        Map<String, Double> response = new HashMap<>();
        response.put("moyenne", moyenne != null ? moyenne : 0.0);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/competence/{competenceId}/moyenne")
    public ResponseEntity<Map<String, Double>> getMoyenneNiveauByCompetence(@PathVariable String competenceId) {
        Double moyenne = competenceEmployeService.getAverageNiveauByCompetence(competenceId);
        Map<String, Double> response = new HashMap<>();
        response.put("moyenne", moyenne != null ? moyenne : 0.0);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/employe/{employeId}/count")
    public ResponseEntity<Map<String, Long>> countCompetencesByEmploye(@PathVariable String employeId) {
        long count = competenceEmployeService.countByEmploye(employeId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/competence/{competenceId}/count")
    public ResponseEntity<Map<String, Long>> countEmployesByCompetence(@PathVariable String competenceId) {
        long count = competenceEmployeService.countByCompetence(competenceId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
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
