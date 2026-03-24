// ManagerRepository.java
package com.rh.manage.Repository;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    Optional<Manager> findById(String id);
    
    List<Manager> findByStatut(Integer statut);
    
    @Modifying
    @Query("UPDATE Manager m SET m.statut = :statut, m.dateFin = :dateFin, m.modifiedAt = :modifiedAt WHERE m.id = :id")
    int archiverManager(@Param("id") String id, 
                       @Param("statut") Integer statut, 
                       @Param("dateFin") LocalDate dateFin,
                       @Param("modifiedAt") LocalDateTime modifiedAt);
    
    @Query("SELECT COUNT(m) > 0 FROM Manager m WHERE m.id = :id AND m.statut = 0")
    boolean existsAndIsActif(@Param("id") String id);
    
    List<Manager> findByDateFinIsNull();
    
    @Modifying
    @Query("UPDATE Manager m SET m.statut = :statut, m.modifiedAt = :modifiedAt WHERE m.id = :id")
    int updateStatut(@Param("id") String id, 
                    @Param("statut") Integer statut, 
                    @Param("modifiedAt") LocalDateTime modifiedAt);

    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.manager.id = :managerId AND ip.employe.statut = 0")
    List<InfosProfessionnelles> findEmployesByManager(@Param("managerId") String managerId);
    
    @Modifying
    @Query("UPDATE InfosProfessionnelles ip SET ip.manager = null WHERE ip.manager.id = :managerId")
    int removeManagerFromEmployees(@Param("managerId") String managerId);

    @Query("SELECT m FROM Manager m WHERE m.departement.id = :departementId AND m.statut = 0 AND m.dateFin IS NULL")
    Optional<Manager> findManagerActuelByDepartement(@Param("departementId") String departementId);

    Optional<Manager> findByEmployeAndStatut(Employe employe, Integer statut);
    
    @Query("SELECT m FROM Manager m WHERE m.employe.id = :idEmploye and m.statut = 0")
    Optional<Manager> findByEmployeId(@Param("idEmploye") String idEmploye);

}