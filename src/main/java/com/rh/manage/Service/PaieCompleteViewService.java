package com.rh.manage.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Repository.PaieCompleteViewRepository;
import com.rh.manage.View.PaieCompleteView;

@Service
public class PaieCompleteViewService {
    @Autowired
    PaieCompleteViewRepository paieCompleteViewRepository;

    public List<PaieCompleteView> getAllPaies() {
        return paieCompleteViewRepository.findAll();
    }

    public List<PaieCompleteView> getPaiesByEmploye(Long idEmploye) {
        return paieCompleteViewRepository.findByIdEmploye(idEmploye);
    }

    public List<PaieCompleteView> getPaiesByPeriode(Integer annee, Integer mois) {
        return paieCompleteViewRepository.findByAnneePaieAndMoisPaie(annee, mois);
    }
    public List<PaieCompleteView> getPaieParDepartementEtParPeriodePaie(String departement, LocalDate date_debut_periode){
        return paieCompleteViewRepository.findByDepartementAndDateDebutPeriode(departement, date_debut_periode);
    }
    
}
