package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.ModePaiement;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModePaiementRepository extends JpaRepository<ModePaiement, String> {
    
    List<ModePaiement> findByEmployeId(String employeId);
    
    List<ModePaiement> findByEmployeIdAndEstActifTrue(String employeId);
    
    Optional<ModePaiement> findByEmployeIdAndEstParDefautTrue(String employeId);
    
    List<ModePaiement> findByTypePaiementId(String typePaiementId);
    
    @Query("SELECT mp FROM ModePaiement mp WHERE mp.employe.id = :employeId AND mp.estActif = true")
    List<ModePaiement> findActiveModesByEmploye(@Param("employeId") String employeId);
    
    @Query("SELECT CASE WHEN COUNT(mp) > 0 THEN true ELSE false END FROM ModePaiement mp " +
           "WHERE mp.employe.id = :employeId AND mp.estParDefaut = true AND mp.id != :excludeId")
    boolean existsOtherDefaultMode(@Param("employeId") String employeId, @Param("excludeId") String excludeId);
    
    @Modifying
    @Query("UPDATE ModePaiement mp SET mp.estParDefaut = false " +
           "WHERE mp.employe.id = :employeId AND mp.id != :excludeId")
    void removeDefaultFromOtherModes(@Param("employeId") String employeId, @Param("excludeId") String excludeId);
    
    boolean existsByNumeroCompteAndEmployeId(String numeroCompte, String employeId);
}
