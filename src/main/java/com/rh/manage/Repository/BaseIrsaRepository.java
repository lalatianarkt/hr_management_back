package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.BaseIrsa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BaseIrsaRepository extends JpaRepository<BaseIrsa, String> {
    
    // Trouver par numéro de tranche
    Optional<BaseIrsa> findByNumTranche(Integer numTranche);
    
    // Trouver toutes les tranches triées par numéro
    List<BaseIrsa> findAllByOrderByNumTrancheAsc();
    
    // Trouver la tranche pour un montant donné
    @Query("SELECT b FROM BaseIrsa b WHERE " +
           "(:montant >= b.trancheMin OR b.trancheMin IS NULL) AND " +
           "(:montant < b.trancheMax OR b.trancheMax IS NULL) " +
           "ORDER BY b.numTranche ASC")
    Optional<BaseIrsa> findTrancheForMontant(@Param("montant") BigDecimal montant);
    
    // Trouver la première tranche (la plus basse)
    @Query("SELECT b FROM BaseIrsa b WHERE b.numTranche = " +
           "(SELECT MIN(b2.numTranche) FROM BaseIrsa b2)")
    Optional<BaseIrsa> findFirstTranche();
    
    // Trouver la dernière tranche (la plus haute)
    @Query("SELECT b FROM BaseIrsa b WHERE b.numTranche = " +
           "(SELECT MAX(b2.numTranche) FROM BaseIrsa b2)")
    Optional<BaseIrsa> findLastTranche();
    
    // Vérifier l'existence d'un numéro de tranche
    boolean existsByNumTranche(Integer numTranche);
    
    // Trouver les tranches avec un taux minimum
    List<BaseIrsa> findByTauxGreaterThanEqual(BigDecimal tauxMin);
    
    // Trouver les tranches avec un taux maximum
    List<BaseIrsa> findByTauxLessThanEqual(BigDecimal tauxMax);
    
    // Trouver par plage de montants
    @Query("SELECT b FROM BaseIrsa b WHERE " +
           "(b.trancheMin BETWEEN :min AND :max) OR " +
           "(b.trancheMax BETWEEN :min AND :max) " +
           "ORDER BY b.numTranche ASC")
    List<BaseIrsa> findByPlageMontants(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    
    // Calculer le nombre de tranches
    @Query("SELECT COUNT(b) FROM BaseIrsa b")
    Long countTranches();
    
    // Trouver la tranche suivante
    @Query("SELECT b FROM BaseIrsa b WHERE b.numTranche > :numTranche ORDER BY b.numTranche ASC LIMIT 1")
    Optional<BaseIrsa> findNextTranche(@Param("numTranche") Integer numTranche);
    
    // Trouver la tranche précédente
    @Query("SELECT b FROM BaseIrsa b WHERE b.numTranche < :numTranche ORDER BY b.numTranche DESC LIMIT 1")
    Optional<BaseIrsa> findPreviousTranche(@Param("numTranche") Integer numTranche);
}
