package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.NiveauHierarchique;
import com.rh.manage.Service.NiveauHierarchiqueService;

import java.util.List;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauHierarchiqueController {

    @Autowired
    private NiveauHierarchiqueService service;

    // GET /api/niveaux
    @GetMapping
    public List<NiveauHierarchique> getAllNiveaux() {
        return service.getAll();
    }

    // GET /api/niveaux/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NiveauHierarchique> getNiveauById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/niveaux
    @PostMapping
    public NiveauHierarchique createNiveau(@RequestBody NiveauHierarchique niveau) {
        return service.save(niveau);
    }

    // PUT /api/niveaux/{id}
    @PutMapping("/{id}")
    public ResponseEntity<NiveauHierarchique> updateNiveau(@PathVariable String id, @RequestBody NiveauHierarchique niveauDetails) {
        return service.getById(id).map(niveau -> {
            niveau.setNom(niveauDetails.getNom());
            niveau.setRang(niveauDetails.getRang());
            niveau.setDescription(niveauDetails.getDescription());
            niveau.setModifiedAt(niveauDetails.getModifiedAt());
            return ResponseEntity.ok(service.save(niveau));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/niveaux/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

