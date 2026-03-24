package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Region;
import com.rh.manage.Service.RegionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
// @CrossOrigin(origins = "*")

public class RegionController {
    
    @Autowired
    private RegionService service;
    
    // Créer une région
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Region region) {
        // Vérifier si le nom existe déjà
        if (service.nomExists(region.getNom())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Une région avec ce nom existe déjà");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        Region savedRegion = service.create(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRegion);
    }
    
    // Mettre à jour une région
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, 
                                   @RequestBody Region regionDetails) {
        try {
            // Vérifier si le nom existe pour une autre région
            if (service.nomExistsForOtherRegion(regionDetails.getNom(), id)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Une autre région avec ce nom existe déjà");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            Region updatedRegion = service.update(id, regionDetails);
            return ResponseEntity.ok(updatedRegion);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Supprimer une région
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            service.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Région supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Récupérer toutes les régions (triées par nom)
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Region> regions = service.findAll();
            return ResponseEntity.ok(regions); 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer une région par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Region> region = service.findById(id);
        
        if (region.isPresent()) {
            return ResponseEntity.ok(region.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Région non trouvée"));
        }
    }

    // Récupérer une région par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<?> getByNom(@PathVariable String nom) {
        Optional<Region> region = service.findByNom(nom);
        
        if (region.isPresent()) {
            return ResponseEntity.ok(region.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Région non trouvée avec le nom: " + nom));
        }
    }
    
    // Rechercher des régions
    @GetMapping("/search")
    public ResponseEntity<List<Region>> search(@RequestParam String keyword) {
        List<Region> regions = service.search(keyword);
        return ResponseEntity.ok(regions);
    }
    
    // Vérifier si un nom existe
    @GetMapping("/exists/nom/{nom}")
    public ResponseEntity<Map<String, Boolean>> checkNomExists(@PathVariable String nom) {
        boolean exists = service.nomExists(nom);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // Compter le nombre de régions
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        long count = service.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
