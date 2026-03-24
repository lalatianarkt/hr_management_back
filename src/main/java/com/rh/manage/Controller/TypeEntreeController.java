package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeEntree;
import com.rh.manage.Service.TypeEntreeService;

import java.util.List;

@RestController
@RequestMapping("/api/types-entree")
// @CrossOrigin(origins = "*")

public class TypeEntreeController {
    @Autowired
    private TypeEntreeService typeEntreeService;
    
    @PostMapping
    public ResponseEntity<TypeEntree> createTypeEntree(@RequestBody TypeEntree typeEntree) {
        TypeEntree createdTypeEntree = typeEntreeService.createTypeEntree(typeEntree);
        return new ResponseEntity<>(createdTypeEntree, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TypeEntree> updateTypeEntree(@PathVariable String id, 
                                                      @RequestBody TypeEntree typeEntree) {
        TypeEntree updatedTypeEntree = typeEntreeService.updateTypeEntree(id, typeEntree);
        return ResponseEntity.ok(updatedTypeEntree);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TypeEntree> getTypeEntreeById(@PathVariable String id) {
        TypeEntree typeEntree = typeEntreeService.getTypeEntreeById(id);
        return ResponseEntity.ok(typeEntree);
    }
    
    @GetMapping
    public ResponseEntity<?> getAllTypeEntrees() {
        try {
            List<TypeEntree> typeEntrees = typeEntreeService.getAllTypeEntrees();
            return ResponseEntity.ok(typeEntrees);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEntree(@PathVariable String id) {
        typeEntreeService.deleteTypeEntree(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/libelle/{libelle}")
    public ResponseEntity<TypeEntree> getTypeEntreeByLibelle(@PathVariable String libelle) {
        TypeEntree typeEntree = typeEntreeService.findByLibelle(libelle);
        if (typeEntree == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(typeEntree);
    }
    
    @GetMapping("/exists/libelle/{libelle}")
    public ResponseEntity<Boolean> checkLibelleExists(@PathVariable String libelle) {
        boolean exists = typeEntreeService.existsByLibelle(libelle);
        return ResponseEntity.ok(exists);
    }
}
