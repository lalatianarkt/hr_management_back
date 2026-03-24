package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.rh.manage.Model.SituationFamiliale;
import com.rh.manage.Service.SituationFamilialeService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/situation-familiale")
// @CrossOrigin(origins = "*")
 // Autorise les appels depuis le frontend
public class SituationFamilialeController {

    @Autowired
    private SituationFamilialeService situationFamilialeService;

    // ✅ 1. Lister toutes les situations familiales
    @GetMapping
    public List<SituationFamiliale> getAllSituations() {
        return situationFamilialeService.findAll();
    }

    // ✅ 2. Récupérer une situation familiale par ID
    @GetMapping("/{id}")
    public ResponseEntity<SituationFamiliale> getSituationById(@PathVariable Integer id) {
        Optional<SituationFamiliale> situation = situationFamilialeService.findById(id);
        return situation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ 3. Ajouter une nouvelle situation familiale
    @PostMapping
    public ResponseEntity<SituationFamiliale> createSituation(@RequestBody SituationFamiliale situationFamiliale) {
        // situationFamiliale.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        SituationFamiliale saved = situationFamilialeService.save(situationFamiliale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ 4. Modifier une situation familiale existante
    @PutMapping("/{id}")
    public ResponseEntity<SituationFamiliale> updateSituation(
            @PathVariable Integer id,
            @RequestBody SituationFamiliale situationFamiliale) {

        try {
            // situationFamiliale.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            SituationFamiliale updated = situationFamilialeService.update(id, situationFamiliale);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ✅ 5. Supprimer une situation familiale
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSituation(@PathVariable Integer id) {
        Optional<SituationFamiliale> existing = situationFamilialeService.findById(id);
        if (existing.isPresent()) {
            situationFamilialeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
