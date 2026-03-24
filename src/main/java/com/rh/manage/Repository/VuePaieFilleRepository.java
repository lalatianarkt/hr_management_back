package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.VuePaieFille;
import com.rh.manage.Model.VuePaieFilleId;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VuePaieFilleRepository extends JpaRepository<VuePaieFille, VuePaieFilleId> {
    
    // Trouver toutes les lignes d'une paie
    List<VuePaieFille> findByIdPaieOrderByOrdre(String idPaie);
    
    // Trouver par code de rubrique
    List<VuePaieFille> findByCode(String code);
    
    // Trouver par paie et code
    VuePaieFille findByIdPaieAndCode(String idPaie, String code);
    
    // Trouver les rubriques avec montant supérieur à
    List<VuePaieFille> findByMontantGreaterThan(BigDecimal montant);
    
    // Trouver les rubriques avec taux supérieur à
    List<VuePaieFille> findByTauxGreaterThan(BigDecimal taux);
    
    // Trouver les rubriques par plafond mensuel
    // List<VuePaieFille> findByPlafondMensuel(BigDecimal plafondMensuel);
    
    // Calculer le total des montants pour une paie
    @Query("SELECT COALESCE(SUM(v.montant), 0) FROM VuePaieFille v WHERE v.idPaie = :idPaie")
    BigDecimal sumMontantByIdPaie(@Param("idPaie") String idPaie);
    
    // Calculer le total des montants par code de rubrique
    @Query("SELECT v.code, COALESCE(SUM(v.montant), 0) FROM VuePaieFille v GROUP BY v.code ORDER BY SUM(v.montant) DESC")
    List<Object[]> sumMontantByCode();
    
    // Obtenir les statistiques par paie
    @Query("SELECT v.idPaie, COUNT(v), COALESCE(SUM(v.montant), 0), COALESCE(AVG(v.taux), 0) " +
           "FROM VuePaieFille v GROUP BY v.idPaie")
    List<Object[]> getStatsByPaie();
    
    // Rechercher les rubriques par plage de montant
    @Query("SELECT v FROM VuePaieFille v WHERE v.montant BETWEEN :min AND :max")
    List<VuePaieFille> findByMontantBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    
    // Obtenir les codes distincts
    @Query("SELECT DISTINCT v.code FROM VuePaieFille v ORDER BY v.code")
    List<String> findDistinctCodes();
    
    // Obtenir les paies distinctes
    @Query("SELECT DISTINCT v.idPaie FROM VuePaieFille v ORDER BY v.idPaie")
    List<String> findDistinctIdPaies();
    
    // Calculer les totaux par type (selon le code)
    @Query("SELECT SUBSTRING(v.code, 1, 2), COUNT(v), COALESCE(SUM(v.montant), 0) " +
           "FROM VuePaieFille v GROUP BY SUBSTRING(v.code, 1, 2)")
    List<Object[]> getTotalsByCodePrefix();
    
    // Trouver les rubriques sans plafond
    // @Query("SELECT v FROM VuePaieFille v WHERE v.plafondMensuel IS NULL OR v.plafondMensuel = 0")
    // List<VuePaieFille> findWithoutPlafond();
    
    // Vérifier si une rubrique dépasse son plafond mensuel
    // @Query("SELECT v FROM VuePaieFille v WHERE v.plafondMensuel IS NOT NULL AND v.plafondMensuel > 0 " +
    //        "AND v.montant > v.plafondMensuel")
    // List<VuePaieFille> findExceedingPlafondMensuel();
    
    // Vérifier si une rubrique dépasse son plafond annuel
    // @Query("SELECT v FROM VuePaieFille v WHERE v.plafondAnnuel IS NOT NULL AND v.plafondAnnuel > 0 " +
    //        "AND v.montant > v.plafondAnnuel")
    // List<VuePaieFille> findExceedingPlafondAnnuel();
}
