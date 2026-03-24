package com.rh.manage.Controller;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Poste;
import com.rh.manage.Service.DepartementService;
import com.rh.manage.Service.PosteService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/departements")
// @CrossOrigin(origins = "*")
 // permet les requêtes depuis le front (React, etc.)
public class DepartementController {

    @Autowired
    private PosteService posteService;

    @Autowired
    private DepartementService departementService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/actif")
    public ResponseEntity<?> getAllDepartementsActifs(){
        return ResponseEntity.ok(departementService.getDepartementActif());
    }
    
    @PostMapping
    public ResponseEntity<?> createDepartement(@RequestBody Departement departement) {
        try {
            Departement newDepartement = departementService.saveDepartement(departement);
            return ResponseEntity.ok(newDepartement);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création du département: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDepartements(@RequestHeader("Authorization") String authHeader) {
        try {
            tokenService.validateToken(authHeader);
            List<Departement> departements = departementService.getAllDepartements();
            return ResponseEntity.ok(departements);
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }  catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des départements: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable String id) {
        Optional<Departement> departement = departementService.getDepartementById(id);
        return departement.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartement(@PathVariable String id, @RequestBody Departement updatedDepartement) {
        try {
            Departement saved = departementService.updateDepartement(updatedDepartement);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du département: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable String id) {
        departementService.deleteDepartement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Departement> getDepartementByNom(@PathVariable String nom) {
        Departement departement = departementService.getDepartementByNom(nom);
        if (departement != null) {
            return ResponseEntity.ok(departement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{departementId}/postes")
    public ResponseEntity<List<Poste>> getPostesByDepartement(@PathVariable String departementId) {
        try {
            List<Poste> postes = posteService.getPostesByDepartementId(departementId);
            
            if (postes.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            
            return ResponseEntity.ok(postes); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // 500 Internal Server Error
        }
    }
    
}
