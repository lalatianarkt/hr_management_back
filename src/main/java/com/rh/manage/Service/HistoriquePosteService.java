package com.rh.manage.Service;

import com.rh.manage.Model.HistoriquePoste;
import java.time.LocalDate;
import java.util.List;

public interface HistoriquePosteService {
    
    // CRUD basique
    HistoriquePoste save(HistoriquePoste historiquePoste);
    List<HistoriquePoste> findAll();
    HistoriquePoste findById(Long id);
    HistoriquePoste update(Long id, HistoriquePoste historiquePoste);
    void deleteById(Long id);
    
    // Méthodes métier
    List<HistoriquePoste> findByEmployeId(String employeId);
    HistoriquePoste findCurrentPosteByEmployeId(String employeId);
    List<HistoriquePoste> findByPosteId(String posteId);
    List<HistoriquePoste> findByPeriod(LocalDate startDate, LocalDate endDate);
    
    // Gestion des changements de poste
    HistoriquePoste createChangementPoste(String employeId, String posteId, LocalDate dateDebut);
    void cloturerHistoriqueActuel(String employeId, LocalDate dateFin);
    
    // Statistiques
    Long countChangementsByEmploye(String employeId);
    List<HistoriquePoste> findPostesActuels();
    boolean hasEmployeAlreadyHadPoste(String employeId, String posteId);
}