package com.rh.manage.Controller;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Model.VuePaieFille;
import com.rh.manage.Service.DepartementService;
import com.rh.manage.Service.VuePaieFilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/vue-paie-fille")
// @CrossOrigin(origins = "*")

public class VuePaieFilleController {
    
    @Autowired
    private VuePaieFilleService service;

    @Autowired
    private DepartementService departementService;    
    
    // Récupérer toutes les lignes
    @GetMapping
    public ResponseEntity<List<VuePaieFille>> getAll() {
        try {
            List<VuePaieFille> result = service.getAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // 
    // Récupérer par ID de paie
    @GetMapping("/paie/{idPaie}")
    public ResponseEntity<List<VuePaieFille>> getByIdPaie(@PathVariable String idPaie) {
        try {
            List<VuePaieFille> result = service.getByIdPaie(idPaie);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer par code de rubrique
    @GetMapping("/code/{code}")
    public ResponseEntity<List<VuePaieFille>> getByCode(@PathVariable String code) {
        try {
            List<VuePaieFille> result = service.getByCode(code);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer une ligne spécifique
    @GetMapping("/paie/{idPaie}/code/{code}")
    public ResponseEntity<VuePaieFille> getByIdPaieAndCode(
            @PathVariable String idPaie, 
            @PathVariable String code) {
        try {
            VuePaieFille result = service.getByIdPaieAndCode(idPaie, code);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Calculer le total des montants pour une paie
    @GetMapping("/paie/{idPaie}/total")
    public ResponseEntity<BigDecimal> getTotalMontantByIdPaie(@PathVariable String idPaie) {
        try {
            BigDecimal result = service.getTotalMontantByIdPaie(idPaie);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les totaux par code de rubrique
    @GetMapping("/totals-by-code")
    public ResponseEntity<Map<String, BigDecimal>> getTotalsByCode() {
        try {
            Map<String, BigDecimal> result = service.getTotalsByCode();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les statistiques par paie
    @GetMapping("/stats-by-paie")
    public ResponseEntity<List<Map<String, Object>>> getStatsByPaie() {
        try {
            List<Map<String, Object>> result = service.getStatsByPaie();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les codes distincts
    @GetMapping("/codes-distincts")
    public ResponseEntity<List<String>> getDistinctCodes() {
        try {
            List<String> result = service.getDistinctCodes();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les ID de paies distincts
    @GetMapping("/paies-distinctes")
    public ResponseEntity<List<String>> getDistinctIdPaies() {
        try {
            List<String> result = service.getDistinctIdPaies();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les rubriques qui dépassent les plafonds
    // @GetMapping("/exceeded-plafonds")
    // public ResponseEntity<Map<String, List<VuePaieFille>>> getRubriquesExceeded() {
    //     try {
    //         Map<String, List<VuePaieFille>> result = service.getRubriquesExceeded();
    //         return ResponseEntity.ok(result);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
    
    // Obtenir les totaux par préfixe de code
    @GetMapping("/totals-by-code-prefix")
    public ResponseEntity<Map<String, Map<String, Object>>> getTotalsByCodePrefix() {
        try {
            Map<String, Map<String, Object>> result = service.getTotalsByCodePrefix();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir un résumé pour une paie
    @GetMapping("/paie/{idPaie}/resume")
    public ResponseEntity<Map<String, Object>> getResumeForPaie(@PathVariable String idPaie) {
        try {
            Map<String, Object> result = service.getResumeForPaie(idPaie);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Vérifier si une rubrique existe pour une paie
    @GetMapping("/exists/paie/{idPaie}/code/{code}")
    public ResponseEntity<Boolean> existsRubriqueForPaie(
            @PathVariable String idPaie, 
            @PathVariable String code) {
        try {
            boolean exists = service.existsRubriqueForPaie(idPaie, code);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Obtenir les rubriques groupées par paie
    @GetMapping("/grouped-by-paie")
    public ResponseEntity<Map<String, List<VuePaieFille>>> getGroupedByPaie() {
        try {
            Map<String, List<VuePaieFille>> result = service.getGroupedByPaie();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
