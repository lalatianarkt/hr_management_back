package com.rh.manage.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Formule;
import com.rh.manage.Service.FormuleService;

import java.util.List;

@RestController
@RequestMapping("/api/formules")
public class FormuleController {
    
    private final FormuleService formuleService;

    public FormuleController(FormuleService formuleService) {
        this.formuleService = formuleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Formule createFormule(@RequestBody Formule formule) {
        return formuleService.createFormule(formule);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formule> getFormule(@PathVariable String id) {
        return formuleService.getFormuleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Formule> getAllFormules() {
        return formuleService.getAllFormules();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Formule> updateFormule(
            @PathVariable String id,
            @RequestBody Formule formule) {
        Formule updated = formuleService.updateFormule(formule, id);
        
        return updated != null 
            ? ResponseEntity.ok(updated)
            : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormule(@PathVariable String id) {
        if (formuleService.formuleExists(id)) {
            formuleService.deleteFormule(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
