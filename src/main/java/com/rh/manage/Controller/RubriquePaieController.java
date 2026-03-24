package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.RubriquePaie;
import com.rh.manage.Service.RubriquePaieService;
import com.rh.manage.Service.RubriquePaieService.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/rubriques-paie")
// @CrossOrigin(origins = "*")

public class RubriquePaieController {
    
    @Autowired
    private RubriquePaieService rubriquePaieService;

    // GET: Toutes les rubriques
    @GetMapping
    public ResponseEntity<List<RubriquePaie>> getAllRubriques(
            @RequestParam(required = false) Boolean actif) {
        
        // if (actif != null && actif) {
        //     return ResponseEntity.ok(rubriquePaieService.getAllRubriqueParOrdre());
        // }
        return ResponseEntity.ok(rubriquePaieService.getAllRubriqueParOrdre());
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String typeId,
            @RequestParam(required = false) String estImposable,
            @RequestParam(required = false) String estSoumisCotisations,
            @RequestParam(required = false) String estActif,
            @RequestParam(required = false) String estDeductibleIrsa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ordre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Page<RubriquePaie> result = rubriquePaieService.searchForFrontend(
            search, typeId, estImposable, estSoumisCotisations, estDeductibleIrsa, estActif,
            page, size, sortBy, direction
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("currentPage", result.getNumber());
        response.put("totalPages", result.getTotalPages());
        response.put("totalElements", result.getTotalElements());
        response.put("pageSize", result.getSize());
        response.put("sort", sortBy + "," + direction);
        
        return ResponseEntity.ok(response);
    }

    // NOUVEAU : Endpoint avec pagination
    @GetMapping("/paginated")
    public ResponseEntity<?> getRubriquesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<RubriquePaie> rubriquesPage = rubriquePaieService.getAllRubriquesPaginated(page, size);
        
        // Créer une réponse structurée
        Map<String, Object> response = new HashMap<>();
        response.put("rubriques", rubriquesPage.getContent());
        response.put("currentPage", rubriquesPage.getNumber());
        response.put("totalItems", rubriquesPage.getTotalElements());
        response.put("totalPages", rubriquesPage.getTotalPages());
        response.put("pageSize", size);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRubriqueById(@PathVariable String id) {
        try {
            Optional<RubriquePaie> rubrique = rubriquePaieService.getById(id);
            return ResponseEntity.ok(rubrique);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<RubriquePaie> getRubriqueByCode(@PathVariable String code) {
        Optional<RubriquePaie> rubrique = rubriquePaieService.getByCode(code);
        return rubrique.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET: Recherche par mot-clé
    // @GetMapping("/search")
    // public ResponseEntity<List<RubriquePaie>> searchRubriques(
    //         @RequestParam String keyword) {
    //     return ResponseEntity.ok(rubriquePaieService.search(keyword));
    // }
    
    // GET: Rubriques par catégorie
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<RubriquePaie>> getByCategorie(@PathVariable String categorieId) {
        return ResponseEntity.ok(rubriquePaieService.getByCategorie(categorieId));
    }
    
    // GET: Rubriques par type
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<RubriquePaie>> getByType(@PathVariable String typeId) {
        return ResponseEntity.ok(rubriquePaieService.getByType(typeId));
    }
    
    // GET: Rubriques par statut actif
    @GetMapping("/statut/{estActif}")
    public ResponseEntity<List<RubriquePaie>> getByStatut(@PathVariable Boolean estActif) {
        return ResponseEntity.ok(rubriquePaieService.getByActifStatus(estActif));
    }

    @GetMapping("/actif")
    public ResponseEntity<List<RubriquePaie>> getAllRubriqueActif() {
        return ResponseEntity.ok(rubriquePaieService.getAllRubriqueActifs());
    }
    
    
    // GET: Statistiques par type
    @GetMapping("/stats/type")
    public ResponseEntity<Map<String, Long>> getStatsByType() {
        List<Object[]> stats = rubriquePaieService.getStatsByType();
        Map<String, Long> response = new HashMap<>();
        for (Object[] stat : stats) {
            response.put((String) stat[0], (Long) stat[1]);
        }
        return ResponseEntity.ok(response);
    }
    
    // GET: Statistiques par catégorie
    @GetMapping("/stats/categorie")
    public ResponseEntity<Map<String, Long>> getStatsByCategorie() {
        List<Object[]> stats = rubriquePaieService.getStatsByCategorie();
        Map<String, Long> response = new HashMap<>();
        for (Object[] stat : stats) {
            response.put((String) stat[0], (Long) stat[1]);
        }
        return ResponseEntity.ok(response);
    }
    
    // POST: Créer une nouvelle rubrique
    @PostMapping
    public ResponseEntity<?> createRubrique(@RequestBody RubriquePaie rubrique) {
        try {
            RubriquePaie created = rubriquePaieService.create(rubrique);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
            
        } catch (ValidationException e) {
            // Pour ValidationException (formule invalide, abréviations non reconnues, etc.)
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            error.put("type", "VALIDATION_ERROR");
            return ResponseEntity.badRequest().body(error);
            
        } catch (RuntimeException e) {
            // Pour RuntimeException (code dupliqué, ID existant, etc.)
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            error.put("type", "BUSINESS_ERROR");
            return ResponseEntity.badRequest().body(error);
            
        } catch (Exception e) {
            // Pour toutes les autres exceptions
            Map<String, String> error = new HashMap<>();
            error.put("message", "Une erreur inattendue s'est produite: " + e.getMessage());
            error.put("type", "SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // PUT: Mettre à jour une rubrique
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRubrique(
            @PathVariable String id,
            @RequestBody RubriquePaie rubriqueDetails) {
        try {
            RubriquePaie updated = rubriquePaieService.update(id, rubriqueDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // DELETE: Désactiver une rubrique
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRubrique(@PathVariable String id) {
        rubriquePaieService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rubrique désactivée avec succès");
        return ResponseEntity.ok(response);
    }
    
    // PATCH: Activer une rubrique
    @PutMapping("/{id}/activate")
    public ResponseEntity<RubriquePaie> activateRubrique(@PathVariable String id) {
        RubriquePaie rubrique = rubriquePaieService.activate(id);
        return ResponseEntity.ok(rubrique);
    }
    
    // PATCH: Désactiver une rubrique
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<RubriquePaie> deactivateRubrique(@PathVariable String id) {
        RubriquePaie rubrique = rubriquePaieService.deactivate(id);
        return ResponseEntity.ok(rubrique);
    }
    
    // POST: Réorganiser les ordres
    @PostMapping("/reorder")
    public ResponseEntity<Map<String, String>> reorderRubriques() {
        rubriquePaieService.reorderRubriques();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rubriques réorganisées avec succès");
        return ResponseEntity.ok(response);
    }
    
    // GET: Vérifier l'existence d'un code
    @GetMapping("/exists/{code}")
    public ResponseEntity<Map<String, Boolean>> checkCodeExists(@PathVariable String code) {
        boolean exists = rubriquePaieService.existsByCode(code);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // GET: Prochain ordre disponible
    @GetMapping("/next-ordre")
    public ResponseEntity<Map<String, Integer>> getNextOrdre() {
        Integer nextOrdre = rubriquePaieService.findNextOrdre();
        Map<String, Integer> response = new HashMap<>();
        response.put("nextOrdre", nextOrdre);
        return ResponseEntity.ok(response);
    }
    
    // GET: Nombre total de rubriques
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countRubriques() {
        long count = rubriquePaieService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        response.put("actives", rubriquePaieService.countActive());
        return ResponseEntity.ok(response);
    }
}
