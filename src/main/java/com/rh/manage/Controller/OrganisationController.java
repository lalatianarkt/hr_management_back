package com.rh.manage.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Dto.OrganisationDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Employe;
import com.rh.manage.Service.OrganisationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/organisation")
public class OrganisationController {
    @Autowired
    OrganisationService organisationService;

     /**
     * GET /api/organisation
     * Récupère la structure complète de l'organigramme
     */
    @GetMapping
    public ResponseEntity<List<OrganisationDTO>> getOrganisation() {
        try {
            List<OrganisationDTO> organisation = organisationService.getOrganisationStructure();
            return ResponseEntity.ok(organisation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    } 

    /**
     * GET /api/organisation/departement/{id}
     * Récupère l'organigramme d'un département spécifique
     */
    // @GetMapping("/departement/{id}")
    // public ResponseEntity<OrganisationDTO> getOrganisationByDepartement(@PathVariable String id) {
    //     try {
    //         OrganisationDTO organisation = organisationService.getOrganisationByDepartement(id);
    //         return ResponseEntity.ok(organisation);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.notFound().build();
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().build();
    //     }
    // } 

    /**
     * GET /api/organisation/departements-avec-managers
     * Récupère les départements qui ont au moins un manager actif
     */
    @GetMapping("/departements-avec-managers")
    public ResponseEntity<List<Departement>> getDepartementsAvecManagers() {
        try {
            List<Departement> departements = organisationService.getDepartementsAvecManagers();
            return ResponseEntity.ok(departements);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

     /**
     * GET /api/organisation/departement/{id}/employes-sans-manager
     * Récupère les employés sans manager dans un département
     */
    // @GetMapping("/departement/{id}/employes-sans-manager")
    // public ResponseEntity<List<Employe>> getEmployesSansManager(@PathVariable String id) {
    //     try {
    //         List<Employe> employes = organisationService.getEmployesSansManager(id);
    //         return ResponseEntity.ok(employes);
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().build();
    //     }
    // }
}
