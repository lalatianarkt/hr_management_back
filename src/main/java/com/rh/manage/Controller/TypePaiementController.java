package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypePaiement;
import com.rh.manage.Service.TypePaiementService;

import java.util.List;

@RestController
@RequestMapping("/api/types-paiement")
public class TypePaiementController {
    @Autowired
    private TypePaiementService typePaiementService;
    
    @PostMapping
    public ResponseEntity<TypePaiement> createTypePaiement(@RequestBody TypePaiement typePaiement) {
        TypePaiement created = typePaiementService.createTypePaiement(typePaiement);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TypePaiement> updateTypePaiement(
            @PathVariable String id, 
            @RequestBody TypePaiement typePaiement) {
        TypePaiement updated = typePaiementService.updateTypePaiement(id, typePaiement);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TypePaiement> getTypePaiementById(@PathVariable String id) {
        TypePaiement typePaiement = typePaiementService.getTypePaiementById(id);
        return ResponseEntity.ok(typePaiement);
    }
    
    @GetMapping
    public ResponseEntity<?> getAllTypePaiements() {
        try {
            List<TypePaiement> typesPaiement = typePaiementService.getAllTypePaiements();
            return ResponseEntity.ok(typesPaiement);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<TypePaiement> getTypePaiementByCode(@PathVariable String code) {
        TypePaiement typePaiement = typePaiementService.getTypePaiementByCode(code);
        return ResponseEntity.ok(typePaiement);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<TypePaiement>> searchTypePaiements(@RequestParam String keyword) {
        List<TypePaiement> typesPaiement = typePaiementService.searchTypePaiements(keyword);
        return ResponseEntity.ok(typesPaiement);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypePaiement(@PathVariable String id) {
        typePaiementService.deleteTypePaiement(id);
        return ResponseEntity.noContent().build();
    }
}
