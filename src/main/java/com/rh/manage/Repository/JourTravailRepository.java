package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.JourTravail;

import java.util.List;
import java.util.Optional;

@Repository
public interface JourTravailRepository extends JpaRepository<JourTravail, Long> {
    
    // Trouver par code jour
    Optional<JourTravail> findByCodeJour(Integer codeJour);
    
    // Trouver par nom jour
    Optional<JourTravail> findByNomJour(String nomJour);
    
    // Trouver par statut
    List<JourTravail> findByStatut(Integer statut);
    
    // Trouver par est_weekend
    List<JourTravail> findByEstWeekend(Integer estWeekend);
    
    // Trouver par statut et est_weekend
    List<JourTravail> findByStatutAndEstWeekend(Integer statut, Integer estWeekend);
    
    // Vérifier l'existence par code jour
    boolean existsByCodeJour(Integer codeJour);
    
    // Vérifier l'existence par nom jour
    boolean existsByNomJour(String nomJour);
    
    // Trouver tous triés par code jour
    List<JourTravail> findAllByOrderByCodeJourAsc();
    
    // Trouver les jours actifs (statut = 1)
    List<JourTravail> findByStatutOrderByCodeJourAsc(Integer statut);
}