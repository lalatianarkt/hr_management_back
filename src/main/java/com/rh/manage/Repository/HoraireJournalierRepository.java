package com.rh.manage.Repository;

import com.rh.manage.Model.HoraireJournalier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoraireJournalierRepository extends JpaRepository<HoraireJournalier, String> {

    // Trouver les horaires par statut
    List<HoraireJournalier> findByStatut(Integer statut);
    
    // Trouver les horaires actifs (statut = 1)
    List<HoraireJournalier> findByStatutOrderByCreatedAtDesc(Integer statut);
    
    // Trouver les horaires par jour de travail
    List<HoraireJournalier> findByJourTravailId(Integer jourTravailId);
    
    // Trouver les horaires actifs par jour de travail
    List<HoraireJournalier> findByJourTravailIdAndStatut(Integer jourTravailId, Integer statut);
    
    // Trouver les horaires par type de shift
    List<HoraireJournalier> findByIsShiftJour(Boolean isShiftJour);
    
    // Trouver les horaires actifs par type de shift
    List<HoraireJournalier> findByIsShiftJourAndStatut(Boolean isShiftJour, Integer statut);    
    
}