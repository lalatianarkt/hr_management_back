package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.RubriqueType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RubriqueTypeRepository extends JpaRepository<RubriqueType, String> {
    
    // Trouver par libellé (exact)
    Optional<RubriqueType> findByLibelle(String libelle);
    
    // Trouver par libellé (ignorer la casse)
    Optional<RubriqueType> findByLibelleIgnoreCase(String libelle);
    
    // Rechercher par libellé contenant
    List<RubriqueType> findByLibelleContainingIgnoreCase(String libelle);
    
    // Rechercher par description contenant
    List<RubriqueType> findByDescriptionContainingIgnoreCase(String description);
    
    // Trouver tous triés par libellé
    List<RubriqueType> findAllByOrderByLibelleAsc();
    
    // Trouver tous triés par ID
    List<RubriqueType> findAllByOrderByIdAsc();
    
    // Vérifier l'existence par libellé
    boolean existsByLibelle(String libelle);
    
    // Vérifier l'existence par libellé (ignorer casse)
    boolean existsByLibelleIgnoreCase(String libelle);
    
    // Compter le nombre de types
    long count();
    
    // Requête personnalisée pour recherche avancée
    @Query("SELECT rt FROM RubriqueType rt WHERE " +
           "LOWER(rt.libelle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(rt.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RubriqueType> searchByKeyword(@Param("keyword") String keyword);
    
    // CORRIGÉ : Version avec le package complet
    @Query("SELECT DISTINCT rt FROM RubriqueType rt " +
           "JOIN com.rh.manage.Model.RubriquePaie rp ON rt.id = rp.type.id " +
           "WHERE rp.estActif = true")
    List<RubriqueType> findUsedTypes();
    
    // OU Version alternative 1 (sous-requête)
    // @Query("SELECT DISTINCT rt FROM RubriqueType rt " +
    //        "WHERE rt.id IN (SELECT DISTINCT rp.type.id FROM com.rh.manage.Model.RubriquePaie rp WHERE rp.estActif = true)")
    // List<RubriqueType> findUsedTypes();
    
    // OU Version alternative 2 (native query - plus fiable)
    // @Query(value = "SELECT DISTINCT rt.* FROM rubrique_type rt " +
    //        "INNER JOIN rubrique_paie rp ON rt.id = rp.id_type " +
    //        "WHERE rp.est_actif = true", nativeQuery = true)
    // List<RubriqueType> findUsedTypes();
}