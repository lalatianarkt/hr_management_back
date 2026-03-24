package com.rh.manage.Repository;

import com.rh.manage.Model.SessionFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionFormationRepository extends JpaRepository<SessionFormation, String> {
    
    // Trouver les sessions par formation (via la relation)
    @Query("SELECT s FROM SessionFormation s WHERE s.formation.id = :formationId")
    List<SessionFormation> findByFormationId(@Param("formationId") String formationId);
    
    // Trouver les sessions par formateur (via la relation)
    @Query("SELECT s FROM SessionFormation s WHERE s.formateur.id = :formateurId")
    List<SessionFormation> findByFormateurId(@Param("formateurId") String formateurId);
    
    // Trouver les sessions par statut
    List<SessionFormation> findByStatut(Integer statut);
    
    // Trouver les sessions par lieu
    List<SessionFormation> findByLieuContainingIgnoreCase(String lieu);
    
    // Trouver les sessions entre deux dates
    List<SessionFormation> findByDateDebutBetween(LocalDate startDate, LocalDate endDate);
    
    // Trouver les sessions à venir (date début après aujourd'hui)
    List<SessionFormation> findByDateDebutAfter(LocalDate date);
    
    // Trouver les sessions passées (date début avant aujourd'hui)
    List<SessionFormation> findByDateDebutBefore(LocalDate date);
    
    // Trouver les sessions en cours (date début <= aujourd'hui et statut en cours)
    @Query("SELECT s FROM SessionFormation s WHERE s.dateDebut <= :today AND s.statut = 2")
    List<SessionFormation> findSessionsEnCours(@Param("today") LocalDate today);
    
    // Compter les sessions par statut
    @Query("SELECT s.statut, COUNT(s) FROM SessionFormation s GROUP BY s.statut")
    List<Object[]> countSessionsByStatut();
    
    // Trouver les sessions avec places disponibles
    @Query("SELECT s FROM SessionFormation s WHERE (s.placeMax IS NULL OR s.placeMax > 0) AND s.statut IN (1, 2)")
    List<SessionFormation> findSessionsWithAvailablePlaces();
    
    // Vérifier si un formateur a des sessions à une certaine période
    @Query("SELECT COUNT(s) > 0 FROM SessionFormation s WHERE s.formateur.id = :formateurId " +
           "AND s.dateDebut <= :dateFin AND s.dateFin >= :dateDebut")
    boolean isFormateurOccupe(
        @Param("formateurId") String formateurId,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") String dateFin
    );
    
    // Trouver les sessions avec les détails de formation et formateur (eager loading)
    @Query("SELECT s FROM SessionFormation s LEFT JOIN FETCH s.formation LEFT JOIN FETCH s.formateur")
    List<SessionFormation> findAllWithDetails();
    
    // Trouver les sessions par nom de formation
    @Query("SELECT s FROM SessionFormation s WHERE s.formation.nom LIKE %:nomFormation%")
    List<SessionFormation> findByNomFormationContaining(@Param("nomFormation") String nomFormation);
    
    // Trouver les sessions par nom de formateur
    @Query("SELECT s FROM SessionFormation s WHERE s.formateur.nom LIKE %:nomFormateur%")
    List<SessionFormation> findByNomFormateurContaining(@Param("nomFormateur") String nomFormateur);
    
    // Trouver les sessions créées après une certaine date
    List<SessionFormation> findByCreatedAtAfter(LocalDateTime date);
    
    // Statistiques mensuelles
    @Query("SELECT YEAR(s.dateDebut), MONTH(s.dateDebut), COUNT(s) FROM SessionFormation s GROUP BY YEAR(s.dateDebut), MONTH(s.dateDebut)")
    List<Object[]> findMonthlyStatistics();
}