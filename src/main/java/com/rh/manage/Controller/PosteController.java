package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Poste;
import com.rh.manage.Service.PosteService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/postes")
// @CrossOrigin(origins = "*")

public class PosteController {

    @Autowired
    private PosteService posteService;

    @GetMapping("/niveau/{idNiveau}")
    public List<Poste> getPostesByNiveau(@PathVariable String idNiveau) {
        return posteService.getPostesByIdNiveau(idNiveau);
    }

    @GetMapping("/dep/{idDep}")
    public ResponseEntity<List<Poste>> getPosteParDepartement(@PathVariable String idDep) {
        try {
            return ResponseEntity.ok(posteService.findByDepartementId(idDep));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer tous les postes
    @GetMapping
    public ResponseEntity<List<Poste>> getAllPostes() {
        try {
            List<Poste> postes = posteService.getAllPostes();
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer un poste par ID
    @GetMapping("/{id}")
    public ResponseEntity<Poste> getPosteById(@PathVariable String id) {
        try {
            Optional<Poste> poste = posteService.getPosteById(id);
            if (poste.isPresent()) {
                return ResponseEntity.ok(poste.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer un poste par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<Poste> getPosteByNom(@PathVariable String nom) {
        try {
            Optional<Poste> poste = posteService.getPosteByNom(nom);
            if (poste.isPresent()) {
                return ResponseEntity.ok(poste.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Créer un nouveau poste
    @PostMapping
    public ResponseEntity<?> createPoste(@RequestBody Poste poste) {
        try {
            Poste savedPoste = posteService.createPoste(poste);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPoste);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Mettre à jour un poste
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePoste(@PathVariable String id, @RequestBody Poste posteDetails) {
        try {
            Poste updatedPoste = posteService.updatePoste(id, posteDetails);
            return ResponseEntity.ok(updatedPoste);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Supprimer un poste
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePoste(@PathVariable String id) {
        try {
            posteService.deletePoste(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Rechercher des postes
    @GetMapping("/search")
    public ResponseEntity<List<Poste>> searchPostes(@RequestParam String q) {
        try {
            List<Poste> postes = posteService.searchPostes(q);
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Rechercher par nom
    @GetMapping("/search/nom")
    public ResponseEntity<List<Poste>> searchByNom(@RequestParam String nom) {
        try {
            List<Poste> postes = posteService.searchByNom(nom);
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Rechercher par description
    @GetMapping("/search/description")
    public ResponseEntity<List<Poste>> searchByDescription(@RequestParam String description) {
        try {
            List<Poste> postes = posteService.searchByDescription(description);
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Vérifier si un poste existe
    @GetMapping("/exists/{nom}")
    public ResponseEntity<Boolean> checkPosteExists(@PathVariable String nom) {
        try {
            boolean exists = posteService.posteExists(nom);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Compter le nombre de postes
    @GetMapping("/count")
    public ResponseEntity<Long> countPostes() {
        try {
            long count = posteService.countPostes();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer les postes récents
    @GetMapping("/recent")
    public ResponseEntity<List<Poste>> getRecentPostes() {
        try {
            List<Poste> postes = posteService.getRecentPostes();
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer les postes créés après une date
    @GetMapping("/after-date")
    public ResponseEntity<List<Poste>> getPostesAfterDate(@RequestParam String date) {
        try {
            LocalDateTime localDate = LocalDateTime.parse(date);
            List<Poste> postes = posteService.getPostesAfterDate(localDate);
            return ResponseEntity.ok(postes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
