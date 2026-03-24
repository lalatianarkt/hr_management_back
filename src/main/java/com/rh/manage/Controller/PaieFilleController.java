package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.PaieFille;
import com.rh.manage.Service.PaieFilleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/paie-fille")
public class PaieFilleController {
    @Autowired
    private PaieFilleService paieFilleService;
    
    @GetMapping
    public ResponseEntity<List<PaieFille>> getAll() {
        List<PaieFille> paieFilles = paieFilleService.getAll();
        return ResponseEntity.ok(paieFilles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaieFille> getById(@PathVariable Long id) {
        return paieFilleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/paie/{paieId}")
    public ResponseEntity<List<PaieFille>> getByPaieId(@PathVariable String paieId) {
        List<PaieFille> paieFilles = paieFilleService.getByPaieId(paieId);
        return ResponseEntity.ok(paieFilles);
    }
    
    @GetMapping("/paie/{paieId}/with-rubrique")
    public ResponseEntity<List<PaieFille>> getByPaieIdWithRubrique(@PathVariable String paieId) {
        List<PaieFille> paieFilles = paieFilleService.getByPaieIdWithRubrique(paieId);
        return ResponseEntity.ok(paieFilles);
    }
    
    @GetMapping("/rubrique/{rubriqueId}")
    public ResponseEntity<List<PaieFille>> getByRubriqueId(@PathVariable String rubriqueId) {
        List<PaieFille> paieFilles = paieFilleService.getByRubriqueId(rubriqueId);
        return ResponseEntity.ok(paieFilles);
    }

    @GetMapping("/paie/{paieId}/rub/{rubriqueId}")
    public ResponseEntity<?> test(@PathVariable String paieId, @PathVariable String rubriqueId) {
        try {
             return ResponseEntity.ok("lala : " + paieId + "" + rubriqueId);
        } catch (Exception e) {
             return ResponseEntity.badRequest().build();
        }
       
    }
    
    
    @GetMapping("/paie/{paieId}/rubrique/{rubriqueId}")
    public ResponseEntity<PaieFille> getByPaieAndRubrique(
            @PathVariable String paieId, 
            @PathVariable String rubriqueId) {
        
        // log.info("=== Appel GET /paie/{}/rubrique/{} ===", paieId, rubriqueId);
        
        // Validation des paramètres
        if (paieId == null || paieId.trim().isEmpty()) {
            // log.error("Paie ID est null ou vide");
            return ResponseEntity.badRequest().build();
        }
        
        if (rubriqueId == null || rubriqueId.trim().isEmpty()) {
            // log.error("Rubrique ID est null ou vide");
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Optional<PaieFille> paieFille = paieFilleService.getByPaieAndRubrique(paieId, rubriqueId);
            
            if (paieFille.isPresent()) {
                // log.info("PaieFille trouvée: ID={}", paieFille.get().getId());
                return ResponseEntity.ok(paieFille.get());
            } else {
                // log.warn("Aucune PaieFille trouvée pour paieId={}, rubriqueId={}", paieId, rubriqueId);
                return ResponseEntity.ok(null);
                        // ou avec un header personnalisé
            }
            
        } catch (Exception e) {
            // log.error("Erreur lors de la recherche de PaieFille: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping("/paie/{paieId}/total")
    public ResponseEntity<BigDecimal> getTotalMontantByPaie(@PathVariable String paieId) {
        BigDecimal total = paieFilleService.getTotalMontantByPaie(paieId);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/paie/{paieId}/count")
    public ResponseEntity<Long> countByPaie(@PathVariable String paieId) {
        long count = paieFilleService.countByPaie(paieId);
        return ResponseEntity.ok(count);
    }
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PaieFille paieFille) {
        try {
            // Validation des données requises AVANT d'appeler le service
            if (paieFille.getPaie() == null || paieFille.getPaie().getId() == null) {
                return ResponseEntity.badRequest().body("L'ID de la paie est obligatoire");
            }
            
            if (paieFille.getRubrique() == null || paieFille.getRubrique().getId() == null) {
                return ResponseEntity.badRequest().body("L'ID de la rubrique est obligatoire");
            }
            
            try {
                // Appel du service qui peut lever des exceptions métier
                PaieFille createdPaieFille = paieFilleService.create(paieFille);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPaieFille);
                
            } catch (Exception e) {
                // Capture des exceptions métier spécifiques du service
                String errorMessage = e.getMessage();
                
                // Journalisation pour le débogage
                System.err.println("Erreur métier lors de la création de PaieFille: " + errorMessage);
                e.printStackTrace();
                
                // Déterminer le type d'erreur pour un meilleur message au frontend
                if (errorMessage.contains("déja été ajouté")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                } else if (errorMessage.contains("par ordre")) {
                    return ResponseEntity.badRequest().body(errorMessage);
                } else {
                    return ResponseEntity.badRequest().body("Erreur métier: " + errorMessage);
                }
            }
            
        } catch (IllegalArgumentException e) {
            // Erreur de validation Spring/Jackson
            System.err.println("Erreur validation: " + e.getMessage());
            return ResponseEntity.badRequest().body("Données invalides: " + e.getMessage());
            
        } catch (Exception e) {
            // Erreur système inattendue
            System.err.println("Erreur système lors de la création: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur technique est survenue. Veuillez réessayer.");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PaieFille> update(
            @PathVariable Long id, 
            @RequestBody PaieFille paieFille) {
        
        PaieFille updated;
        try {
            updated = paieFilleService.update(id, paieFille);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            paieFilleService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/paie/{paieId}")
    public ResponseEntity<Void> deleteByPaieId(@PathVariable String paieId) {
        paieFilleService.deleteByPaieId(paieId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<PaieFille> createOrUpdate(
            @RequestParam String paieId,
            @RequestParam String rubriqueId,
            @RequestParam(required = false) BigDecimal base,
            @RequestParam(required = false) BigDecimal taux,
            @RequestParam(required = false) BigDecimal nombre) {
        
        // Note: Cette méthode nécessiterait des services supplémentaires pour récupérer Paie et Rubrique
        // Vous devriez adapter cette méthode selon vos besoins réels
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
