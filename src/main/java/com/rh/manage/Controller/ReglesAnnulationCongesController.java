package com.rh.manage.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.ReglesAnnulationConges;
import com.rh.manage.Service.ReglesAnnulationCongesService;

import java.util.List;

@RestController
@RequestMapping("/api/regles-annulation")
public class ReglesAnnulationCongesController {

    private final ReglesAnnulationCongesService service;

    public ReglesAnnulationCongesController(ReglesAnnulationCongesService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReglesAnnulationConges> getAll() {
        return service.getAllRegles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReglesAnnulationConges> getById(@PathVariable String id) {
        return service.getRegleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/actives")
    public List<ReglesAnnulationConges> getActives() {
        return service.getActives();
    }

    @PostMapping
    public ReglesAnnulationConges create(@RequestBody ReglesAnnulationConges regle) {
        return service.saveRegle(regle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReglesAnnulationConges> update(@PathVariable String id, @RequestBody ReglesAnnulationConges regle) {
        return service.getRegleById(id).map(existing -> {
            regle.setId(id);
            return ResponseEntity.ok(service.saveRegle(regle));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteRegle(id);
        return ResponseEntity.noContent().build();
    }
}

