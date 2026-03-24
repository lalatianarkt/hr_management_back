package com.rh.manage.Repository;

import com.rh.manage.Model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, String> {
    
    // Trouver par nom (exact)
    Optional<Formation> findByNom(String nom);
    
    // Trouver par nom (contient)
    List<Formation> findByNomContainingIgnoreCase(String nom);
    
    // Vérifier si un nom existe déjà
    boolean existsByNom(String nom);
    
    // Trouver par durée minimum
    List<Formation> findByDureeGreaterThanEqual(Integer dureeMin);
    
    // Trouver par durée maximum
    List<Formation> findByDureeLessThanEqual(Integer dureeMax);
    
    // Trouver par coût maximum
    List<Formation> findByCoutLessThanEqual(BigDecimal coutMax);
    
    // Compter le nombre de formations
    long count();
    
    // Recherche personnalisée par nom ou description
    @Query("SELECT f FROM Formation f WHERE " +
           "LOWER(f.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.prerequis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(f.objectifs) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Formation> search(@Param("searchTerm") String searchTerm);
    
    // Trier par nom
    List<Formation> findAllByOrderByNomAsc();
    
    // Trier par coût croissant
    List<Formation> findAllByOrderByCoutAsc();
    
    // Trier par durée décroissante
    List<Formation> findAllByOrderByDureeDesc();
    
    // Formations avec prérequis
    List<Formation> findByPrerequisIsNotNull();
    
    // Formations sans prérequis
    List<Formation> findByPrerequisIsNull();
}