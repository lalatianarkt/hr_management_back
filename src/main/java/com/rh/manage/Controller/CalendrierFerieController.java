package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.CalendrierFerie;
import com.rh.manage.Service.CalendrierFerieService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendrier-ferie")
public class CalendrierFerieController {
    
    @Autowired
    private CalendrierFerieService calendrierFerieService;
    
    // Créer un jour férié
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CalendrierFerie calendrierFerie) {
        try {
            CalendrierFerie created = calendrierFerieService.create(calendrierFerie);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de la création du jour férié : " + e.getMessage(), 
                              HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Mettre à jour un jour férié
    @PutMapping("/{id}")
    public ResponseEntity<CalendrierFerie> update(@PathVariable Long id, @RequestBody CalendrierFerie calendrierFerie) {
        CalendrierFerie updated = calendrierFerieService.update(id, calendrierFerie);
        return ResponseEntity.ok(updated);
    }
    
    // Supprimer un jour férié
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        calendrierFerieService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Désactiver un jour férié (suppression logique)
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CalendrierFerie> deactivate(@PathVariable Long id) {
        CalendrierFerie deactivated = calendrierFerieService.deactivate(id);
        return ResponseEntity.ok(deactivated);
    }
    
    // Récupérer tous les jours fériés
    @GetMapping
    public ResponseEntity<List<CalendrierFerie>> getAll() {
        List<CalendrierFerie> feries = calendrierFerieService.getAll();
        return ResponseEntity.ok(feries);
    }
    
    // Récupérer par ID
    @GetMapping("/{id}")
    public ResponseEntity<CalendrierFerie> getById(@PathVariable Long id) {
        CalendrierFerie calendrierFerie = calendrierFerieService.getById(id);
        return ResponseEntity.ok(calendrierFerie);
    }
    
    // Récupérer par date spécifique
    @GetMapping("/date")
    public ResponseEntity<List<CalendrierFerie>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CalendrierFerie> feries = calendrierFerieService.getByDate(date);
        return ResponseEntity.ok(feries);
    }
    
    // Récupérer les jours fériés actifs
    @GetMapping("/active")
    public ResponseEntity<List<CalendrierFerie>> getActiveFeries() {
        List<CalendrierFerie> feries = calendrierFerieService.getActiveFeries();
        return ResponseEntity.ok(feries);
    }
    
    // Récupérer par plage de dates
    @GetMapping("/range")
    public ResponseEntity<List<CalendrierFerie>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CalendrierFerie> feries = calendrierFerieService.getByDateRange(start, end);
        return ResponseEntity.ok(feries);
    }
    
    // Vérifier si une date est fériée
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFerie(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isFerie = calendrierFerieService.isFerie(date);
        return ResponseEntity.ok(isFerie);
    }
    
    // Récupérer par année
    @GetMapping("/year/{year}")
    public ResponseEntity<List<CalendrierFerie>> getByYear(@PathVariable int year) {
        List<CalendrierFerie> feries = calendrierFerieService.getFeriesByYear(year);
        return ResponseEntity.ok(feries);
    }
    
    // Récupérer les jours fériés à venir
    @GetMapping("/upcoming")
    public ResponseEntity<List<CalendrierFerie>> getUpcomingFeries() {
        List<CalendrierFerie> feries = calendrierFerieService.getUpcomingFeries();
        return ResponseEntity.ok(feries);
    }
    
    // Recherche par libellé
    @GetMapping("/search")
    public ResponseEntity<List<CalendrierFerie>> searchByLibelle(@RequestParam String keyword) {
        List<CalendrierFerie> feries = calendrierFerieService.searchByLibelle(keyword);
        return ResponseEntity.ok(feries);
    }
}
