package com.rh.manage.Service;

import com.rh.manage.Dto.EmployeAvecInfosDTO;
import com.rh.manage.Dto.EmployeDTO;
import com.rh.manage.Dto.ManagerHierarchieDTO;
import com.rh.manage.Dto.OrganisationDTO;
import com.rh.manage.Model.*;
import com.rh.manage.Repository.DepartementManagerRepository;
import com.rh.manage.Repository.DepartementRepository;
import com.rh.manage.Repository.InfosProfessionnellesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganisationService {

    @Autowired
    private DepartementService departementService;

    @Autowired
    private ManagerService managerService;

    // @Autowired
    // private ManagerEmployeService managerEmployeService;

    @Autowired
    private PosteEmployeService posteEmployeService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private DepartementManagerRepository departementManagerRepository;

    @Autowired
    private InfosProfessionnellesRepository infosProfessionnellesRepository;

    /**
 * Récupère la structure complète de l'organisation pour l'organigramme
 */
    public List<OrganisationDTO> getOrganisationStructure() {
        List<OrganisationDTO> organisation = new ArrayList<>();

        List<Departement> tousDepartements = departementRepository.findAll();

        for (Departement departement : tousDepartements) {
            OrganisationDTO organisationDto = new OrganisationDTO();
            organisationDto.setDepartement(departement);

            try {
                Manager managerActuel = managerService.findManagerActuelByDepartement(departement.getId());
                organisationDto.setManager(managerActuel); // peut être null

                if (managerActuel != null) {
                    try {
                        System.out.println("dep : " + departement.getNom());
                        System.out.println("manager actuel : " + managerActuel.getEmploye().getNom());

                        // List<InfosProfessionnelles> les_infos = infosProfessionnellesRepository.findByManagerIdAndStatut(managerActuel.getId());
                        List<InfosProfessionnelles> les_infos = infosProfessionnellesRepository.findByDepartementIdAndStatut(departement.getId());
                        
                        organisationDto.setInfosProfessionnelles(les_infos != null ? les_infos : new ArrayList<>());
                        System.out.println("emp lié aminy : " + organisationDto.getInfosProfessionnelles().size());

                    } catch (Exception e) {
                        System.err.println("Erreur lors de la récupération des infos professionnelles pour le manager "
                                + managerActuel.getId() + " : " + e.getMessage());
                        e.printStackTrace();
                        organisationDto.setInfosProfessionnelles(new ArrayList<>());
                    }
                } else {
                    System.out.println("Aucun manager pour ce département : " + departement.getNom());
                    organisationDto.setInfosProfessionnelles(new ArrayList<>());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du manager pour le département "
                        + departement.getNom() + " : " + e.getMessage());
                e.printStackTrace();
                organisationDto.setManager(null);
                organisationDto.setInfosProfessionnelles(new ArrayList<>());
            }

            organisation.add(organisationDto);
        }

        return organisation;
    }




   private List<ManagerHierarchieDTO> getManagersAvecEmployes(String departementId) {
    List<ManagerHierarchieDTO> managersHierarchie = new ArrayList<>();
    
    List<DepartementManager> managersActifs = departementManagerRepository
        .findManagersActifsByDepartement(departementId);
    
    for (DepartementManager dm : managersActifs) {
        Manager manager = dm.getManager();
        
        if (manager != null && manager.getStatut() == 0) {
            ManagerHierarchieDTO hierarchie = new ManagerHierarchieDTO();
            
            // ✅ VÉRIFIER SI L'EMPLOYÉ MANAGER EST ACTIF
            Employe employeManager = manager.getEmploye();
            if (employeManager != null && employeManager.getStatut() == 0) {
                
                // ✅ RÉCUPÉRER LES INFOS PRO DU MANAGER (sans filtre statut)
                List<InfosProfessionnelles> infosProManagerList = infosProfessionnellesRepository
                    .findByEmploye(employeManager);
                
                InfosProfessionnelles infosProManager = infosProManagerList.stream()
                    .findFirst()
                    .orElse(null);
                
                EmployeAvecInfosDTO managerAvecInfos = new EmployeAvecInfosDTO(
                    employeManager, 
                    infosProManager
                );
                
                hierarchie.setManager(managerAvecInfos);
                
                // ✅ EMPLOYÉS AVEC LEURS INFOS PRO (filtrer les employés actifs)
                List<InfosProfessionnelles> infosEmployes = infosProfessionnellesRepository
                    .findEmployesByManager(manager.getId());
                
                List<EmployeAvecInfosDTO> employesAvecInfos = infosEmployes.stream()
                    .filter(infosPro -> infosPro.getEmploye() != null && infosPro.getEmploye().getStatut() == 0)
                    .map(infosPro -> new EmployeAvecInfosDTO(infosPro.getEmploye(), infosPro))
                    .collect(Collectors.toList());
                
                hierarchie.setEmployes(employesAvecInfos);
                managersHierarchie.add(hierarchie);
            }
        }
    }
    
    return managersHierarchie;
}
     /**
     * Récupère l'organigramme pour un département spécifique
     */
    // public OrganisationDTO getOrganisationByDepartement(String departementId) {
    //     Departement departement = departementRepository.findById(departementId)
    //         .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        
    //     OrganisationDTO orgDTO = new OrganisationDTO();
    //     orgDTO.setDepartement(departement);
        
    //     List<ManagerHierarchieDTO> managersHierarchie = getManagersAvecEmployes(departementId);
    //     orgDTO.setManagers(managersHierarchie);
        
    //     return orgDTO;
    // } 

     /**
     * Récupère les employés sans manager dans un département
     */
    // public List<Employe> getEmployesSansManager(String departementId) {
    //     List<InfosProfessionnelles> infosEmployes = infosProfessionnellesRepository
    //         .findEmployesSansManagerByDepartement(departementId);
        
    //     return infosEmployes.stream()
    //         .map(InfosProfessionnelles::getEmploye)
    //         .collect(Collectors.toList());
    // }
    
    /**
     * Récupère les départements avec au moins un manager actif
     */
    public List<Departement> getDepartementsAvecManagers() {
        return departementManagerRepository.findDepartementsAvecManagersActifs();
    }
}