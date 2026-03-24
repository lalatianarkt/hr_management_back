package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Dto.CalculCleRibRequest;
import com.rh.manage.Dto.CalculCleRibResponse;
import com.rh.manage.Model.ModePaiement;
import com.rh.manage.Service.ModePaiementService;

import java.util.List;

@RestController
@RequestMapping("/api/modes-paiement")
public class ModePaiementController {
    @Autowired
    private ModePaiementService modePaiementService;

    @PostMapping("/calculer-cle-rib")
    public ResponseEntity<CalculCleRibResponse> calculerCleRib(
            @RequestBody CalculCleRibRequest request) {
        
        try {
            String cleRib = modePaiementService.calculerCleRIB(
                request.getCodeBanque(),
                request.getCodeGuichet(),
                request.getNumeroCompte()
            );
            
            CalculCleRibResponse response = new CalculCleRibResponse(
                request.getCodeBanque(),
                request.getCodeGuichet(),
                request.getNumeroCompte(),
                cleRib
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Endpoint pour vérifier si une clé RIB est valide
     * POST /api/modes-paiement/verifier-cle-rib
     */
    @PostMapping("/verifier-cle-rib")
    public ResponseEntity<Boolean> verifierCleRib(
            @RequestBody CalculCleRibRequest request,
            @RequestParam String cleSaisie) {
        
        boolean valide = modePaiementService.verifierCleRIB(
            request.getCodeBanque(),
            request.getCodeGuichet(),
            request.getNumeroCompte(),
            cleSaisie
        );
        
        return ResponseEntity.ok(valide);
    }
    
    /**
     * Endpoint GET pour calculer avec des paramètres dans l'URL (optionnel)
     * GET /api/modes-paiement/calculer-cle-rib?codeBanque=10001&codeGuichet=00001&numeroCompte=12345678901
     */
    @GetMapping("/calculer-cle-rib")
    public ResponseEntity<CalculCleRibResponse> calculerCleRibGet(
            @RequestParam String codeBanque,
            @RequestParam String codeGuichet,
            @RequestParam String numeroCompte) {
        
        try {
            String cleRib = modePaiementService.calculerCleRIB(
                codeBanque, codeGuichet, numeroCompte
            );
            
            CalculCleRibResponse response = new CalculCleRibResponse(
                codeBanque, codeGuichet, numeroCompte, cleRib
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ModePaiement> createModePaiement(@RequestBody ModePaiement modePaiement) {
        try {
            ModePaiement created = modePaiementService.createModePaiement(modePaiement);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ModePaiement> updateModePaiement(
            @PathVariable String id, 
            @RequestBody ModePaiement modePaiement) {
        ModePaiement updated = modePaiementService.updateModePaiement(id, modePaiement);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ModePaiement> getModePaiementById(@PathVariable String id) {
        ModePaiement modePaiement = modePaiementService.getModePaiementById(id);
        return ResponseEntity.ok(modePaiement);
    }
    
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<?> getModesPaiementByEmploye(@PathVariable String employeId) {
        try {
            List<ModePaiement> modesPaiement = modePaiementService.getModesPaiementByEmploye(employeId);
            return ResponseEntity.ok(modesPaiement);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du département: " + e.getMessage());
        }
        
    }
    
    @GetMapping("/employe/{employeId}/actifs")
    public ResponseEntity<List<ModePaiement>> getActiveModesPaiementByEmploye(@PathVariable String employeId) {
        List<ModePaiement> modesPaiement = modePaiementService.getActiveModesPaiementByEmploye(employeId);
        return ResponseEntity.ok(modesPaiement);
    }
    
    @GetMapping("/employe/{employeId}/defaut")
    public ResponseEntity<ModePaiement> getDefaultModePaiementByEmploye(@PathVariable String employeId) {
        ModePaiement modePaiement = modePaiementService.getDefaultModePaiementByEmploye(employeId);
        return ResponseEntity.ok(modePaiement);
    }
    
    @PatchMapping("/{id}/set-default")
    public ResponseEntity<ModePaiement> setAsDefaultMode(@PathVariable String id) {
        try {
            ModePaiement modePaiement = modePaiementService.setAsDefaultMode(id);
            return ResponseEntity.ok(modePaiement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/toggle-actif")
    public ResponseEntity<ModePaiement> toggleActif(@PathVariable String id) {
        ModePaiement modePaiement = modePaiementService.toggleActif(id);
        return ResponseEntity.ok(modePaiement);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModePaiement(@PathVariable String id) {
        try {
            modePaiementService.deleteModePaiement(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
