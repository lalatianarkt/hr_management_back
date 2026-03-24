package com.rh.manage.Controller;

import com.rh.manage.Model.HoraireJournalier;
import com.rh.manage.Service.HoraireJournalierService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/horaires-journaliers")
public class HoraireJournalierController {

    private HoraireJournalierService horaireService;

    @PostMapping
    public ResponseEntity<?> createHoraire(@RequestBody HoraireJournalier horaire) {
        try {
            HoraireJournalier createdHoraire = horaireService.createHoraire(horaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHoraire);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la création de l'horaire: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHoraireById(@PathVariable String id) {
        try {
            HoraireJournalier horaire = horaireService.getHoraireById(id);
            return ResponseEntity.ok(horaire);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<HoraireJournalier>> getAllHoraires() {
        List<HoraireJournalier> horaires = horaireService.getAllHoraires();
        return ResponseEntity.ok(horaires);
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<HoraireJournalier>> getHorairesActifs() {
        List<HoraireJournalier> horairesActifs = horaireService.getHorairesActifs();
        return ResponseEntity.ok(horairesActifs);
    }

    @GetMapping("/jour/{jourTravailId}")
    public ResponseEntity<?> getHorairesByJourTravail(@PathVariable Integer jourTravailId) {
        try {
            List<HoraireJournalier> horaires = horaireService.getHorairesByJourTravail(jourTravailId);
            return ResponseEntity.ok(horaires);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }

    @GetMapping("/actifs/jour/{jourTravailId}")
    public ResponseEntity<?> getHorairesActifsByJourTravail(@PathVariable Integer jourTravailId) {
        try {
            List<HoraireJournalier> horaires = horaireService.getHorairesActifsByJourTravail(jourTravailId);
            return ResponseEntity.ok(horaires);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }

    @GetMapping("/shift/{isShiftJour}")
    public ResponseEntity<?> getHorairesByShiftType(@PathVariable Boolean isShiftJour) {
        try {
            List<HoraireJournalier> horaires = horaireService.getHorairesByShiftType(isShiftJour);
            return ResponseEntity.ok(horaires);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }

    @GetMapping("/actifs/shift/{isShiftJour}")
    public ResponseEntity<?> getHorairesActifsByShiftType(@PathVariable Boolean isShiftJour) {
        try {
            List<HoraireJournalier> horaires = horaireService.getHorairesActifsByShiftType(isShiftJour);
            return ResponseEntity.ok(horaires);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur serveur: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHoraire(@PathVariable String id, @RequestBody HoraireJournalier horaireDetails) {
        try {
            HoraireJournalier updatedHoraire = horaireService.updateHoraire(id, horaireDetails);
            return ResponseEntity.ok(updatedHoraire);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoraire(@PathVariable String id) {
        try {
            horaireService.deleteHoraire(id);
            return ResponseEntity.ok(Map.of("message", "Horaire désactivé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()));
        }
    }

    @DeleteMapping("/permanent/{id}")
    public ResponseEntity<?> deleteHorairePermanently(@PathVariable String id) {
        try {
            horaireService.deleteHorairePermanently(id);
            return ResponseEntity.ok(Map.of("message", "Horaire supprimé définitivement avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erreur lors de la suppression définitive: " + e.getMessage()));
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> checkHoraireExists(@PathVariable String id) {
        boolean exists = horaireService.existsById(id);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}