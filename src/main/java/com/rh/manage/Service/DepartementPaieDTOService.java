package com.rh.manage.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.DepartementPaieDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.PeriodePaie;

@Service
public class DepartementPaieDTOService {
    @Autowired
    PaieService paieService;

    @Autowired
    DepartementService departementService;

    @Autowired
    PeriodePaieService periodePaieService;

    public List<DepartementPaieDTO> getPaiesParDepartement(){
        List<DepartementPaieDTO> departementPaies = new ArrayList<>();
        PeriodePaie periodePaieActif = null;
        if(periodePaieService.getPeriodePaiesByStatut(0).isPresent()){
            periodePaieActif = periodePaieService.getPeriodePaiesByStatut(0).get();
        }
        List<Departement> les_departements = departementService.getDepartementActif();
        for (Departement departement : les_departements) {
            DepartementPaieDTO departPaie = new DepartementPaieDTO();
            departPaie.setDepartement(departement);
            if(periodePaieActif != null && paieService.findByDepartementAndPeriodePaieId
                            (departement.getNom(), periodePaieActif.getId()).isPresent()){
                departPaie.setDate_debut_periode(periodePaieActif.getDateDebut());
                departPaie.setStatut(0); //ouvert en cours
            } else { 
                departPaie.setStatut(1); //en attente
            }
            departementPaies.add(departPaie);
        }
        return departementPaies;
    } 
}
