package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.PointageFille;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointageFilleRepository extends JpaRepository<PointageFille, String> {
    
    // Trouver par pointage parent
    List<PointageFille> findByPointageId(String pointageId);
    
    // Trouver par pointage parent ordonné par date
    List<PointageFille> findByPointageIdOrderByDateHeurePointageAsc(String pointageId);
    
    // Trouver par type d'action
    List<PointageFille> findByTypeAction(String typeAction);
    
    // Trouver par source
    List<PointageFille> findBySource(String source);
    
    // Trouver par période
    List<PointageFille> findByDateHeurePointageBetween(LocalDateTime start, LocalDateTime end);
    
    // Trouver par pointage et type d'action
    List<PointageFille> findByPointageIdAndTypeAction(String pointageId, String typeAction);
    
    // Trouver le premier pointage d'un pointage parent
    PointageFille findFirstByPointageIdOrderByDateHeurePointageAsc(String pointageId);
    
    // Trouver le dernier pointage d'un pointage parent
    PointageFille findFirstByPointageIdOrderByDateHeurePointageDesc(String pointageId);
    
    // Compter les pointages filles d'un pointage parent
    long countByPointageId(String pointageId);
    
    // Vérifier s'il existe des pointages filles pour un pointage
    boolean existsByPointageId(String pointageId);
    
    // Trouver les pointages filles par employé (via pointage parent)
    @Query("SELECT pf FROM PointageFille pf JOIN pf.pointage p WHERE p.employe.id = :employeId")
    List<PointageFille> findByEmployeId(@Param("employeId") String employeId);
    
    // Trouver les pointages filles d'un employé par période
    @Query("SELECT pf FROM PointageFille pf JOIN pf.pointage p WHERE p.employe.id = :employeId AND pf.dateHeurePointage BETWEEN :start AND :end")
    List<PointageFille> findByEmployeIdAndDateBetween(
            @Param("employeId") String employeId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
    
    // Trouver les entrées non appariées avec des sorties
    @Query("SELECT pf FROM PointageFille pf WHERE pf.typeAction = 'IN' AND NOT EXISTS (" +
           "SELECT pf2 FROM PointageFille pf2 WHERE pf2.pointage.id = pf.pointage.id AND pf2.typeAction = 'OUT')")
    List<PointageFille> findEntreesSansSortie();
}
