package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Service.PeriodePaieService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/periodes-paie")
// @CrossOrigin(origins = "*")

public class PeriodePaieController {
    
    private final PeriodePaieService periodePaieService;
    
    @Autowired
    public PeriodePaieController(PeriodePaieService periodePaieService) {
        this.periodePaieService = periodePaieService;
    }
    
    // GET - Récupérer toutes les périodes
    @GetMapping
    public ResponseEntity<List<PeriodePaie>> getAllPeriodePaies() {
        List<PeriodePaie> periodes = periodePaieService.getAllPeriodePaies();
        return ResponseEntity.ok(periodes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PeriodePaie> getPeriodePaieById(@PathVariable String id) {
        return periodePaieService.getPeriodePaieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/actif")
    public ResponseEntity<?> getPeriodeActif() {
        try {
            // Récupérer la période active
            PeriodePaie periodeActif = periodePaieService.getPeriodePaieActif();
            
            if (periodeActif != null) {
                return ResponseEntity.ok(periodeActif);
            } else {
                // Retourner une réponse 404 si aucune période active n'est trouvée
                Map<String, String> response = new HashMap<>();
                response.put("message", "Aucune période active trouvée");
                response.put("status", "NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erroro ++++ : " + e.getMessage());
            // Gestion des erreurs
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur lors de la récupération de la période active");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", "INTERNAL_SERVER_ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/cloture/{id}")
    public ResponseEntity<?> cloturerPeriode(@PathVariable String id, @RequestBody PeriodePaie periodePaie) {
        try {
            PeriodePaie periodeCloturee = periodePaieService.cloturePaie(id, periodePaie);
            if (periodeCloturee != null) {
                return ResponseEntity.ok(periodeCloturee);
            } else {
                return ResponseEntity.badRequest()
                    .body("Erreur lors de la clôture de la période");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur cloture : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur serveur: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createPeriodePaie(@RequestBody PeriodePaie periodePaie) {
        try {
            PeriodePaie createdPeriode = periodePaieService.createPeriodePaie(periodePaie);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPeriode);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de la période");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePeriodePaie(
            @PathVariable String id,
            @RequestBody PeriodePaie periodePaie) {
        try {
            PeriodePaie updatedPeriode = periodePaieService.updatePeriodePaie(id, periodePaie);
            return ResponseEntity.ok(updatedPeriode);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de la période");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePeriodePaie(@PathVariable String id) {
        try {
            periodePaieService.deletePeriodePaie(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la période");
        }
    }
    
    // GET - Rechercher par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<PeriodePaie> getPeriodePaiesByStatut(@PathVariable Integer statut) {
        PeriodePaie periodes = periodePaieService.getPeriodePaiesByStatut(statut).get();
        return ResponseEntity.ok(periodes);
    }
    
    // GET - Rechercher entre deux dates
    @GetMapping("/recherche")
    public ResponseEntity<List<PeriodePaie>> getPeriodePaiesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PeriodePaie> periodes = periodePaieService.getPeriodePaiesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(periodes);
    }
    
    // GET - Récupérer les périodes actives
    @GetMapping("/actives")
    public ResponseEntity<List<PeriodePaie>> getActivePeriodePaies() {
        List<PeriodePaie> periodes = periodePaieService.getActivePeriodePaies();
        return ResponseEntity.ok(periodes);
    }
    
    // GET - Vérifier l'existence d'une période
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsPeriodePaie(@PathVariable String id) {
        boolean exists = periodePaieService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
