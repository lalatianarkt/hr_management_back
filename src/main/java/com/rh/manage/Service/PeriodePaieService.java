package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Mois;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Repository.MoisRepository;
import com.rh.manage.Repository.PeriodePaieRepository;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeriodePaieService {

    private final EmployeService employeService;
    
    @Autowired
    private PaieService paieService;

    @Autowired
    MoisRepository moisRepository;

    @Autowired
    MouvementSoldeService mouvementSoldeService;

    private final PeriodePaieRepository periodePaieRepository;
    
    public PeriodePaieService(PeriodePaieRepository periodePaieRepository, EmployeService employeService) {
        this.periodePaieRepository = periodePaieRepository;
        this.employeService = employeService;
    }
    public PeriodePaie createPeriodePaie(PeriodePaie periodePaie) {    
        if (periodePaie.getDateDebut() == null || periodePaie.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être null");
        }

        if (periodePaieRepository.existsByDateRange(periodePaie.getDateDebut(), periodePaie.getDateFin())) {
            throw new IllegalArgumentException("Une période existe déjà pour cette plage de dates");
        }

        if (periodePaie.getMois() != null && periodePaie.getMois().getId() != null) {
            Mois managedMois = moisRepository.getReferenceById(periodePaie.getMois().getId());
            periodePaie.setMois(managedMois);
        }
        return periodePaieRepository.save(periodePaie);    
    }
    
    // Récupérer toutes les périodes
    public List<PeriodePaie> getAllPeriodePaies() {
        return periodePaieRepository.findAll();
    }
    
    // Récupérer une période par son ID
    public Optional<PeriodePaie> getPeriodePaieById(String id) {
        return periodePaieRepository.findById(id);
    }
    
    // Mettre à jour une période
    public PeriodePaie updatePeriodePaie(String id, PeriodePaie updatedPeriode) {
        return periodePaieRepository.findById(id)
                .map(existingPeriode -> {
                    existingPeriode.setDateDebut(updatedPeriode.getDateDebut());
                    existingPeriode.setDateFin(updatedPeriode.getDateFin());
                    existingPeriode.setStatut(updatedPeriode.getStatut());
                    existingPeriode.setModifiedAt(LocalDateTime.now());
                    return periodePaieRepository.save(existingPeriode);
                })
                .orElseThrow(() -> new RuntimeException("Période non trouvée avec l'ID : " + id));
    }
    
    // Supprimer une période
    public void deletePeriodePaie(String id) {
        if (!periodePaieRepository.existsById(id)) {
            throw new RuntimeException("Période non trouvée avec l'ID : " + id);
        }
        periodePaieRepository.deleteById(id);
    }
    
    // Rechercher par statut
    public Optional<PeriodePaie> getPeriodePaiesByStatut(Integer statut) {
        return periodePaieRepository.findByStatut(statut);
    }

    // mouvementSoldeService.validerReportSolde(employeService.getEmployesActifs());
    @Transactional 
    public PeriodePaie cloturePaie(String idPeriode, PeriodePaie periodePaie) throws Exception{
        mouvementSoldeService.validerReportSolde(employeService.getEmployesActifs());
        paieService.cloturePaieParPeriode(periodePaie.getId());
        System.out.println("nety aloha le clôture paie");
        periodePaie.setStatut(1);
        periodePaie = periodePaieRepository.save(periodePaie);
        System.out.println("dia aveo mety ny période e ");
        
        return periodePaie;
    }   

    public PeriodePaie getPeriodePaieActif(){
        System.out.println("ato anaty periode paie actif+++++++++++++++++00");
        if(getPeriodePaiesByStatut(0).isPresent()){
            System.out.println("ato ve ??????????????????? ");
            System.out.println("id : " + getPeriodePaiesByStatut(0).get().getId());
            return getPeriodePaiesByStatut(0).get();
        } else{
            System.out.println("tsisy actif hono e");
            return null;
        }  
    } 
    
    // Rechercher des périodes entre deux dates
    public List<PeriodePaie> getPeriodePaiesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return periodePaieRepository.findBetweenDates(startDate, endDate);
    }
    
    // Obtenir les périodes actives
    public List<PeriodePaie> getActivePeriodePaies() {
        return periodePaieRepository.findActivePeriods();
    }
    
    // Vérifier si une période existe
    public boolean existsById(String id) {
        return periodePaieRepository.existsById(id);
    }
}
