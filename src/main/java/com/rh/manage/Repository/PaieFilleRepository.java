package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.PaieFille;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaieFilleRepository extends JpaRepository<PaieFille, Long> {

    // Trouver toutes les lignes pour une paie spécifique
    List<PaieFille> findByPaieId(String paieId);

    Optional<PaieFille> findByRubrique_CodeAndPaie_Id(String rubriqueCode, String paieId);

    // 1. Récupérer toutes les paieFilles pour une paie où les rubriques sont imposables (estImposable = true)
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND r.estImposable = true " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findImposablesByPaieId(@Param("paieId") String paieId);

    // 2. Récupérer toutes les paieFilles pour une paie où les rubriques sont soumises aux cotisations (estSoumisCotisations = true)
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND r.estSoumisCotisations = true " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findSoumisCotisationsByPaieId(@Param("paieId") String paieId);

    // 3. Récupérer toutes les paieFilles pour une paie où les rubriques sont NON imposables
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "AND r.estImposable = false " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findNonImposablesByPaieId(@Param("paieId") String paieId);

    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND r.estDeductibleIrsa = true " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findDeductiblesIrsaByPaieId(@Param("paieId") String paieId);

    // 4. Récupérer toutes les paieFilles pour une paie où les rubriques sont NON soumises aux cotisations
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND r.estSoumisCotisations = false " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findNonSoumisCotisationsByPaieId(@Param("paieId") String paieId);

    // 1. Récupérer toutes les paieFilles de type GAIN pour une paie spécifique
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "JOIN r.type rt " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND rt.id = 'GAIN' " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findGainsByPaieId(@Param("paieId") String paieId);
    
    // 2. Récupérer toutes les paieFilles de type RETENUE pour une paie spécifique
    @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "JOIN r.type rt " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND rt.id = 'RETENUE' " +
           "ORDER BY r.ordre ASC")
    List<PaieFille> findRetenuesByPaieId(@Param("paieId") String paieId);

     @Query("SELECT pf FROM PaieFille pf " +
           "JOIN pf.rubrique r " +
           "WHERE pf.paie.id = :paieId " +
           "and r.estActif = true " +
           "AND r.ordre < :rubriqueOrdre " +
           "ORDER BY r.ordre DESC " +
           "LIMIT 1")
    Optional<PaieFille> findPreviousPaieFilleByOrder(
            @Param("paieId") String paieId, 
            @Param("rubriqueOrdre") Integer rubriqueOrdre);

    //. Trouver la paieFille précédente (par ordre de rubrique) pour une paie donnée
//     @Query("SELECT pf FROM PaieFille pf " +
//        "JOIN pf.rubrique r " +
//        "WHERE pf.paie.id = :paieId " +
//        "AND r.ordre = :rubriqueOrdre - 1")
//        Optional<PaieFille> findPreviousPaieFilleByOrder(
//               @Param("paieId") String paieId, 
//               @Param("rubriqueOrdre") Integer rubriqueOrdre);
    
    // Trouver toutes les lignes pour une rubrique spécifique
    List<PaieFille> findByRubriqueId(String rubriqueId);
    
    // Trouver une ligne spécifique par paie et rubrique
    Optional<PaieFille> findByPaieIdAndRubriqueId(String paieId, String rubriqueId);
    
    // Supprimer toutes les lignes pour une paie
    void deleteByPaieId(String paieId);
    
    // Compter le nombre de lignes pour une paie
    long countByPaieId(String paieId);
    
    // Récupérer avec la rubrique (JOIN FETCH pour éviter N+1)
    @Query("SELECT pf FROM PaieFille pf JOIN FETCH pf.rubrique WHERE pf.paie.id = :paieId and pf.rubrique.estActif = true")
    List<PaieFille> findByPaieIdWithRubrique(@Param("paieId") String paieId);
    
    // Calculer le total des montants pour une paie
    @Query("SELECT COALESCE(SUM(pf.montant), 0) FROM PaieFille pf WHERE pf.paie.id = :paieId")
    BigDecimal sumMontantByPaieId(@Param("paieId") String paieId);
}
