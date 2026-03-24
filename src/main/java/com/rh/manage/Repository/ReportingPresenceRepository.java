package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.ReportingPresence;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportingPresenceRepository extends JpaRepository<ReportingPresence, String> {
    
    // Recherche par département
    List<ReportingPresence> findByIdDepartement(String idDepartement);
    
    // Recherche par nom (LIKE)
    List<ReportingPresence> findByNomCompletContainingIgnoreCase(String nom);
    
    // Recherche par matricule
    ReportingPresence findByMatricule(String matricule);
    
    // Recherche avec plusieurs critères - VERSION CORRIGÉE
    @Query("SELECT rp FROM ReportingPresence rp WHERE " +
        "(:departementId IS NULL OR rp.idDepartement = :departementId) AND " +
        "(:nom IS NULL OR rp.nomComplet LIKE %:nom%) AND " +
        "(:minConges IS NULL OR rp.nombreCongesTermines >= :minConges) AND " +
        "(:maxConges IS NULL OR rp.nombreCongesTermines <= :maxConges)")
    List<ReportingPresence> findWithFilters(
            @Param("departementId") String departementId,
            @Param("nom") String nom,
            @Param("minConges") Integer minConges,
            @Param("maxConges") Integer maxConges);

    
    
    // Statistiques globales
    @Query("SELECT COUNT(rp) FROM ReportingPresence rp")
    Long countTotalEmployes();
    
    @Query("SELECT SUM(rp.totalHeureTravaillee) FROM ReportingPresence rp")
    Long sumTotalHeuresTravaillees();
    
    @Query("SELECT SUM(rp.totalJoursCongesTermines) FROM ReportingPresence rp")
    Long sumTotalJoursConges();
    
    // Statistiques par département
    @Query("SELECT rp.departementNom, " +
           "COUNT(rp) as nbEmployes, " +
           "SUM(rp.totalHeureTravaillee) as totalHeures, " +
           "AVG(rp.totalHeureTravaillee) as moyenneHeures, " +
           "SUM(rp.totalJoursCongesTermines) as totalConges, " +
           "AVG(rp.totalJoursCongesTermines) as moyenneConges " +
           "FROM ReportingPresence rp " +
           "GROUP BY rp.departementNom, rp.idDepartement " +
           "ORDER BY rp.departementNom")
    List<Object[]> getStatsParDepartement();
    
    // Top employés par heures travaillées
    @Query("SELECT rp FROM ReportingPresence rp ORDER BY rp.totalHeureTravaillee DESC")
    List<ReportingPresence> findTopByHeuresTravaillees();
    
    // Employés avec plus de X jours de congés
    List<ReportingPresence> findByNombreCongesTerminesGreaterThan(Integer nombre);
}
