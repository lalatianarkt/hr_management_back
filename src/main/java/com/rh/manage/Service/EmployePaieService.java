package com.rh.manage.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.EmployeInfosDTO;
import com.rh.manage.Dto.EmployePaieDTO;
import com.rh.manage.Model.Paie;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Repository.PeriodePaieRepository;

@Service
public class EmployePaieService {
    
    // Constantes pour les états
    private static final int ETAT_INACTIF = 0;      // période non active = rouge
    private static final int ETAT_EN_ATTENTE = 1;   // paie non générée = orange
    private static final int ETAT_ACTIF = 2;        // paie générée = vert
    
    @Autowired
    private EmployeService employeService;

    @Autowired 
    private PeriodePaieRepository periodePaieRepository;

    @Autowired
    private PaieService paieService;

    /**
     * Récupère tous les employés avec leur état de paie (sans filtre ni pagination)
     */
    public List<EmployePaieDTO> getAllEmpwithPaie() {
        List<EmployePaieDTO> les_infos_emp_paie = new ArrayList<>();
        
        // Récupérer tous les employés sans pagination
        List<EmployeInfosDTO> tousLesEmployes = employeService.getAllEmployesWithInfos();
        
        // Récupérer la période active (statut = 0)
        Optional<PeriodePaie> periodeActiveOpt = periodePaieRepository.findByStatut(0);
        
        for (EmployeInfosDTO employeInfosDTO : tousLesEmployes) {
            EmployePaieDTO employePaie = new EmployePaieDTO();
            employePaie.setEmployeAvecInfosDTO(employeInfosDTO);
            
            // Déterminer l'état de la paie pour cet employé
            int etat = determineEtatPaie(employeInfosDTO, periodeActiveOpt);
            employePaie.setEtat(etat);
            
            les_infos_emp_paie.add(employePaie);
        }
        
        return les_infos_emp_paie;
    }
    
    /**
     * Détermine l'état de la paie pour un employé par rapport à la période active
     */
    private int determineEtatPaie(EmployeInfosDTO employeInfosDTO, Optional<PeriodePaie> periodeActiveOpt) {
        // Si aucune période active n'existe
        if (!periodeActiveOpt.isPresent()) {
            return ETAT_INACTIF;
        }
        
        PeriodePaie periodeActive = periodeActiveOpt.get();
        String employeId = employeInfosDTO.getEmploye().getId();
        
        // Récupérer la dernière paie de l'employé
        Optional<Paie> lastPaieOpt = paieService.getLastPaieByEmploye(employeId);
        
        // Si l'employé n'a aucune paie
        if (!lastPaieOpt.isPresent()) {
            return ETAT_EN_ATTENTE;
        }
        
        Paie lastPaie = lastPaieOpt.get();
        
        // Vérifier si la dernière paie correspond à la période active
        if (lastPaie.getPeriodePaie() != null && 
            lastPaie.getPeriodePaie().getMois() != null &&
            lastPaie.getPeriodePaie().getMois().getNum() == periodeActive.getMois().getNum() && 
            lastPaie.getPeriodePaie().getAnnee() == periodeActive.getAnnee()) {
            
            return ETAT_ACTIF;
        }
        
        // Si la dernière paie ne correspond pas à la période active
        return ETAT_EN_ATTENTE;
    }
    
    /**
     * Récupère les employés par état
     */
    public List<EmployePaieDTO> getEmployesByEtat(int etat) {
        List<EmployePaieDTO> tousLesEmployes = getAllEmpwithPaie();
        List<EmployePaieDTO> employesFiltres = new ArrayList<>();
        
        for (EmployePaieDTO emp : tousLesEmployes) {
            if (emp.getEtat() == etat) {
                employesFiltres.add(emp);
            }
        }
        
        return employesFiltres;
    }
    
    /**
     * Compte le nombre d'employés par état
     */
    public long countByEtat(int etat) {
        List<EmployePaieDTO> tousLesEmployes = getAllEmpwithPaie();
        long compteur = 0;
        
        for (EmployePaieDTO emp : tousLesEmployes) {
            if (emp.getEtat() == etat) {
                compteur++;
            }
        }
        
        return compteur;
    }
}