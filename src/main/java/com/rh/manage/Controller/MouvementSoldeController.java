package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.TypeEnumConge;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.MouvementService;
import com.rh.manage.Service.MouvementSoldeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/mouvementSolde")
public class MouvementSoldeController {


    private final MouvementSoldeService service;

    @Autowired
    EmployeService employeService;

    public MouvementSoldeController(MouvementSoldeService service) {
        this.service = service;
    }

    @GetMapping
    public List<MouvementSolde> getAll() {
        return service.getAllMouvements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouvementSolde> getById(@PathVariable int id) {
        return service.getMouvementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employe/{idEmploye}/mouvementSolde")
    public ResponseEntity<?> getTroisDerniersMouvements(@PathVariable String idEmploye) {
        try {
            System.out.println("🔍 GET /employe/" + idEmploye + "/mouvementSolde - 3 derniers seulement");
            
            // Validation de l'ID
            if (idEmploye == null || idEmploye.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "ID employé invalide",
                    "message", "L'ID de l'employé est requis"
                ));
            }
            
            idEmploye = idEmploye.trim();
            
            // Récupération des 3 derniers mouvements
            Optional<List<MouvementSolde>> result = service.getMouvementSoldeParEmploye(idEmploye);
            
            if (result.isPresent()) {
                List<MouvementSolde> tousMouvements = result.get();
                
                // Prendre seulement les 3 premiers (déjà triés par date décroissante)
                List<MouvementSolde> troisDerniers = tousMouvements.stream()
                    .limit(3)
                    .collect(Collectors.toList());
                
                System.out.println("✅ " + troisDerniers.size() + " mouvements (sur " + tousMouvements.size() + ") pour employé " + idEmploye);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "3 derniers mouvements récupérés avec succès",
                    "employeId", idEmploye,
                    "totalMouvements", tousMouvements.size(),
                    "count", troisDerniers.size(),
                    "data", troisDerniers,
                    "timestamp", LocalDateTime.now()
                ));
                
            } else {
                System.out.println("⚠️ Aucun mouvement trouvé pour employé " + idEmploye);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Aucun mouvement trouvé pour cet employé",
                    "employeId", idEmploye,
                    "count", 0,
                    "data", new ArrayList<>(),
                    "timestamp", LocalDateTime.now()
                ));
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Erreur serveur",
                "message", "Une erreur est survenue",
                "details", e.getMessage()
            ));
        }
    }

        @GetMapping("/employe/{idEmploye}/derniersMouvements")
    public ResponseEntity<?> getMouvementParEmploye(@PathVariable String idEmploye){
        Employe employe = employeService.getEmployeActifById(idEmploye).get();
        return ResponseEntity.ok(service.getMouvementsPrisByEmploye(employe));
    }

    @GetMapping("/employe/{idEmploye}/solde-actuel")
    public ResponseEntity<?> getByEmploye(@PathVariable String idEmploye) {
        System.out.println("🔍 GET /employe/" + idEmploye + "/solde-actuel");
        try {
            Employe employe = employeService.getById(idEmploye).get();
            return ResponseEntity.ok(service.getDernierMouvementParEmploye(employe).get());
        } catch (Exception e) {
            System.out.println("ity no tena olana anie : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Erreur serveur",
                "message", "Une erreur est survenue",
                "details", e.getMessage()
            ));
        }
    }   

    @GetMapping("/type/{typeMouvement}")
    public List<MouvementSolde> getByType(@PathVariable TypeEnumConge typeMouvement) {
        return service.getMouvementsByType(typeMouvement);
    }

    @PostMapping
    public MouvementSolde create(@RequestBody MouvementSolde mouvement) {
        return service.saveMouvement(mouvement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MouvementSolde> update(@PathVariable int id, @RequestBody MouvementSolde mouvement) {
        return service.getMouvementById(id).map(existing -> {
            mouvement.setId(id);
            return ResponseEntity.ok(service.saveMouvement(mouvement));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteMouvement(id);
        return ResponseEntity.noContent().build();
    }
}


