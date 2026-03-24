package com.rh.manage.Controller;

import com.rh.manage.Model.Sexe;
import com.rh.manage.Service.SexeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sexes")
// @CrossOrigin(origins = "*")

public class SexeController {

    @Autowired
    private SexeService sexeService;

    // Récupérer tous les sexes ordonnés
    @GetMapping
    public ResponseEntity<?> getAllSexes() {
        try {
            List<Sexe> sexes = sexeService.getAllSexes();
            return ResponseEntity.ok(sexes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer un sexe par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Sexe> getSexeById(@PathVariable Integer id) {
        try {
            Sexe sexe = sexeService.getSexeById(id);
            if (sexe != null) {
                return ResponseEntity.ok(sexe);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer un sexe par son code
    @GetMapping("/code/{code}")
    public ResponseEntity<Sexe> getSexeByCode(@PathVariable String code) {
        try {
            Sexe sexe = sexeService.getSexeByCode(code);
            if (sexe != null) {
                return ResponseEntity.ok(sexe);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Vérifier si un code existe
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> checkCodeExists(@PathVariable String code) {
        try {
            boolean exists = sexeService.codeExists(code);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Compter le nombre de sexes
    @GetMapping("/count")
    public ResponseEntity<Long> countSexes() {
        try {
            long count = sexeService.countSexes();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Rechercher par nom de sexe
    @GetMapping("/search")
    public ResponseEntity<List<Sexe>> searchSexes(@RequestParam String query) {
        try {
            List<Sexe> sexes = sexeService.searchBySexe(query);
            return ResponseEntity.ok(sexes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}