package com.rh.manage.Controller;

import com.rh.manage.Model.*;
import com.rh.manage.Service.MouvementService;
import com.rh.manage.Service.AutomatisationService;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.InfosProfessionnellesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/mouvements")

public class MouvementController {

    @Autowired 
    AutomatisationService automatisationService;

    private final MouvementService mouvementService;
    private final EmployeService employeService;

    public MouvementController(MouvementService mouvementService,
                             EmployeService employeService) {
        this.mouvementService = mouvementService;
        this.employeService = employeService;
    }
    
    @PostMapping
    public ResponseEntity<?> insertMouvement(@RequestBody Mouvement mouvement) {
        try {

            // typeMouvement 
            // idInfoProActuel 
            // infosProPose 
            System.out.println("=== DÉBUT INSERTION MOUVEMENT ===");
            System.out.println("Mouvement reçu: " + mouvement);
            
            // ✅ VALIDATION DES DONNÉES OBLIGATOIRES
            if (mouvement == null) {
                return ResponseEntity.badRequest().body("Le mouvement ne peut pas être null");
            }
            
            if (mouvement.getInfosProActuel() == null) {
                return ResponseEntity.badRequest().body("L'info pro actuelle est obligatoire");
            }
            
            if (mouvement.getEmployeDemandeur() == null) {
                return ResponseEntity.badRequest().body("L'employé demandeur est obligatoire");
            }
            
            if (mouvement.getInfosProPropose() == null) {
                return ResponseEntity.badRequest().body("L'info pro proposée est obligatoire");
            }
            
            // ✅ VÉRIFICATION ET CORRECTION DU MANAGER
            if (mouvement.getInfosProPropose() != null) {
                InfosProfessionnelles infosProPropose = mouvement.getInfosProPropose();
                
                System.out.println("Manager dans infosProPropose: " + infosProPropose.getManager());
                if (infosProPropose.getManager() != null) {
                    System.out.println("Manager ID: " + infosProPropose.getManager().getId());
                    
                    // Si Manager a un ID null → objet vide, on le met à null
                    if (infosProPropose.getManager().getId() == null) {
                        System.out.println("⚠️ Correction: Manager avec ID null → mis à null");
                        infosProPropose.setManager(null);
                    }
                }
            }
            
            Mouvement mvt = mouvementService.creerDemandeMouvement(mouvement);
            automatisationService.sendEmailNotificationMouvement(mvt);
            
            System.out.println("✅ Mouvement créé avec succès: " + mvt.getId());
            return ResponseEntity.ok(mvt);
            
        } catch (RuntimeException e) {
            // Erreurs métier (validation, données manquantes, etc.)
            System.err.println("❌ Erreur métier: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            // Erreurs techniques
            System.err.println("❌ Erreur technique: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erreur interne du serveur: " + e.getMessage());
        }
    }

    /**
     * Récupérer tous les mouvements
     */
    @GetMapping
    public ResponseEntity<List<Mouvement>> getAllMouvements() {
        try {
            List<Mouvement> mouvements = mouvementService.findAll();
            return ResponseEntity.ok(mouvements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupérer un mouvement par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mouvement> getMouvementById(@PathVariable String id) {
        try {
            Optional<Mouvement> mouvement = mouvementService.findById(id);
            return mouvement.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/demande")
    public ResponseEntity<?> creerDemandeMouvement(@RequestBody Mouvement mouvement) {
        try {

            Mouvement savedMouvement = mouvementService.creerDemandeMouvement(mouvement);
            automatisationService.sendEmailNotificationMouvement(savedMouvement);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                        "status", "success",
                        "message", "Demande de mouvement créée avec succès.",
                        "data", savedMouvement
                    ));

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "status", "error",
                        "message", e.getMessage()
                    ));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "error",
                        "message", "Une erreur interne s'est produite."
                    ));
        }
    }


    /**
     * Valider un mouvement
     */
    @PutMapping("/{id}/validation")
    public ResponseEntity<Mouvement> validerMouvement(
            @PathVariable String id, 
            @RequestBody Mouvement mouvement) { // Ajout de @RequestBody
        try {
            Mouvement mouvementValide;
            
            if(mouvement.getStatut() == 2) {
                mouvementValide = mouvementService.validerMouvement(id, mouvement);
            } else {
                mouvementValide = mouvementService.rejeterMouvement(id, mouvement);
            }
            
            return ResponseEntity.ok(mouvementValide);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Rejeter un mouvement
     */
    // @PostMapping("/{id}/rejet")
    // public ResponseEntity<Mouvement> rejeterMouvement(
    //         @PathVariable String id,
    //         @RequestBody RejetRequest rejetRequest) {
    //     try {
    //         Employe validateur = employeService.getById(rejetRequest.getIdValidateur())
    //             .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

    //         Mouvement mouvementRejete = mouvementService.rejeterMouvement(
    //             id, validateur, rejetRequest.getMotifRejet());

    //         return ResponseEntity.ok(mouvementRejete);

    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(null);
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().build();
    //     }
    // }

    /**
     * Récupérer les mouvements en attente de validation
     */
    @GetMapping("/en-attente")
    public ResponseEntity<List<Mouvement>> getMouvementsEnAttente() {
        try {
            List<Mouvement> mouvements = mouvementService.findMouvementsEnAttente();
            return ResponseEntity.ok(mouvements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupérer les mouvements d'un employé
     */
    @GetMapping("/employe/{idEmploye}")
    public ResponseEntity<List<Mouvement>> getMouvementsByEmploye(@PathVariable String idEmploye) {
        try {
            Employe employe = employeService.getById(idEmploye)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
            
            List<Mouvement> mouvements = mouvementService.findMouvementsByEmploye(employe);
            return ResponseEntity.ok(mouvements);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Supprimer un mouvement
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable String id) {
        try {
            mouvementService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
