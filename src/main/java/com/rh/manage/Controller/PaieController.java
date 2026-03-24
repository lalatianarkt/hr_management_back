package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.PaieGenerationRequest;
import com.rh.manage.Model.Paie;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Service.PaieService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paies")
public class PaieController {
    @Autowired
    PaieService paieService;

    @GetMapping("/employe/{employeId}/derniere-non-cloturee")
    public ResponseEntity<?> getDernierePaieNonCloturee(@PathVariable String employeId) {
        try {
            return ResponseEntity.ok(paieService.getDernierePaieNonCloturee(employeId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    } 
    
    @GetMapping("/employe/{employeId}/has-non-cloturee")
    public ResponseEntity<Boolean> hasPaieNonCloturee(@PathVariable String employeId) {
        boolean hasNonCloturee = paieService.hasPaieNonCloturee(employeId);
        return ResponseEntity.ok(hasNonCloturee);
    }

    @PostMapping("/generer-multiples")
    public ResponseEntity<?> genererPaiesMultiples(@RequestBody PaieGenerationRequest requestData) {
        try {
            // Validation
            if (requestData.getLes_paies() == null || requestData.getLes_paies().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Aucune donnée reçue"
                ));
            }
            
            // Appel du service
            Map<String, Object> result = paieService.genererPaiesMultiples(requestData.getPeriode(), requestData.getLes_paies());
            
            @SuppressWarnings("unchecked")
            List<Paie> paiesGenerees = (List<Paie>) result.get("paiesGenerees");
            @SuppressWarnings("unchecked")
            List<String> erreurs = (List<String>) result.get("erreurs");
            @SuppressWarnings("unchecked")
            List<String> doublons = (List<String>) result.get("doublons");
            int count = (int) result.get("count");
            int totalTraite = (int) result.get("totalTraite");
            
            // Construire la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", buildSuccessMessage(count, erreurs.size(), doublons.size(), totalTraite));
            response.put("paies", paiesGenerees);
            response.put("count", count);
            response.put("totalEmployes", totalTraite);
            
            // Ajouter les erreurs et doublons si nécessaire
            if (!erreurs.isEmpty()) {
                response.put("erreurs", erreurs);
            }
            if (!doublons.isEmpty()) {
                response.put("doublons", doublons);
            }
            
            // Ajouter un résumé
            response.put("resume", Map.of(
                "generes", count,
                "doublons", doublons.size(),
                "erreurs", erreurs.size(),
                "total", totalTraite
            ));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Erreur lors de la génération des fiches de paie" + e.getMessage(),
                "detail", e.getMessage()
            ));
        }
    }

    private String buildSuccessMessage(int succes, int erreurs, int doublons, int total) {
        StringBuilder message = new StringBuilder();
        
        message.append("✅ ").append(succes).append(" fiche(s) de paie générée(s) avec succès");
        
        if (doublons > 0) {
            message.append(", ⚠️ ").append(doublons).append(" doublon(s) ignoré(s)");
        }
        
        if (erreurs > 0) {
            message.append(", ❌ ").append(erreurs).append(" erreur(s)");
        }
        
        message.append(" (Total traité: ").append(total).append(" employé(s))");
        
        return message.toString();
    }
    
    @GetMapping
    public ResponseEntity<List<Paie>> getAll() {
        List<Paie> paies = paieService.getAll();
        return ResponseEntity.ok(paies);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Paie> getById(@PathVariable String id) {
        return paieService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/with-employe")
    public ResponseEntity<Paie> getByIdWithEmploye(@PathVariable String id) {
        return paieService.getByIdWithEmploye(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<Paie>> searchPaies(
            @RequestParam(required = false) Integer matricule,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Integer statutCloture,
            Pageable pageable) {
        
        Page<Paie> paies = paieService.searchPaies(
                matricule, nom, prenom, 
                dateDebut, dateFin, statutCloture, 
                pageable);
        
        return ResponseEntity.ok(paies);
    }
    
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<List<Paie>> getByMatricule(@PathVariable Integer matricule) {
        List<Paie> paies = paieService.getByMatricule(matricule);
        return ResponseEntity.ok(paies);
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<Paie>> getByEmployeId(@PathVariable String employeId) {
        List<Paie> paies = paieService.getByEmployeId(employeId);
        return ResponseEntity.ok(paies);
    }
    
    // // @GetMapping("/periode")
    // // public ResponseEntity<List<Paie>> getByPeriode(
    // //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
    // //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
    // //     List<Paie> paies = paieService.getByPeriode(dateDebut, dateFin);
    // //     return ResponseEntity.ok(paies);
    // // }
    
    // @GetMapping("/mois-annee")
    // public ResponseEntity<List<Paie>> getByMoisAndAnnee(
    //         @RequestParam int mois,
    //         @RequestParam int annee) {
        
    //     List<Paie> paies = paieService.getByMoisAndAnnee(mois, annee);
    //     return ResponseEntity.ok(paies);
    // }
    
    // @GetMapping("/statut-cloture/{statut}")
    // public ResponseEntity<List<Paie>> getByStatutCloture(@PathVariable Integer statut) {
    //     List<Paie> paies = paieService.getByStatutCloture(statut);
    //     return ResponseEntity.ok(paies);
    // }
    
    // @GetMapping("/employe/{employeId}/last")
    // public ResponseEntity<Paie> getLastPaieForEmploye(@PathVariable String employeId) {
    //     return paieService.getLastPaieForEmploye(employeId)
    //             .map(ResponseEntity::ok)
    //             .orElse(ResponseEntity.notFound().build());
    // }
    
    @GetMapping("/count-periode")
    public ResponseEntity<Long> countByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        long count = paieService.countPaiesByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/sum-salaire-base")
    public ResponseEntity<BigDecimal> sumSalaireBaseByPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        BigDecimal sum = paieService.sumSalaireBaseByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(sum);
    }
    
    @GetMapping("/societe/{societeId}")
    public ResponseEntity<List<Paie>> getBySocieteId(@PathVariable Integer societeId) {
        List<Paie> paies = paieService.getBySocieteId(societeId);
        return ResponseEntity.ok(paies);
    }
    
    @PostMapping
    public ResponseEntity<Paie> create(@RequestBody Paie paie) {
        try {
            Paie created = paieService.create(paie);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Paie> update(@PathVariable String id, @RequestBody Paie paie) {
        try {
            Paie updated = paieService.update(id, paie);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/cloturer")
    public ResponseEntity<Paie> cloturerPaie(@PathVariable String id) {
        try {
            Paie updated = paieService.cloturerPaie(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/decloturer")
    public ResponseEntity<Paie> decloturerPaie(@PathVariable String id) {
        try {
            Paie updated = paieService.decloturerPaie(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
        
    // @GetMapping("/{id}/calculate")
    // public ResponseEntity<Paie> calculateMontants(@PathVariable String id) {
    //     try {
    //         Paie paie = paieService.calculateMontants(id);
    //         return ResponseEntity.ok(paie);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.notFound().build();
    //     }
    // }
    
    // @GetMapping("/check-exists")
    // public ResponseEntity<Boolean> checkPaieExists(
    //         @RequestParam String employeId,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
    //     boolean exists = paieService.existsPaieForEmployeInPeriode(employeId, dateDebut, dateFin);
    //     return ResponseEntity.ok(exists);
    // }
}
