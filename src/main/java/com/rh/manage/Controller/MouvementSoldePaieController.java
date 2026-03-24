package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.MouvementSoldePaie;
import com.rh.manage.Service.MouvementSoldePaieService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mouvements-solde-paie")
public class MouvementSoldePaieController {

    @Autowired
    private MouvementSoldePaieService service;

    /**
     * Créer un nouveau mouvement de solde
     */
    @PostMapping
    public ResponseEntity<MouvementSoldePaie> createMouvement(@RequestBody MouvementSoldePaie mouvement) {
        MouvementSoldePaie created = service.createMouvement(mouvement);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les mouvements avec pagination
     */
    @GetMapping
    public ResponseEntity<Page<MouvementSoldePaie>> getAllMouvements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Page<MouvementSoldePaie> mouvements = service.getAllMouvements(page, size, sortBy, direction);
        return ResponseEntity.ok(mouvements);
    }

    /**
     * Récupérer tous les mouvements (sans pagination)
     */
    @GetMapping("/all")
    public ResponseEntity<List<MouvementSoldePaie>> getAllMouvementsList() {
        List<MouvementSoldePaie> mouvements = service.getAllMouvements();
        return ResponseEntity.ok(mouvements);
    }

    /**
     * Récupérer un mouvement par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MouvementSoldePaie> getMouvementById(@PathVariable int id) {
        MouvementSoldePaie mouvement = service.getMouvementById(id);
        return ResponseEntity.ok(mouvement);
    }

    /**
     * Récupérer un mouvement par son ID unique
     */
    @GetMapping("/reference/{idMouvementSolde}")
    public ResponseEntity<MouvementSoldePaie> getMouvementByIdMouvementSolde(
            @PathVariable int idMouvementSolde) {
        MouvementSoldePaie mouvement = service.getMouvementByIdMouvementSolde(idMouvementSolde);
        return ResponseEntity.ok(mouvement);
    }

    /**
     * Récupérer tous les mouvements pour une paie
     */
    @GetMapping("/paie/{idPaie}")
    public ResponseEntity<List<MouvementSoldePaie>> getMouvementsByPaie(@PathVariable String idPaie) {
        List<MouvementSoldePaie> mouvements = service.getMouvementsByPaie(idPaie);
        return ResponseEntity.ok(mouvements);
    }

    /**
     * Récupérer les mouvements dans une période
     */
    @GetMapping("/periode")
    public ResponseEntity<List<MouvementSoldePaie>> getMouvementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MouvementSoldePaie> mouvements = service.getMouvementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(mouvements);
    }

    /**
     * Mettre à jour un mouvement
     */
    @PutMapping("/{id}")
    public ResponseEntity<MouvementSoldePaie> updateMouvement(
            @PathVariable int id,
            @RequestBody MouvementSoldePaie mouvement) {
        MouvementSoldePaie updated = service.updateMouvement(id, mouvement);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer un mouvement
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable int id) {
        service.deleteMouvement(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer tous les mouvements pour une paie
     */
    @DeleteMapping("/paie/{idPaie}")
    public ResponseEntity<Void> deleteMouvementsByPaie(@PathVariable String idPaie) {
        service.deleteMouvementsByPaie(idPaie);
        return ResponseEntity.noContent().build();
    }

    /**
     * Compter le nombre de mouvements pour une paie
     */
    @GetMapping("/paie/{idPaie}/count")
    public ResponseEntity<Long> countByPaie(@PathVariable String idPaie) {
        long count = service.countByPaie(idPaie);
        return ResponseEntity.ok(count);
    }

    /**
     * Récupérer le dernier mouvement pour une paie
     */
    @GetMapping("/paie/{idPaie}/last")
    public ResponseEntity<MouvementSoldePaie> getLastMouvementByPaie(@PathVariable String idPaie) {
        MouvementSoldePaie mouvement = service.getLastMouvementByPaie(idPaie);
        if (mouvement != null) {
            return ResponseEntity.ok(mouvement);
        }
        return ResponseEntity.noContent().build();
    }
}
