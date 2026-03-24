package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.RubriquePaie;
import com.rh.manage.Model.CategorieRub;
import com.rh.manage.Model.RubriqueType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RubriquePaieRepository extends JpaRepository<RubriquePaie, String> {
    
    // Méthodes de base (sans problème)
    Optional<RubriquePaie> findByCode(String code);
    Optional<RubriquePaie> findByCodeIgnoreCase(String code);
    List<RubriquePaie> findByEstActifTrue();
    List<RubriquePaie> findByEstActifOrderByOrdreAsc(Boolean estActif);
    
    // CORRIGÉ : Utiliser des requêtes JPQL pour accéder aux IDs des relations
    @Query("SELECT rp FROM RubriquePaie rp WHERE rp.categorie.id = :categorieId")
    List<RubriquePaie> findByCategorieId(@Param("categorieId") String categorieId);
    
    @Query("SELECT rp FROM RubriquePaie rp WHERE rp.type.id = :typeId")
    List<RubriquePaie> findByTypeId(@Param("typeId") String typeId);
    
    List<RubriquePaie> findAllByOrderByOrdreAsc();
    Page<RubriquePaie> findAllByOrderByOrdreAsc(Pageable pageable);

    // Tous les paramètres sont optionnels (peuvent être NULL ou vides)
    @Query("SELECT rp FROM RubriquePaie rp WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "   LOWER(rp.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "   LOWER(rp.libelle) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:typeId IS NULL OR :typeId = '' OR rp.type.id = :typeId) " +
           "AND (:estImposable IS NULL OR rp.estImposable = :estImposable) " +
           "AND (:estSoumisCotisations IS NULL OR rp.estSoumisCotisations = :estSoumisCotisations) " +
           "AND (:estDeductibleIrsa is null or rp.estDeductibleIrsa =: estDeductibleIrsa)" +
           "AND (:estActif IS NULL OR rp.estActif = :estActif)")
    Page<RubriquePaie> searchByCriteria(
            @Param("search") String search,           // Recherche dans code ET libellé
            @Param("typeId") String typeId,           // ID du type (exact)
            @Param("estImposable") Boolean estImposable,          // Booléen
            @Param("estSoumisCotisations") Boolean estSoumisCotisations, // Booléen
            @Param("estDeductibleIrsa") Boolean estDeductibleIrsa,
            @Param("estActif") Boolean estActif,                  // Booléen
            Pageable pageable);

        List<RubriquePaie> findByEstActifTrueOrderByOrdreAsc();
        List<RubriquePaie> findByEstActifAndEstDeductibleIrsa(Boolean estActif, Boolean estDeductibleIrsa);
        List<RubriquePaie> findByOrdreBetween(Integer min, Integer max);
        List<RubriquePaie> findByEstImposable(Boolean estImposable);
        List<RubriquePaie> findByEstSoumisCotisations(Boolean estSoumisCotisations);
        boolean existsByCode(String code);
        boolean existsByCodeIgnoreCase(String code);
        List<RubriquePaie> findByLibelleContainingIgnoreCase(String libelle);
        List<RubriquePaie> findByCommentaireContainingIgnoreCase(String commentaire);
    
    // Recherche avancée
    @Query("SELECT rp FROM RubriquePaie rp WHERE " +
           "LOWER(rp.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(rp.libelle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(rp.commentaire) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<RubriquePaie> searchByKeyword(@Param("keyword") String keyword);
    
    // Trouver le prochain ordre disponible
    @Query("SELECT COALESCE(MAX(rp.ordre), 0) + 1 FROM RubriquePaie rp")
    Integer findNextOrdre();
    
    // Trouver les rubriques par type et statut
    @Query("SELECT rp FROM RubriquePaie rp WHERE rp.type.id = :typeId AND rp.estActif = :estActif")
    List<RubriquePaie> findByTypeIdAndEstActif(@Param("typeId") String typeId, 
                                               @Param("estActif") Boolean estActif);
    
    // CORRIGÉ : Utiliser des requêtes JPQL orientées objet
    // Statistiques par type
    @Query("SELECT rp.type.libelle, COUNT(rp) FROM RubriquePaie rp " +
           "WHERE rp.type IS NOT NULL " +
           "GROUP BY rp.type.id, rp.type.libelle " +
           "ORDER BY COUNT(rp) DESC")
    List<Object[]> countByType();
    
    // Statistiques par catégorie
    @Query("SELECT rp.categorie.libelle, COUNT(rp) FROM RubriquePaie rp " +
           "WHERE rp.categorie IS NOT NULL " +
           "GROUP BY rp.categorie.id, rp.categorie.libelle " +
           "ORDER BY COUNT(rp) DESC")
    List<Object[]> countByCategorie();
    
    // Compter les rubriques actives par catégorie
    @Query("SELECT rp.categorie.libelle, COUNT(rp) FROM RubriquePaie rp " +
           "WHERE rp.estActif = true AND rp.categorie IS NOT NULL " +
           "GROUP BY rp.categorie.id, rp.categorie.libelle " +
           "ORDER BY COUNT(rp) DESC")
    List<Object[]> countActiveByCategorie();
}