package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.TypeContrat;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;
import com.rh.manage.Service.TypeContratService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-contrats")
// @CrossOrigin(origins = "*")

public class TypeContratController {

    @Autowired
    private TypeContratService typeContratService;

    @Autowired
    private TokenService tokenService;

    // GET - Récupérer tous les types de contrats
    @GetMapping
    public ResponseEntity<?> getAllTypeContrats(@RequestHeader("Authorization") String authHeader) {
        try {
            tokenService.validateToken(authHeader);
            List<TypeContrat> typeContrats = typeContratService.getAllTypeContrats();
            return ResponseEntity.ok(typeContrats);
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer un type de contrat par ID
    @GetMapping("/{id}")
    public ResponseEntity<TypeContrat> getTypeContratById(@PathVariable String id) {
        try {
            Optional<TypeContrat> typeContrat = typeContratService.getTypeContratById(id);
            if (typeContrat.isPresent()) {
                return ResponseEntity.ok(typeContrat.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer un type de contrat par intitulé
    @GetMapping("/intitule/{intitule}")
    public ResponseEntity<TypeContrat> getTypeContratByIntitule(@PathVariable String intitule) {
        try {
            Optional<TypeContrat> typeContrat = typeContratService.getTypeContratByIntitule(intitule);
            if (typeContrat.isPresent()) {
                return ResponseEntity.ok(typeContrat.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Créer un nouveau type de contrat
    @PostMapping
    public ResponseEntity<?> createTypeContrat(@RequestBody TypeContrat typeContrat) {
        try {
            TypeContrat savedTypeContrat = typeContratService.createTypeContrat(typeContrat);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTypeContrat);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Mettre à jour un type de contrat
    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateTypeContrat(@PathVariable String id, @RequestBody TypeContrat typeContratDetails) {
    //     try {
    //         TypeContrat updatedTypeContrat = typeContratService.updateTypeContrat(id, typeContratDetails);
    //         return ResponseEntity.ok(updatedTypeContrat);
    //     } catch (RuntimeException e) {
    //         Map<String, String> errorResponse = new HashMap<>();
    //         errorResponse.put("error", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    // DELETE - Supprimer un type de contrat
    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> deleteTypeContrat(@PathVariable String id) {
    //     try {
    //         typeContratService.deleteTypeContrat(id);
    //         return ResponseEntity.noContent().build();
    //     } catch (RuntimeException e) {
    //         Map<String, String> errorResponse = new HashMap<>();
    //         errorResponse.put("error", e.getMessage());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    // GET - Rechercher des types de contrats
    // @GetMapping("/search")
    // public ResponseEntity<List<TypeContrat>> searchTypeContrats(@RequestParam String q) {
    //     try {
    //         List<TypeContrat> typeContrats = typeContratService.searchTypeContrats(q);
    //         return ResponseEntity.ok(typeContrats);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    // GET - Rechercher par intitulé
    @GetMapping("/search/intitule")
    public ResponseEntity<List<TypeContrat>> searchByIntitule(@RequestParam String intitule) {
        try {
            List<TypeContrat> typeContrats = typeContratService.searchByIntitule(intitule);
            return ResponseEntity.ok(typeContrats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Rechercher par description
    @GetMapping("/search/description")
    public ResponseEntity<List<TypeContrat>> searchByDescription(@RequestParam String description) {
        try {
            List<TypeContrat> typeContrats = typeContratService.searchByDescription(description);
            return ResponseEntity.ok(typeContrats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Vérifier si un type de contrat existe
    @GetMapping("/exists/{intitule}")
    public ResponseEntity<Boolean> checkTypeContratExists(@PathVariable String intitule) {
        try {
            boolean exists = typeContratService.typeContratExists(intitule);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Compter le nombre de types de contrats
    @GetMapping("/count")
    public ResponseEntity<Long> countTypeContrats() {
        try {
            long count = typeContratService.countTypeContrats();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Récupérer les types de contrats actifs (avec contrats associés)
    // @GetMapping("/actifs")
    // public ResponseEntity<List<TypeContrat>> getTypesContratAvecContrats() {
    //     try {
    //         List<TypeContrat> typeContrats = typeContratService.getTypesContratAvecContrats();
    //         return ResponseEntity.ok(typeContrats);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    // GET - Vérifier si un type de contrat peut être supprimé
    // @GetMapping("/{id}/can-delete")
    // public ResponseEntity<Boolean> canDeleteTypeContrat(@PathVariable String id) {
    //     try {
    //         boolean canDelete = typeContratService.canDeleteTypeContrat(id);
    //         return ResponseEntity.ok(canDelete);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

    // GET - Récupérer les types de contrats les plus utilisés
    // @GetMapping("/plus-utilises")
    // public ResponseEntity<List<Object[]>> getTypesContratPlusUtilises() {
    //     try {
    //         List<Object[]> typeContrats = typeContratService.getTypesContratPlusUtilises();
    //         return ResponseEntity.ok(typeContrats);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
}
