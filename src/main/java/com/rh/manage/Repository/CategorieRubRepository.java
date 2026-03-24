package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.CategorieRub;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRubRepository extends JpaRepository<CategorieRub, String> {
    
    // Méthodes de base (conservées)
    Optional<CategorieRub> findByLibelle(String libelle);
    Optional<CategorieRub> findByLibelleIgnoreCase(String libelle);
    List<CategorieRub> findAllByOrderByLibelleAsc();
    List<CategorieRub> findAllByOrderByIdAsc();
    List<CategorieRub> findByLibelleContainingIgnoreCase(String libelle);
    boolean existsByLibelle(String libelle);
    boolean existsByLibelleIgnoreCase(String libelle);
    
    // Recherche avancée par mot-clé
    @Query("SELECT cr FROM CategorieRub cr WHERE " +
           "LOWER(cr.libelle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CategorieRub> searchByKeyword(@Param("keyword") String keyword);
    
    // Récupérer les catégories utilisées dans les rubriques actives (Version native)
    @Query(value = "SELECT DISTINCT cr.* FROM categorie_rub cr " +
           "INNER JOIN rubrique_paie rp ON cr.id = rp.id_categorie " +
           "WHERE rp.est_actif = true", nativeQuery = true)
    List<CategorieRub> findUsedCategories();
    
    // Compter le nombre de rubriques par catégorie (Version native)
    @Query(value = "SELECT cr.libelle, COUNT(rp.id) as count " +
           "FROM categorie_rub cr " +
           "LEFT JOIN rubrique_paie rp ON cr.id = rp.id_categorie " +
           "GROUP BY cr.id, cr.libelle " +
           "ORDER BY COUNT(rp.id) DESC", nativeQuery = true)
    List<Object[]> countRubriquesByCategorie();
    
    // Obtenir les catégories avec statistiques d'utilisation (Version native)
    @Query(value = "SELECT cr.*, COUNT(rp.id) as usage_count " +
           "FROM categorie_rub cr " +
           "LEFT JOIN rubrique_paie rp ON cr.id = rp.id_categorie " +
           "GROUP BY cr.id " +
           "ORDER BY COUNT(rp.id) DESC", nativeQuery = true)
    List<Object[]> getCategoriesWithUsageStats();
}