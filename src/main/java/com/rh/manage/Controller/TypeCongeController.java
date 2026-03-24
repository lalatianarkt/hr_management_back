package com.rh.manage.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeConge;
import com.rh.manage.Service.TypeCongeService;

import java.util.List;

@RestController
@RequestMapping("/api/type-conge")
public class TypeCongeController {

    private final TypeCongeService service;

    public TypeCongeController(TypeCongeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeConge> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TypeConge create(@RequestBody TypeConge typeConge) {
        return service.save(typeConge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeConge> update(@PathVariable String id, @RequestBody TypeConge typeConge) {
        return service.findById(id)
                .map(existing -> {
                    typeConge.setId(id);
                    return ResponseEntity.ok(service.save(typeConge));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

