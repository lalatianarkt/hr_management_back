package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.BaseIrsa;
import com.rh.manage.Service.BaseIrsaService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/base-irsa")
public class BaseIrsaController {
    
    @Autowired
    private BaseIrsaService baseIrsaService;
    
    // CRUD Endpoints
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BaseIrsa baseIrsa) {
        try {
            BaseIrsa created = baseIrsaService.create(baseIrsa);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<BaseIrsa> tranches = baseIrsaService.findAll();
            return ResponseEntity.ok(tranches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Optional<BaseIrsa> baseIrsaOptional = baseIrsaService.findById(id);
            
            if (baseIrsaOptional.isPresent()) {
                return ResponseEntity.ok(baseIrsaOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tranche non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    @GetMapping("/tranche/{numTranche}")
    public ResponseEntity<?> getByNumTranche(@PathVariable Integer numTranche) {
        try {
            Optional<BaseIrsa> baseIrsaOptional = baseIrsaService.findByNumTranche(numTranche);
            
            if (baseIrsaOptional.isPresent()) {
                return ResponseEntity.ok(baseIrsaOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tranche non trouvée avec le numéro: " + numTranche);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody BaseIrsa baseIrsa) {
        try {
            BaseIrsa updated = baseIrsaService.update(id, baseIrsa);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            baseIrsaService.delete(id);
            return ResponseEntity.ok("Tranche supprimée avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/tranche/{numTranche}")
    public ResponseEntity<?> deleteByNumTranche(@PathVariable Integer numTranche) {
        try {
            baseIrsaService.deleteByNumTranche(numTranche);
            return ResponseEntity.ok("Tranche supprimée avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    // Business Logic Endpoints
    
    @GetMapping("/calculer")
    public ResponseEntity<?> calculerIRSA(@RequestParam BigDecimal revenu) {
        try {
            if (revenu == null || revenu.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Le revenu doit être un nombre positif");
            }
            
            BigDecimal irsa = baseIrsaService.calculerIRSA(revenu);
            return ResponseEntity.ok(irsa);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du calcul: " + e.getMessage());
        }
    }
    
    @GetMapping("/calculer-details")
    public ResponseEntity<?> calculerIRSAWithDetails(@RequestParam BigDecimal revenu) {
        try {
            if (revenu == null || revenu.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Le revenu doit être un nombre positif");
            }
            
            Map<String, Object> details = baseIrsaService.getRecapCalculIRSA(revenu);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du calcul détaillé: " + e.getMessage());
        }
    }
    
    @GetMapping("/tranche-pour-montant")
    public ResponseEntity<?> getTrancheForMontant(@RequestParam BigDecimal montant) {
        try {
            if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body("Le montant doit être positif");
            }
            
            Optional<BaseIrsa> tranche = baseIrsaService.findTrancheForMontant(montant);
            
            if (tranche.isPresent()) {
                return ResponseEntity.ok(tranche.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucune tranche trouvée pour le montant: " + montant);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la recherche: " + e.getMessage());
        }
    }
    
    @GetMapping("/validation")
    public ResponseEntity<?> validateTranches() {
        try {
            boolean isValid = baseIrsaService.validateTranches();
            if (isValid) {
                return ResponseEntity.ok("Les tranches IRSA sont valides");
            } else {
                return ResponseEntity.badRequest().body("Les tranches IRSA ne sont pas valides");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la validation: " + e.getMessage());
        }
    }
    
    @PostMapping("/reordonner")
    public ResponseEntity<?> reorderTranches() {
        try {
            baseIrsaService.reorderTranches();
            return ResponseEntity.ok("Tranches réordonnées avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du réordonnancement: " + e.getMessage());
        }
    }
    
    // Endpoint pour initialiser les tranches par défaut (barème Madagascar)
    @PostMapping("/initialiser-defaut")
    public ResponseEntity<?> initialiserTranchesParDefaut() {
        try {
            // Supprimer toutes les tranches existantes
            List<BaseIrsa> existing = baseIrsaService.findAll();
            for (BaseIrsa tranche : existing) {
                baseIrsaService.delete(tranche.getId());
            }
            
            // Créer les tranches par défaut (barème Madagascar 2024)
            BaseIrsa tranche1 = new BaseIrsa(
                BigDecimal.ZERO,
                new BigDecimal("350000"),
                new BigDecimal("5"),
                1
            );
            
            BaseIrsa tranche2 = new BaseIrsa(
                new BigDecimal("350000"),
                new BigDecimal("750000"),
                new BigDecimal("10"),
                2
            );
            
            BaseIrsa tranche3 = new BaseIrsa(
                new BigDecimal("750000"),
                new BigDecimal("1250000"),
                new BigDecimal("15"),
                3
            );
            
            BaseIrsa tranche4 = new BaseIrsa(
                new BigDecimal("1250000"),
                null, // Dernière tranche : pas de maximum
                new BigDecimal("20"),
                4
            );
            
            baseIrsaService.create(tranche1);
            baseIrsaService.create(tranche2);
            baseIrsaService.create(tranche3);
            baseIrsaService.create(tranche4);
            
            return ResponseEntity.ok("Tranches IRSA initialisées avec les valeurs par défaut");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    // Endpoint pour tester le calcul avec plusieurs valeurs
    @GetMapping("/test-calcul")
    public ResponseEntity<?> testCalcul() {
        try {
            List<BigDecimal> testValues = List.of(
                new BigDecimal("100000"),
                new BigDecimal("350000"),
                new BigDecimal("500000"),
                new BigDecimal("1000000"),
                new BigDecimal("2000000"),
                new BigDecimal("5000000")
            );
            
            List<Map<String, Object>> results = new ArrayList<>();
            
            for (BigDecimal valeur : testValues) {
                Map<String, Object> result = new HashMap<>();
                result.put("revenu", valeur);
                result.put("irsa", baseIrsaService.calculerIRSA(valeur));
                results.add(result);
            }
            
            return ResponseEntity.ok(results);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du test: " + e.getMessage());
        }
    }
}
