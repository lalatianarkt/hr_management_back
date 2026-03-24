package com.rh.manage.Repository;

import com.rh.manage.Model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, String> {
    
    // Trouver par nom (exact)
    Optional<Competence> findByNom(String nom);
    
    // Trouver par nom (contient)
    List<Competence> findByNomContainingIgnoreCase(String nom);
    
    // Vérifier si un nom existe déjà
    boolean existsByNom(String nom);
    
    // Trouver par catégorie
    List<Competence> findByCategorieId(String categorieId);
    
    List<Competence> findByCategorieNom(String categorieNom);
    
    // Compter le nombre de compétences
    long count();
    
    // Compter par catégorie
    long countByCategorieId(String categorieId);
    
    // Recherche personnalisée par nom ou description
    @Query("SELECT c FROM Competence c WHERE " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Competence> searchByNomOrDescription(@Param("searchTerm") String searchTerm);
    
    // Recherche par nom ou description avec catégorie
    @Query("SELECT c FROM Competence c WHERE " +
           "(LOWER(c.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "c.categorie.id = :categorieId")
    List<Competence> searchByNomOrDescriptionAndCategorie(@Param("searchTerm") String searchTerm, 
                                                          @Param("categorieId") String categorieId);
    
    // Trier par nom
    List<Competence> findAllByOrderByNomAsc();
    
    // Trouver les compétences sans catégorie
    List<Competence> findByCategorieIsNull();
}