package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.PreparationExportEmploye;

import java.util.Optional;

@Repository
public interface PreparationExportEmployeRepository extends JpaRepository<PreparationExportEmploye, Long> {
    @Query("SELECT p FROM PreparationExportEmploye p ORDER BY p.id DESC")
    Optional<PreparationExportEmploye> findActiveConfiguration();
    
    @Query("SELECT COUNT(p) > 0 FROM PreparationExportEmploye p")
    boolean existsConfiguration();
}
