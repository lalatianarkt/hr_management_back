package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Formateur;
import com.rh.manage.Service.FormateurService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formateurs")
// @CrossOrigin(origins = "*")

public class FormateurController {
    
    @Autowired
    private FormateurService formateurService;
    
    // GET - Récupérer tous les formateurs
    @GetMapping
    public ResponseEntity<List<Formateur>> getAllFormateurs() {
        try {
            List<Formateur> formateurs = formateurService.getAllFormateurs();
            return new ResponseEntity<>(formateurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Récupérer un formateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Formateur> getFormateurById(@PathVariable String id) {
        Optional<Formateur> formateur = formateurService.getFormateurById(id);
        
        if (formateur.isPresent()) {
            return new ResponseEntity<>(formateur.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // POST - Créer un nouveau formateur
    @PostMapping
    public ResponseEntity<Formateur> createFormateur(@RequestBody Formateur formateur) {
        try {
            Formateur nouveauFormateur = formateurService.createFormateur(formateur);
            return new ResponseEntity<>(nouveauFormateur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PUT - Mettre à jour un formateur existant
    @PutMapping("/{id}")
    public ResponseEntity<Formateur> updateFormateur(@PathVariable String id, @RequestBody Formateur formateur) {
        try {
            Formateur formateurMaj = formateurService.updateFormateur(id, formateur);
            return new ResponseEntity<>(formateurMaj, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // DELETE - Supprimer un formateur
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFormateur(@PathVariable String id) {
        try {
            formateurService.deleteFormateur(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Formateur> getFormateurByEmail(@PathVariable String email) {
        Optional<Formateur> formateur = formateurService.getFormateurByEmail(email);
        
        if (formateur.isPresent()) {
            return new ResponseEntity<>(formateur.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // GET - Rechercher par spécialité
    @GetMapping("/specialite/{specialite}")
    public ResponseEntity<List<Formateur>> getFormateursBySpecialite(@PathVariable String specialite) {
        try {
            List<Formateur> formateurs = formateurService.getFormateursBySpecialite(specialite);
            return new ResponseEntity<>(formateurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Formateur>> getFormateursByType(@PathVariable Integer type) {
        try {
            List<Formateur> formateurs = formateurService.getFormateursByType(type);
            return new ResponseEntity<>(formateurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Rechercher par nom (recherche partielle)
    @GetMapping("/recherche")
    public ResponseEntity<List<Formateur>> searchFormateursByNom(@RequestParam String nom) {
        try {
            List<Formateur> formateurs = formateurService.searchFormateursByNom(nom);
            return new ResponseEntity<>(formateurs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET - Compter le nombre de formateurs
    @GetMapping("/count")
    public ResponseEntity<Long> countFormateurs() {
        try {
            long count = formateurService.countFormateurs();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
