package com.rh.manage.Controller;

import com.rh.manage.Model.Categorie;
import com.rh.manage.Service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
// @CrossOrigin(origins = "*")

public class CategorieController {
    
    @Autowired
    private CategorieService categorieService;
    
    // === CREATE ===
    @PostMapping
    public ResponseEntity<?> createCategorie(@RequestBody Categorie categorie) {
        try {
            Categorie savedCategorie = categorieService.create(categorie);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategorie);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Erreur de validation", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de créer la catégorie"));
        }
    }
    
    // === READ ALL ===
    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.getAll();
        return ResponseEntity.ok(categories);
    }
    
    // === READ BY ID ===
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategorieById(@PathVariable String id) {
        Optional<Categorie> categorie = categorieService.getById(id);
        if (categorie.isPresent()) {
            return ResponseEntity.ok(categorie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Catégorie non trouvée avec l'ID: " + id));
        }
    }
    
    // === READ BY NOM ===
    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getCategorieByNom(@PathVariable String nom) {
        Optional<Categorie> categorie = categorieService.getByNom(nom);
        if (categorie.isPresent()) {
            return ResponseEntity.ok(categorie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", "Catégorie non trouvée avec le nom: " + nom));
        }
    }
    
    // === UPDATE ===
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategorie(@PathVariable String id, @RequestBody Categorie categorieDetails) {
        try {
            Categorie updatedCategorie = categorieService.update(id, categorieDetails);
            return ResponseEntity.ok(updatedCategorie);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvée")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Non trouvé", e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Erreur de validation", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de mettre à jour la catégorie"));
        }
    }
    
    // === DELETE ===
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategorie(@PathVariable String id) {
        try {
            categorieService.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégorie supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Non trouvé", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur interne", "Impossible de supprimer la catégorie"));
        }
    }
    
    // === RECHERCHE ===
    @GetMapping("/search")
    public ResponseEntity<List<Categorie>> searchCategories(@RequestParam String q) {
        List<Categorie> categories = categorieService.search(q);
        return ResponseEntity.ok(categories);
    }
    
    // === VÉRIFICATION EXISTENCE ===
    @GetMapping("/exists/{nom}")
    public ResponseEntity<Map<String, Boolean>> checkIfExists(@PathVariable String nom) {
        boolean exists = categorieService.existsByNom(nom);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // === COMPTAGE ===
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countCategories() {
        long count = categorieService.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // === INITIALISATION DÉFAUT ===
    @PostMapping("/initialize-default")
    public ResponseEntity<?> initializeDefaultCategories() {
        try {
            categorieService.initializeDefaultCategories();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Catégories par défaut initialisées avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erreur", "Impossible d'initialiser les catégories par défaut"));
        }
    }
    
    // Méthode utilitaire pour les réponses d'erreur
    private Map<String, String> createErrorResponse(String type, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", type);
        errorResponse.put("message", message);
        return errorResponse;
    }
}