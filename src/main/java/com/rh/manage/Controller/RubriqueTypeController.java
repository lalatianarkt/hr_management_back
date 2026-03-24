package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.RubriqueType;
import com.rh.manage.Service.RubriqueTypeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rubrique-types")
// @CrossOrigin(origins = "*")

public class RubriqueTypeController {
    
    @Autowired
    private RubriqueTypeService rubriqueTypeService;
    
    // GET: Tous les types
    @GetMapping
    public ResponseEntity<List<RubriqueType>> getAllRubriqueTypes() {
        return ResponseEntity.ok(rubriqueTypeService.getAll());
    }
    
    // GET: Type par ID
    @GetMapping("/{id}")
    public ResponseEntity<RubriqueType> getRubriqueTypeById(@PathVariable String id) {
        Optional<RubriqueType> rubriqueType = rubriqueTypeService.getById(id);
        return rubriqueType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET: Type par libellé
    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<RubriqueType> getRubriqueTypeByLibelle(@PathVariable String libelle) {
        Optional<RubriqueType> rubriqueType = rubriqueTypeService.getByLibelle(libelle);
        return rubriqueType.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET: Recherche par mot-clé
    @GetMapping("/search")
    public ResponseEntity<List<RubriqueType>> searchRubriqueTypes(
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(rubriqueTypeService.search(keyword));
    }
    
    // GET: Types utilisés
    @GetMapping("/used")
    public ResponseEntity<List<RubriqueType>> getUsedTypes() {
        return ResponseEntity.ok(rubriqueTypeService.getUsedTypes());
    }
    
    // POST: Créer un nouveau type
    @PostMapping
    public ResponseEntity<?> createRubriqueType(@RequestBody RubriqueType rubriqueType) {
        try {
            RubriqueType created = rubriqueTypeService.create(rubriqueType);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de création");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // PUT: Mettre à jour un type
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRubriqueType(
            @PathVariable String id,
            @RequestBody RubriqueType rubriqueTypeDetails) {
        try {
            RubriqueType updated = rubriqueTypeService.update(id, rubriqueTypeDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de mise à jour");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // DELETE: Supprimer un type
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRubriqueType(@PathVariable String id) {
        try {
            rubriqueTypeService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Type de rubrique supprimé avec succès");
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
    public ResponseEntity<Map<String, Boolean>> existsRubriqueType(@PathVariable String id) {
        boolean exists = rubriqueTypeService.exists(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // GET: Vérifier l'existence par libellé
    @GetMapping("/exists/libelle/{libelle}")
    public ResponseEntity<Map<String, Boolean>> existsByLibelle(@PathVariable String libelle) {
        boolean exists = rubriqueTypeService.existsByLibelle(libelle);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // GET: Nombre de types
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countRubriqueTypes() {
        long count = rubriqueTypeService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // POST: Initialiser les données par défaut (Admin IT seulement)
    @PostMapping("/initialize-defaults")
    public ResponseEntity<Map<String, String>> initializeDefaults() {
        try {
            rubriqueTypeService.initializeDefaultData();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Types de rubriques initialisés avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur d'initialisation");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}