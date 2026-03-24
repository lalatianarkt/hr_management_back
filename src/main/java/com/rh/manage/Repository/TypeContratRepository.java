package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypeContrat;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeContratRepository extends JpaRepository<TypeContrat, String> {
    
    // Trouver tous les types de contrats ordonnés par intitulé
    List<TypeContrat> findAllByOrderByIntitule();
    
    // Trouver un type de contrat par son intitulé exact
    Optional<TypeContrat> findByIntitule(String intitule);
    
    // Rechercher des types de contrats par intitulé (recherche partielle insensible à la casse)
    List<TypeContrat> findByIntituleContainingIgnoreCase(String intitule);
    
    // Rechercher des types de contrats par description (recherche partielle)
    List<TypeContrat> findByDescriptionContainingIgnoreCase(String description);
    
    // Vérifier si un type de contrat existe par intitulé
    boolean existsByIntitule(String intitule);
    
    // Compter le nombre de types de contrats
    long count();
    
    // Trouver les types de contrats actifs (ayant des contrats associés)
    // @Query("SELECT DISTINCT tc FROM TypeContrat tc JOIN tc.contrats c WHERE c IS NOT NULL")
    // List<TypeContrat> findTypesContratAvecContrats();
    
    // Recherche avancée avec plusieurs critères
    // @Query("SELECT tc FROM TypeContrat tc WHERE " +
    //        "LOWER(tc.intitule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    //        "LOWER(tc.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    // List<TypeContrat> searchTypeContrats(@Param("searchTerm") String searchTerm);
    
    // // Trouver les types de contrats les plus utilisés
    // @Query("SELECT tc, COUNT(c) as usageCount FROM TypeContrat tc LEFT JOIN tc.contrats c " +
    //        "GROUP BY tc ORDER BY usageCount DESC")
    // List<Object[]> findTypesContratPlusUtilises();
}
