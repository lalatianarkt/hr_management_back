package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.PreparationExportEmploye;
import com.rh.manage.Service.PreparationExportEmployeService;

@RestController
@RequestMapping("/api/preparation-export")
public class PreparationExportEmployeController {

    @Autowired
    private PreparationExportEmployeService service;

    /**
     * Récupérer la configuration d'export active
     */
    @GetMapping
    public ResponseEntity<PreparationExportEmploye> getActiveConfiguration() {
        PreparationExportEmploye config = service.getActiveConfiguration();
        return ResponseEntity.ok(config);
    }

    /**
     * Sauvegarder la configuration d'export
     */
    @PostMapping
    public ResponseEntity<PreparationExportEmploye> saveConfiguration(
            @RequestBody PreparationExportEmploye entity) {
        PreparationExportEmploye saved = service.saveConfiguration(entity);
        return ResponseEntity.ok(saved);
    }

    /**
     * Réinitialiser la configuration aux valeurs par défaut
     */
    @DeleteMapping("/reset")
    public ResponseEntity<PreparationExportEmploye> resetConfiguration() {
        PreparationExportEmploye defaultConfig = service.resetToDefault();
        return ResponseEntity.ok(defaultConfig);
    }
}
