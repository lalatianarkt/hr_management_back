package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeDemande;
import com.rh.manage.Service.TypeDemandeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/types-demande")
public class TypeDemandeController {
    @Autowired
    private TypeDemandeService typeDemandeService;
    
    @PostMapping
    public ResponseEntity<?> createTypeDemande(@RequestBody TypeDemande typeDemande) {
        try {
            TypeDemande created = typeDemandeService.createTypeDemande(typeDemande);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la création du type de demande");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeDemande(@PathVariable String id, 
                                              @RequestBody TypeDemande typeDemande) {
        try {
            TypeDemande updated = typeDemandeService.updateTypeDemande(id, typeDemande);
            return ResponseEntity.ok(updated);
        }  catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la mise à jour du type de demande");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeDemande(@PathVariable String id) {
        try {
            typeDemandeService.deleteTypeDemande(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la suppression du type de demande");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeDemandeById(@PathVariable String id) {
        try {
            TypeDemande type = typeDemandeService.getTypeDemandeById(id);
            return ResponseEntity.ok(type);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getTypeDemandeByType(@PathVariable String type) {
        try {
            TypeDemande typeDemande = typeDemandeService.getTypeDemandeByType(type);
            return ResponseEntity.ok(typeDemande);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<TypeDemande>> getAllTypeDemandes() {
        List<TypeDemande> types = typeDemandeService.getAllTypeDemandes();
        return ResponseEntity.ok(types);
    }
    
    @GetMapping("/recherche")
    public ResponseEntity<List<TypeDemande>> searchTypeDemandes(@RequestParam String term) {
        List<TypeDemande> types = typeDemandeService.searchByType(term);
        return ResponseEntity.ok(types);
    }
}
