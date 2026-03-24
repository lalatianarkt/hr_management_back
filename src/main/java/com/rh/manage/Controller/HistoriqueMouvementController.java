package com.rh.manage.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.HistoriqueMouvement;
import com.rh.manage.Service.HistoriqueMouvementService;

import java.util.List;

@RestController
@RequestMapping("/api/historique-mouvements")
public class HistoriqueMouvementController {

    private final HistoriqueMouvementService service;

    public HistoriqueMouvementController(HistoriqueMouvementService service) {
        this.service = service;
    }

    @GetMapping
    public List<HistoriqueMouvement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueMouvement> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueMouvement create(@RequestBody HistoriqueMouvement historique) {
        return service.save(historique);
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


