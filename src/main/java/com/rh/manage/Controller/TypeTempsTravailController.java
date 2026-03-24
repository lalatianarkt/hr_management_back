package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeTempsTravail;
import com.rh.manage.Service.TypeTempsTravailService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/types-temps-travail")
// @CrossOrigin(origins = "*")

public class TypeTempsTravailController {
    
    @Autowired
    private TypeTempsTravailService service;
    
    // Créer un type de temps de travail
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TypeTempsTravail typeTempsTravail) {
        // Vérifier si le temps de travail existe déjà
        if (service.tempsTravailExists(typeTempsTravail.getTempsTravail())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Un type de temps de travail avec cette valeur existe déjà");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        TypeTempsTravail savedType = service.create(typeTempsTravail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedType);
    }
    
    // Mettre à jour un type de temps de travail
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, 
                                   @RequestBody TypeTempsTravail typeTempsTravailDetails) {
        try {
            // Vérifier si le temps de travail existe pour un autre type
            if (service.tempsTravailExistsForOtherType(typeTempsTravailDetails.getTempsTravail(), id)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Un autre type avec cette valeur existe déjà");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            TypeTempsTravail updatedType = service.update(id, typeTempsTravailDetails);
            return ResponseEntity.ok(updatedType);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Supprimer un type de temps de travail
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Type de temps de travail supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // Récupérer tous les types de temps de travail (triés)
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TypeTempsTravail> types = service.findAll();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Récupérer un type par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<TypeTempsTravail> typeOptional = service.findById(id);
        
        if (typeOptional.isPresent()) {
            return ResponseEntity.ok(typeOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Type de temps de travail non trouvé"));
        }
    }

    // Récupérer un type par temps de travail
    @GetMapping("/temps/{tempsTravail}")
    public ResponseEntity<?> getByTempsTravail(@PathVariable String tempsTravail) {
        Optional<TypeTempsTravail> typeOptional = service.findByTempsTravail(tempsTravail);
        
        if (typeOptional.isPresent()) {
            return ResponseEntity.ok(typeOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Type de temps de travail non trouvé avec la valeur: " + tempsTravail));
        }
    }
    
    // Rechercher des types
    @GetMapping("/search")
    public ResponseEntity<List<TypeTempsTravail>> search(@RequestParam String keyword) {
        List<TypeTempsTravail> types = service.search(keyword);
        return ResponseEntity.ok(types);
    }
    
    // Vérifier si un temps de travail existe
    @GetMapping("/exists/{tempsTravail}")
    public ResponseEntity<Map<String, Boolean>> checkTempsTravailExists(@PathVariable String tempsTravail) {
        boolean exists = service.tempsTravailExists(tempsTravail);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    // Compter le nombre de types
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        long count = service.count();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
