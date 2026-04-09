package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Dto.AbsenceCongeDTO;
import com.rh.manage.Dto.AbsenceCongeFilterDTO;
import com.rh.manage.Dto.StatistiquesAbsenceDTO;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Repository.AbsenceCongeRepository;
import com.rh.manage.View.AbsenceCongeView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AbsenceCongeService {
    
    private final AbsenceCongeRepository absenceCongeRepository;
    private final EmployeService employeService; // Service pour avoir le nombre total d'employés

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    public AbsenceCongeService(AbsenceCongeRepository absenceCongeRepository, EmployeService employeService) {
        this.absenceCongeRepository = absenceCongeRepository;
        this.employeService = employeService;
    }

    @Transactional(readOnly = true)
    public List<AbsenceCongeDTO> getAllAbsencesConges() {
        return absenceCongeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AbsenceCongeDTO> getAbsencesCongesWithFilters(AbsenceCongeFilterDTO filter) {
        List<AbsenceCongeView> result = absenceCongeRepository.findWithFilters(
                filter.getEmployeId(),
                filter.getDepartementId(),
                filter.getTypeAbsence(),
                filter.getDateDebut(),
                filter.getDateFin()
        );
        
        return result.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AbsenceCongeDTO> getByEmploye(String employeId) {
        return absenceCongeRepository.findByEmployeId(employeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AbsenceCongeDTO> getByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return absenceCongeRepository.findByDateAbsenceBetween(dateDebut, dateFin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AbsenceCongeDTO> getByDepartement(String departementId) {
        return absenceCongeRepository.findByIdDepartement(departementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AbsenceCongeView> getAbsenceByEmploye(String employeId){
       InfosProfessionnelles infosProActuelle = infosProfessionnellesService.getByIdEmploye(employeId).get();
       return absenceCongeRepository.findByEmployeIdAndIdDepartement(employeId, infosProActuelle.getDepartement().getId()).get();
    } 
    
    @Transactional(readOnly = true)
    public StatistiquesAbsenceDTO getStatistiquesGlobales() {
        LocalDate dateDebut = LocalDate.now().minusDays(100);
        LocalDate dateFin = LocalDate.now();
        return getStatistiquesGlobales(dateDebut, dateFin);
    }

    @Transactional(readOnly = true)
    public StatistiquesAbsenceDTO getStatistiquesGlobales(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("dateDebut doit Ãªtre avant dateFin");
        }
        return getStatistiquesParPeriode(dateDebut, dateFin);
    }
    
    @Transactional(readOnly = true)
    public StatistiquesAbsenceDTO getStatistiquesParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        Long totalAbsences = absenceCongeRepository.countByPeriodAndType(
                dateDebut, dateFin, AbsenceCongeView.TypeAbsence.absence);
        Long totalConges = absenceCongeRepository.countByPeriodAndType(
                dateDebut, dateFin, AbsenceCongeView.TypeAbsence.conge);
        Long totalEmployes = employeService.countTotalEmployes();
        
        long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);
        Double tauxAbsence = totalEmployes > 0 ? 
                (totalAbsences.doubleValue() / (totalEmployes * joursOuvrables)) * 100 : 0.0;
        Double tauxConge = totalEmployes > 0 ? 
                (totalConges.doubleValue() / (totalEmployes * joursOuvrables)) * 100 : 0.0;
        
        return new
             StatistiquesAbsenceDTO(totalAbsences, totalConges,
            tauxAbsence, tauxConge, dateDebut + " à " + dateFin, 
            dateDebut, dateFin
        );

        // return StatistiquesAbsenceDTO.builder()
        //         .totalAbsences(totalAbsences)
        //         .totalConges(totalConges)
        //         .tauxAbsence(tauxAbsence)
        //         .tauxConge(tauxConge)
        //         .periode(dateDebut + " à " + dateFin)
        //         .dateDebut(dateDebut)
        //         .dateFin(dateFin)
        //         .build();
    }

    @Transactional(readOnly = true)
    public StatistiquesAbsenceDTO getStatistiquesParPeriodeEtDepartement(LocalDate dateDebut, LocalDate dateFin, String departementId) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("dateDebut et dateFin sont obligatoires");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("dateDebut doit être avant dateFin");
        }
        if (departementId == null || departementId.trim().isEmpty()) {
            throw new IllegalArgumentException("departementId est obligatoire");
        }

        List<AbsenceCongeView> result = absenceCongeRepository.findWithFilters(
                null, departementId, null, dateDebut, dateFin);

        long totalAbsences = result.stream()
                .filter(a -> a.getTypeAbsence() == AbsenceCongeView.TypeAbsence.absence)
                .count();
        long totalConges = result.stream()
                .filter(a -> a.getTypeAbsence() == AbsenceCongeView.TypeAbsence.conge)
                .count();

        long totalEmployes = employeService.countEmployesByDepartement(departementId);
        long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);

        double tauxAbsence = totalEmployes > 0 && joursOuvrables > 0
                ? (totalAbsences / (double) (totalEmployes * joursOuvrables)) * 100
                : 0.0;
        double tauxConge = totalEmployes > 0 && joursOuvrables > 0
                ? (totalConges / (double) (totalEmployes * joursOuvrables)) * 100
                : 0.0;

        return new StatistiquesAbsenceDTO(
                totalAbsences, totalConges, tauxAbsence, tauxConge,
                dateDebut + " à " + dateFin, dateDebut, dateFin
        );
    }
    
    @Transactional(readOnly = true)
    public StatistiquesAbsenceDTO getStatistiquesParEmploye(String employeId) {
        Long totalAbsences = absenceCongeRepository.countAbsencesByEmployeId(employeId);
        Long totalConges = absenceCongeRepository.countCongesByEmployeId(employeId);
        
        LocalDate dateDebut = LocalDate.now().minusDays(100);
        LocalDate dateFin = LocalDate.now();
        long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);
        
        Double tauxAbsence = (totalAbsences.doubleValue() / joursOuvrables) * 100;
        Double tauxConge = (totalConges.doubleValue() / joursOuvrables) * 100;

        return new
             StatistiquesAbsenceDTO(totalAbsences, totalConges,
            tauxAbsence, tauxConge, dateDebut + " à " + dateFin, 
            dateDebut, dateFin
        );

        
        // return StatistiquesAbsenceDTO.builder()
        //         .totalAbsences(totalAbsences)
        //         .totalConges(totalConges)
        //         .tauxAbsence(tauxAbsence)
        //         .tauxConge(tauxConge)
        //         .periode("100 derniers jours")
        //         .dateDebut(dateDebut)
        //         .dateFin(dateFin)
        //         .build();
    }
    
    // Méthodes utilitaires
    private AbsenceCongeDTO convertToDTO(AbsenceCongeView view) {
        return new AbsenceCongeDTO( 
            view.getDateAbsence(),
            view.getEmployeId(),
            view.getNomComplet(),
            view.getMatricule(),
            view.getDepartementNom(),
            view.getNomPoste(),
            view.getIdDepartement(), 
            view.getTypeAbsence() 
        );
        // return AbsenceCongeDTO.builder()
        //         .id(view.getId())
        //         .dateAbsence(view.getDateAbsence())
        //         .employeId(view.getEmployeId())
        //         .nomComplet(view.getNomComplet())
        //         .matricule(view.getMatricule())
        //         .departementNom(view.getDepartementNom())
        //         .nomPoste(view.getNomPoste())
        //         .idDepartement(view.getIdDepartement())
        //         .typeAbsence(view.getTypeAbsence())
        //         .build();
    }
    
    private long calculerJoursOuvrables(LocalDate debut, LocalDate fin) {
        long jours = 0;
        LocalDate date = debut;
        
        while (!date.isAfter(fin)) {
            // Exclure weekends (samedi=6, dimanche=7)
            int jourSemaine = date.getDayOfWeek().getValue();
            if (jourSemaine < 6) {
                jours++;
            }
            date = date.plusDays(1);
        }
        return jours;
    }
}
