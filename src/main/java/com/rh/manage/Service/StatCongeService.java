package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.StatCongeDTO;
import com.rh.manage.Model.DemandeConge;

@Service
public class StatCongeService {
    @Autowired 
    DemandeCongeService demandeCongeService;

    public StatCongeDTO getStatistiqueConge(){
        StatCongeDTO statCongeDTO = new StatCongeDTO();
        Long totalDemandes = demandeCongeService.getTotalDemandesAnneeEnCours();
        // Long totalDemandesEnAttente = demandeCongeService.getTotalDemandesEnAttenteParAnneeEnCours();
        // Long totalDemandesApprouvees = demandeCongeService.getTotalDemandesApprouveesParAnneeEnCours();
        // Long totalDemandesRefusees = demandeCongeService.getTotalDemandesRefuseesParAnneeEnCours();
        // Long totalDemandesAnnulees = demandeCongeService.getTotalDemandesAnnuleesParAnneeEnCours();
        // Double tauxApprobation = calculTauxApprobation(totalDemandes, totalDemandesApprouvees);
        Double moyenneJoursDemande = demandeCongeService.getMoyenneJoursDemandeesParAnneeEnCours();
        String moisPic = demandeCongeService.getMoisPicConges();

        statCongeDTO.setTotalDemandes(totalDemandes);
        // statCongeDTO.setDemandesEnAttente(totalDemandesEnAttente);
        // statCongeDTO.setDemandesApprouvees(totalDemandesApprouvees);
        // statCongeDTO.setDemandesRefusees(totalDemandesRefusees);
        // statCongeDTO.setDemandesAnnulees(totalDemandesAnnulees);
        // statCongeDTO.setTauxApprobation(tauxApprobation);
        statCongeDTO.setJoursMoyens(moyenneJoursDemande);
        statCongeDTO.setMoisPlusActif(moisPic);
        return statCongeDTO;
    } 

    public Double calculTauxApprobation(Long approuvees, Long rejetees) {
        // Vérifier la division par zéro
        if (approuvees == null || rejetees == null) {
            return 0.0;
        }
        
        Long totalTraitees = approuvees + rejetees;
        
        // Si aucune demande traitée, taux = 0
        if (totalTraitees == 0) {
            return 0.0;
        }
        
        // Calculer avec précision décimale
        return (approuvees.doubleValue() / totalTraitees.doubleValue()) * 100.0;
    }
}
