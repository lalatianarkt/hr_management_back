package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.InformationSociete;
import com.rh.manage.Service.InformationSocieteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/informations-societe")
public class InformationSocieteController {
    @Autowired
    InformationSocieteService informationSocieteService;
    
    @PostMapping
    public ResponseEntity<?> createInformationSociete(@RequestBody InformationSociete informationSociete) {
        try {
            InformationSociete created = informationSocieteService.createInformationSociete(informationSociete);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la création de l'information société");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInformationSociete(@PathVariable Integer id, 
                                                     @RequestBody InformationSociete informationSociete) {
        try {
            InformationSociete updated = informationSocieteService.updateInformationSociete(id, informationSociete);
            return ResponseEntity.ok(updated);
        }catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la mise à jour de l'information société");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInformationSociete(@PathVariable Integer id) {
        try {
            informationSocieteService.deleteInformationSociete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la suppression de l'information société");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getInformationSocieteById(@PathVariable Integer id) {
        try {
            InformationSociete informationSociete = informationSocieteService.getInformationSocieteById(id);
            return ResponseEntity.ok(informationSociete);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/nom/{nomCompany}")
    public ResponseEntity<?> getInformationSocieteByNom(@PathVariable String nomCompany) {
        try {
            InformationSociete informationSociete = informationSocieteService.getInformationSocieteByNom(nomCompany);
            return ResponseEntity.ok(informationSociete);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<InformationSociete>> getAllInformationSocietes() {
        List<InformationSociete> informations = informationSocieteService.getAllInformationSocietes();
        return ResponseEntity.ok(informations);
    }
    
    @GetMapping("/premiere")
    public ResponseEntity<?> getFirstSociete() {
        try {
            InformationSociete societe = informationSocieteService.getFirstSociete();
            return ResponseEntity.ok(societe);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/recherche/{nomCompany}")
    public ResponseEntity<?> searchByNom(@PathVariable String nomCompany) {
        try {
            InformationSociete societe = informationSocieteService.searchByNom(nomCompany);
            return ResponseEntity.ok(societe);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
