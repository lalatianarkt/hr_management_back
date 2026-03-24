package com.rh.manage.Controller;

import com.rh.manage.Model.DemandeAbsence;
import com.rh.manage.Service.DemandeAbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes-absence")
public class DemandeAbsenceController {
    @Autowired 
    DemandeAbsenceService demandeAbsenceService;
    
    @PostMapping
    public ResponseEntity<?> createDemande(@RequestBody DemandeAbsence demande) {
        try {
            System.out.println("le json anlé demande mitsy e : " + demande.getDateHeureAbsenceDebut());
            System.out.println("absence " + demande.getDateHeureAbsenceFin());
            System.out.println("idEmploye " + demande.getEmploye().getId());
            System.out.println("debut " + demande.getDateHeureAbsenceDebut());
            
            DemandeAbsence created = demandeAbsenceService.createDemande(demande);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("BUSINESS_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SERVER_ERROR", "Erreur lors de la création de la demande"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDemande(@PathVariable String id, 
                                          @RequestBody DemandeAbsence demande) {
        try {
            DemandeAbsence updated = demandeAbsenceService.updateDemande(id, demande);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("UPDATE_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SERVER_ERROR", "Erreur lors de la mise à jour"));
        }
    }
    
    @PostMapping("/{id}/approver")
    public ResponseEntity<?> approveDemande(@PathVariable String id,
                                           @RequestParam String approverId) {
        try {
            DemandeAbsence approved = demandeAbsenceService.approveDemande(id, approverId);
            return ResponseEntity.ok(approved);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("STATUS_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SERVER_ERROR", "Erreur lors de l'approbation"));
        }
    }
    
    @PostMapping("/{id}/rejeter")
    public ResponseEntity<?> rejectDemande(@PathVariable String id,
                                          @RequestParam String rejecterId,
                                          @RequestParam(required = false) String raison) {
        try {
            DemandeAbsence rejected = demandeAbsenceService.rejectDemande(id, rejecterId, raison);
            return ResponseEntity.ok(rejected);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("STATUS_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SERVER_ERROR", "Erreur lors du rejet"));
        }
    }
    
    @PostMapping("/{id}/annuler")
    public ResponseEntity<?> cancelDemande(@PathVariable String id,
                                          @RequestParam String employeeId) {
        try {
            DemandeAbsence cancelled = demandeAbsenceService.cancelDemande(id, employeeId);
            return ResponseEntity.ok(cancelled);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("SECURITY_ERROR", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("STATUS_ERROR", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("SERVER_ERROR", "Erreur lors de l'annulation"));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getDemandeById(@PathVariable String id) {
        try {
            DemandeAbsence demande = demandeAbsenceService.getDemandeById(id);
            return ResponseEntity.ok(demande);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<DemandeAbsence>> getDemandesByEmploye(@PathVariable String employeId) {
        List<DemandeAbsence> demandes = demandeAbsenceService.getDemandesByEmploye(employeId);
        return ResponseEntity.ok(demandes);
    }
    
    @GetMapping("/employe/{employeId}/en-attente")
    public ResponseEntity<List<DemandeAbsence>> getPendingDemandesByEmploye(@PathVariable String employeId) {
        List<DemandeAbsence> demandes = demandeAbsenceService.getPendingDemandesByEmploye(employeId);
        return ResponseEntity.ok(demandes);
    }
    
    @GetMapping("/en-attente")
    public ResponseEntity<List<DemandeAbsence>> getAllPendingDemandes() {
        List<DemandeAbsence> demandes = demandeAbsenceService.getAllPendingDemandes();
        return ResponseEntity.ok(demandes);
    }
    
    // @GetMapping("/manager/{managerId}")
    // public ResponseEntity<List<DemandeAbsence>> getDemandesForManager(@PathVariable String managerId) {
    //     List<DemandeAbsence> demandes = demandeAbsenceService.getDemandesForManager(managerId);
    //     return ResponseEntity.ok(demandes);
    // }
    
    @GetMapping("/verifier-disponibilite")
    public ResponseEntity<?> checkAvailability(
            @RequestParam String employeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        boolean isAvailable = demandeAbsenceService.isPeriodAvailable(employeId, debut, fin);
        
        Map<String, Object> response = new HashMap<>();
        response.put("employeId", employeId);
        response.put("debut", debut);
        response.put("fin", fin);
        response.put("disponible", isAvailable);
        
        return ResponseEntity.ok(response);
    }
    
    // @GetMapping("/stats/{mois}/{annee}")
    // public ResponseEntity<?> getStats(@PathVariable int mois, @PathVariable int annee) {
    //     try {
    //         DemandeAbsenceService.DemandeAbsenceStats stats = demandeAbsenceService.getStats(mois, annee);
    //         return ResponseEntity.ok(stats);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body(createErrorResponse("SERVER_ERROR", "Erreur lors du calcul des statistiques"));
    //     }
    // }
    
    @GetMapping("/calendrier/{mois}/{annee}")
    public ResponseEntity<?> getCalendrierAbsences(
            @PathVariable int mois,
            @PathVariable int annee,
            @RequestParam(required = false) String departementId) {
        
        // Implémentation pour retourner les absences formatées pour un calendrier
        // (à adapter selon vos besoins)
        
        Map<String, Object> response = new HashMap<>();
        response.put("mois", mois);
        response.put("annee", annee);
        response.put("departementId", departementId);
        response.put("absences", List.of()); // Liste des absences formatées
        
        return ResponseEntity.ok(response);
    }
    
    private Map<String, String> createErrorResponse(String type, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("type", type);
        error.put("message", message);
        error.put("timestamp", LocalDateTime.now().toString());
        return error;
    }
}