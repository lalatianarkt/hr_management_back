package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.CategorieProfessionnelle;
import com.rh.manage.Service.CategorieProfessionnelleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories-professionnelles")
// @CrossOrigin(origins = "*")

public class CategorieProfessionnelleController {
    
    @Autowired
    private CategorieProfessionnelleService service;
    
    // Créer une catégorie
    @PostMapping
    public ResponseEntity<?> create(CategorieProfessionnelle categorie) {
        // Vérifier si le code existe déjà
        if (service.codeExists(categorie.getCode())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Le code existe déjà");
            return ResponseEntity.badRequest().body(response);
        }
        
        CategorieProfessionnelle savedCategorie = service.create(categorie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategorie);
    }
    
    // Mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, 
                                   CategorieProfessionnelle categorieDetails) {
        try {
            CategorieProfessionnelle updatedCategorie = service.update(id, categorieDetails);
            return ResponseEntity.ok(updatedCategorie);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            service.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégorie supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<CategorieProfessionnelle> categories = service.findAllOrderByCreatedAtDesc();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<CategorieProfessionnelle> categorie = service.findById(id);
        
        if (categorie.isPresent()) {
            return ResponseEntity.ok(categorie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Catégorie non trouvée"));
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getByCode(@PathVariable String code) {
        Optional<CategorieProfessionnelle> categorie = service.findByCode(code);
        
        if (categorie.isPresent()) {
            return ResponseEntity.ok(categorie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Catégorie non trouvée avec le code: " + code));
        }
    }
    
    // Rechercher des catégories
    @GetMapping("/search")
    public ResponseEntity<List<CategorieProfessionnelle>> search(@RequestParam String keyword) {
        List<CategorieProfessionnelle> categories = service.search(keyword);
        return ResponseEntity.ok(categories);
    }
    
    // Vérifier si un code existe
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Map<String, Boolean>> checkCodeExists(@PathVariable String code) {
        boolean exists = service.codeExists(code);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
