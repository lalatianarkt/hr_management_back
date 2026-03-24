package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.CategorieRub;
import com.rh.manage.Service.CategorieRubService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories-rub")
// @CrossOrigin(origins = "*")

public class CategorieRubController {
    
    @Autowired
    private CategorieRubService categorieRubService;
    
    // GET: Toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategorieRub>> getAllCategoriesRub() {
        return ResponseEntity.ok(categorieRubService.getAll());
    }
    
    // GET: Catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<CategorieRub> getCategorieRubById(@PathVariable String id) {
        Optional<CategorieRub> categorieRub = categorieRubService.getById(id);
        return categorieRub.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET: Catégorie par libellé
    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<CategorieRub> getCategorieRubByLibelle(@PathVariable String libelle) {
        Optional<CategorieRub> categorieRub = categorieRubService.getByLibelle(libelle);
        return categorieRub.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET: Recherche par mot-clé
    @GetMapping("/search")
    public ResponseEntity<List<CategorieRub>> searchCategoriesRub(
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(categorieRubService.search(keyword));
    }
    
    // GET: Catégories utilisées
    @GetMapping("/used")
    public ResponseEntity<List<CategorieRub>> getUsedCategories() {
        return ResponseEntity.ok(categorieRubService.getUsedCategories());
    }
    
    // GET: Statistiques d'utilisation
    @GetMapping("/stats/usage")
    public ResponseEntity<Map<String, Long>> getUsageStats() {
        List<Object[]> stats = categorieRubService.getUsageStats();
        Map<String, Long> response = new HashMap<>();
        for (Object[] stat : stats) {
            response.put((String) stat[0], (Long) stat[1]);
        }
        return ResponseEntity.ok(response);
    }
    
    // GET: Catégories avec statistiques détaillées
    @GetMapping("/stats/detailed")
    public ResponseEntity<List<Object[]>> getDetailedStats() {
        return ResponseEntity.ok(categorieRubService.getCategoriesWithUsageStats());
    }
    
    // POST: Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<?> createCategorieRub(@RequestBody CategorieRub categorieRub) {
        try {
            CategorieRub created = categorieRubService.create(categorieRub);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de création");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // PUT: Mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategorieRub(
            @PathVariable String id,
            @RequestBody CategorieRub categorieRubDetails) {
        try {
            CategorieRub updated = categorieRubService.update(id, categorieRubDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de mise à jour");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // DELETE: Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategorieRub(@PathVariable String id) {
        try {
            categorieRubService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégorie supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de suppression");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // GET: Vérifier l'existence
    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> existsCategorieRub(@PathVariable String id) {
        boolean exists = categorieRubService.exists(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // GET: Vérifier l'existence par libellé
    @GetMapping("/exists/libelle/{libelle}")
    public ResponseEntity<Map<String, Boolean>> existsByLibelle(@PathVariable String libelle) {
        boolean exists = categorieRubService.existsByLibelle(libelle);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // GET: Vérifier si une catégorie est utilisée
    @GetMapping("/is-used/{id}")
    public ResponseEntity<Map<String, Boolean>> isCategorieUsed(@PathVariable String id) {
        boolean isUsed = categorieRubService.isCategorieUsed(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUsed", isUsed);
        return ResponseEntity.ok(response);
    }
    
    // GET: Nombre de catégories
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCategoriesRub() {
        long count = categorieRubService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // POST: Initialiser les données par défaut (Admin IT seulement)
    @PostMapping("/initialize-defaults")
    public ResponseEntity<Map<String, String>> initializeDefaults() {
        try {
            categorieRubService.initializeDefaultData();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégories de rubriques initialisées avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur d'initialisation");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
