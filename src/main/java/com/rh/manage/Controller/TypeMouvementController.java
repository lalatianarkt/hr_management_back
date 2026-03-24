package com.rh.manage.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeMouvement;
import com.rh.manage.Service.TypeMouvementService;

import java.util.List;

@RestController
@RequestMapping("/api/type-mouvements")
public class TypeMouvementController {

    private final TypeMouvementService service;

    public TypeMouvementController(TypeMouvementService service) {
        this.service = service;
    }

    @GetMapping
    public List<TypeMouvement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeMouvement> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TypeMouvement create(@RequestBody TypeMouvement typeMouvement) {
        return service.save(typeMouvement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeMouvement> update(@PathVariable String id, @RequestBody TypeMouvement typeMouvement) {
        return service.getById(id)
                .map(existing -> {
                    existing.setType(typeMouvement.getType());
                    existing.setModifiedAt(typeMouvement.getModifiedAt());
                    TypeMouvement updated = service.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.getById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

