package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.JourTravail;
import com.rh.manage.Service.JourTravailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jours-travail")
public class JourTravailController {
    
    @Autowired
    private JourTravailService jourTravailService;
    
    // GET all
    @GetMapping
    public ResponseEntity<List<JourTravail>> getAllJoursTravail() {
        List<JourTravail> jours = jourTravailService.getAllJoursTravail();
        return ResponseEntity.ok(jours);
    }
    
    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<JourTravail> getJourTravailById(@PathVariable Long id) {
        JourTravail jour = jourTravailService.getJourTravailById(id);
        return ResponseEntity.ok(jour);
    }
    
    // GET by code
    @GetMapping("/code/{codeJour}")
    public ResponseEntity<JourTravail> getJourTravailByCode(@PathVariable Integer codeJour) {
        JourTravail jour = jourTravailService.getJourTravailByCode(codeJour);
        return ResponseEntity.ok(jour);
    }
    
    // POST create
    @PostMapping
    public ResponseEntity<?> createJourTravail(@RequestBody JourTravail jourTravail) {
        try {
            JourTravail created = jourTravailService.createJourTravail(jourTravail);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJourTravail(@PathVariable Long id, @RequestBody JourTravail jourTravailDetails) {
        try {
            JourTravail updated = jourTravailService.updateJourTravail(id, jourTravailDetails);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJourTravail(@PathVariable Long id) {
        try {
            jourTravailService.deleteJourTravail(id);
            return ResponseEntity.ok().body(createSuccessResponse("Jour de travail supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        }
    }
    
    // GET jours actifs
    @GetMapping("/actifs")
    public ResponseEntity<List<JourTravail>> getJoursActifs() {
        List<JourTravail> jours = jourTravailService.getJoursActifs();
        return ResponseEntity.ok(jours);
    }
    
    // GET jours inactifs
    @GetMapping("/inactifs")
    public ResponseEntity<List<JourTravail>> getJoursInactifs() {
        List<JourTravail> jours = jourTravailService.getJoursInactifs();
        return ResponseEntity.ok(jours);
    }
    
    // GET weekends
    @GetMapping("/weekends")
    public ResponseEntity<List<JourTravail>> getWeekends() {
        List<JourTravail> jours = jourTravailService.getWeekends();
        return ResponseEntity.ok(jours);
    }
    
    // GET jours ouvrables
    @GetMapping("/ouvrables")
    public ResponseEntity<List<JourTravail>> getJoursOuvrables() {
        List<JourTravail> jours = jourTravailService.getJoursOuvrables();
        return ResponseEntity.ok(jours);
    }
    
    // GET vérifier si jour ouvrable
    @GetMapping("/verifier-ouvrable/{codeJour}")
    public ResponseEntity<Map<String, Object>> isJourOuvrable(@PathVariable Integer codeJour) {
        boolean isOuvrable = jourTravailService.isJourOuvrable(codeJour);
        Map<String, Object> response = new HashMap<>();
        response.put("codeJour", codeJour);
        response.put("estOuvrable", isOuvrable);
        return ResponseEntity.ok(response);
    }
    
    // POST initialiser (pour l'admin)
    @PostMapping("/initialiser")
    public ResponseEntity<?> initialiserJoursSemaine() {
        try {
            jourTravailService.initialiserJoursSemaine();
            return ResponseEntity.ok(createSuccessResponse("Jours de la semaine initialisés avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }
    
    // Helper methods
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
    
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
