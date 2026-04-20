package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.CalendrierFerie;
import com.rh.manage.Repository.CalendrierFerieRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CalendrierFerieService {
    
    @Autowired
    private CalendrierFerieRepository calendrierFerieRepository;
    
    // Créer un jour férié
    public CalendrierFerie create(CalendrierFerie calendrierFerie) {
        calendrierFerie.setCreatedAt(LocalDateTime.now());
        calendrierFerie.setEstActif(true);
        return calendrierFerieRepository.save(calendrierFerie);
    }
    
    // Mettre à jour un jour férié
    public CalendrierFerie update(Long id, CalendrierFerie calendrierFerieDetails) {
        CalendrierFerie existing = getById(id);
        existing.setDateFerie(calendrierFerieDetails.getDateFerie());
        existing.setLibelle(calendrierFerieDetails.getLibelle());
        existing.setModifiedAt(LocalDateTime.now());
        existing.setEstActif(calendrierFerieDetails.getEstActif());
        return calendrierFerieRepository.save(existing);
    }
    
    // Supprimer un jour férié
    public void delete(Long id) {
        calendrierFerieRepository.deleteById(id);
    }
    
    // Suppression logique
    public CalendrierFerie deactivate(Long id) {
        CalendrierFerie existing = getById(id);
        existing.setEstActif(false);
        existing.setModifiedAt(LocalDateTime.now());
        return calendrierFerieRepository.save(existing);
    }
    
    // Récupérer par ID
    public CalendrierFerie getById(Long id) {
        Optional<CalendrierFerie> calendrierFerie = calendrierFerieRepository.findById(id);
        return calendrierFerie.orElseThrow(() -> new RuntimeException("Jour férié non trouvé avec l'id: " + id));
    }
    
    // Récupérer tous les jours fériés
    public List<CalendrierFerie> getAll() {
        return calendrierFerieRepository.findAll();
    }
    
    // Récupérer par date
    public List<CalendrierFerie> getByDate(LocalDate date) {
        return calendrierFerieRepository.findByDateFerie(date);
    }
    
    // Récupérer les jours fériés actifs
    public List<CalendrierFerie> getActiveFeries() {
        return calendrierFerieRepository.findByEstActifTrue();
    }
    
    // Récupérer par plage de dates
    public List<CalendrierFerie> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return calendrierFerieRepository.findByDateFerieBetween(startDate, endDate);
    }
    
    // Vérifier si une date est fériée
    public boolean isFerie(LocalDate date) {
        return calendrierFerieRepository.existsByDateFerieAndEstActifTrue(date);
    }
    
    // Récupérer par année
    public List<CalendrierFerie> getFeriesByYear(int year) {
        return calendrierFerieRepository.findFeriesByYear(year);
    }
    
    // Récupérer les jours fériés à venir
    public List<CalendrierFerie> getUpcomingFeries() {
        return calendrierFerieRepository.findUpcomingFeries();
    }
    
    // Recherche par libellé
    public List<CalendrierFerie> searchByLibelle(String libelle) {
        return calendrierFerieRepository.findByLibelleContainingIgnoreCase(libelle);
    }
}
