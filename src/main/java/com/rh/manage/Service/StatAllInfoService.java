package com.rh.manage.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.StatAllInfoDTO;
import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.InfosProfessionnelles;

@Service
public class StatAllInfoService {
    // private int nb_manager; ok
    // private int nb_departement; ok
    // private int nb_contrat_cdi; ok
    // private int nb_contrat_cdd; ok
    // private int nb_employe; ok
    // private int nb_poste; ok
    // private int nb_employes_en_conges; ok
    // private int nb_competences_employes; ok
    // private double nb_competences_added_dernier_mois; 
    // private double nb_moyenne_competence_par_employe;

    @Autowired 
    DepartementService departementService;

    @Autowired
    PosteService posteService;

    @Autowired
    EmployeService employeService;

    @Autowired
    TypeContratService typeContratService;

    @Autowired 
    CompetenceEmployeService competenceEmployeService;

    @Autowired
    DemandeCongeService demandeCongeService;

    @Autowired
    ManagerService managerService;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    public StatAllInfoDTO getAllStatistiques(){
        StatAllInfoDTO statAllInfoDTO = new StatAllInfoDTO();
        statAllInfoDTO.setNb_departement(departementService.getDepartementActif().size());
        statAllInfoDTO.setNb_manager(managerService.getManagerActif().size());
        statAllInfoDTO.setNb_contrat_cdd(infosProfessionnellesService.getAllInfosProParTypeContrat("CDD").size());
        statAllInfoDTO.setNb_contrat_cdi(infosProfessionnellesService.getAllInfosProParTypeContrat("CDI").size());
        statAllInfoDTO.setNb_employe(employeService.findAllEmployeesActived().size());
        statAllInfoDTO.setNb_poste(calculNbPosteOccupe(infosProfessionnellesService.getAllInfoProActif()));
        statAllInfoDTO.setNb_employes_en_conges(demandeCongeService. findTodayDemandeCongeActif().size());
        // statAllInfoDTO.setNb_employes_en_conges(demandeCongeService.findTodayDemandeCongeActif().size());
        // statAllInfoDTO.
        return statAllInfoDTO;
    } 

    public int calculNbPosteOccupe(List<InfosProfessionnelles> les_infos_pro_actif) {
        Set<String> postesIdsUniques = new HashSet<>();
        for (InfosProfessionnelles infoPro : les_infos_pro_actif) {
            // Vérifier que le poste n'est pas null
            if (infoPro.getPoste() != null && infoPro.getPoste().getId() != null) {
                postesIdsUniques.add(infoPro.getPoste().getId());
            }
        }
        return postesIdsUniques.size();
    }
}
