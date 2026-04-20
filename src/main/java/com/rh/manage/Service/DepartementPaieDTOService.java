package com.rh.manage.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.DepartementPaieDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Repository.PaieRepository;

@Service
public class DepartementPaieDTOService {
    @Autowired
    PaieService paieService;

    @Autowired
    DepartementService departementService;

    @Autowired
    PeriodePaieService periodePaieService;
    
    @Autowired
    PaieRepository paieRepository;  // Ajout du repository

    public List<DepartementPaieDTO> getPaiesParDepartement(){
        List<DepartementPaieDTO> departementPaies = new ArrayList<>();
        PeriodePaie periodePaieActif = null;
        
        // Récupérer la période active (statut 0 = active)
        if(periodePaieService.getPeriodePaiesByStatut(0).isPresent()){
            periodePaieActif = periodePaieService.getPeriodePaiesByStatut(0).get();
        }
        
        // Récupérer tous les départements actifs
        List<Departement> les_departements = departementService.getDepartementActif();
        
        for (Departement departement : les_departements) {
            DepartementPaieDTO departPaie = new DepartementPaieDTO();
            departPaie.setDepartement(departement);
            
            // Compter le nombre de paies générées pour ce département
            int nbPaieGenere = 0;
            
            if(periodePaieActif != null) {
                // Méthode 1: Utiliser le repository directement avec le nom du département
                long count = paieRepository.countByDepartementAndPeriodePaieId(
                    departement.getNom(), 
                    periodePaieActif.getId()
                );
                nbPaieGenere = (int) count;
                
                // Méthode 2: Utiliser l'ID du département (alternative)
                // long count = paieRepository.countByDepartementIdAndPeriodePaieId(
                //     departement.getId(), 
                //     periodePaieActif.getId()
                // );
                // nbPaieGenere = (int) count;
                
                // Vérifier si le département a déjà une paie générée pour cette période
                if(paieService.findByDepartementAndPeriodePaieId(departement.getNom(), periodePaieActif.getId()).isPresent()){
                    departPaie.setDate_debut_periode(periodePaieActif.getDateDebut());
                    departPaie.setStatut(0); // ouvert en cours
                } else { 
                    departPaie.setStatut(1); // en attente
                }
            } else {
                departPaie.setStatut(1); // en attente (pas de période active)
            }
            
            // Ajouter le nombre de paies générées au DTO
            departPaie.setNbPaieGenere(nbPaieGenere);
            departementPaies.add(departPaie);
        }
        return departementPaies;
    }
    
    // Méthode alternative si vous voulez compter pour une période spécifique
    // public List<DepartementPaieDTO> getPaiesParDepartementForPeriode(Long periodeId){
    //     List<DepartementPaieDTO> departementPaies = new ArrayList<>();
    //     PeriodePaie periode = periodePaieService.getPeriodePaieById(periodeId).orElse(null);
        
    //     if(periode == null) {
    //         return departementPaies;
    //     }
        
    //     List<Departement> les_departements = departementService.getDepartementActif();
        
    //     for (Departement departement : les_departements) {
    //         DepartementPaieDTO departPaie = new DepartementPaieDTO();
    //         departPaie.setDepartement(departement);
    //         departPaie.setDate_debut_periode(periode.getDateDebut());
            
    //         // Compter les paies générées pour ce département
    //         long count = paieRepository.countByDepartementAndPeriodePaieId(
    //             departement.getNom(), 
    //             periode.getId()
    //         );
    //         departPaie.setNbPaieGenere((int) count);
            
    //         // Déterminer le statut
    //         if(count > 0) {
    //             departPaie.setStatut(0); // ouvert (des paies ont été générées)
    //         } else {
    //             departPaie.setStatut(1); // en attente
    //         }
            
    //         departementPaies.add(departPaie);
    //     }
    //     return departementPaies;
    // }
}