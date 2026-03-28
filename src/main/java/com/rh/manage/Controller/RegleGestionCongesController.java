package com.rh.manage.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.RegleGestionConges;
import com.rh.manage.Service.RegleGestionCongesService;

import java.util.List;

@RestController
@RequestMapping("/api/regles-conges")
public class RegleGestionCongesController {

    private final RegleGestionCongesService service;

    public RegleGestionCongesController(RegleGestionCongesService service) {
        this.service = service;
    }

    @GetMapping
    public List<RegleGestionConges> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegleGestionConges> getById(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RegleGestionConges create(@RequestBody RegleGestionConges regle) {
        return service.create(regle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegleGestionConges> update(@PathVariable int id, @RequestBody RegleGestionConges regle) {
        try {
            RegleGestionConges updated = service.update(id, regle); 
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build(); 
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}

