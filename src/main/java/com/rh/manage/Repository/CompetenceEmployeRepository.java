package com.rh.manage.Repository;

import com.rh.manage.Model.CompetenceEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompetenceEmployeRepository extends JpaRepository<CompetenceEmploye, String> {
    
    // Trouver par employé
    List<CompetenceEmploye> findByEmployeId(String employeId);
    
    // Trouver par compétence
    List<CompetenceEmploye> findByCompetenceId(String competenceId);
    
    // Trouver une association spécifique
    Optional<CompetenceEmploye> findByEmployeIdAndCompetenceId(String employeId, String competenceId);
    
    // Vérifier si une association existe
    boolean existsByEmployeIdAndCompetenceId(String employeId, String competenceId);
    
    // Trouver par niveau minimum
    List<CompetenceEmploye> findByNiveauGreaterThanEqual(Integer niveauMin);
    
    // Trouver par niveau maximum
    List<CompetenceEmploye> findByNiveauLessThanEqual(Integer niveauMax);
    
    // Trouver par date d'acquisition
    List<CompetenceEmploye> findByDateAcquisition(LocalDate dateAcquisition);
    
    // Trouver les compétences acquises après une date
    List<CompetenceEmploye> findByDateAcquisitionAfter(LocalDate date);
    
    // Trouver les compétences acquises avant une date
    List<CompetenceEmploye> findByDateAcquisitionBefore(LocalDate date);
    
    // Compter le nombre de compétences par employé
    long countByEmployeId(String employeId);
    
    // Compter le nombre d'employés par compétence
    long countByCompetenceId(String competenceId);
    
    // Moyenne des niveaux par employé
    @Query("SELECT AVG(ce.niveau) FROM CompetenceEmploye ce WHERE ce.employe.id = :employeId")
    Double findAverageNiveauByEmployeId(@Param("employeId") String employeId);
    
    // Moyenne des niveaux par compétence
    @Query("SELECT AVG(ce.niveau) FROM CompetenceEmploye ce WHERE ce.competence.id = :competenceId")
    Double findAverageNiveauByCompetenceId(@Param("competenceId") String competenceId);
    
    // Trouver les compétences d'un employé avec niveau minimum
    @Query("SELECT ce FROM CompetenceEmploye ce WHERE ce.employe.id = :employeId AND ce.niveau >= :niveauMin")
    List<CompetenceEmploye> findByEmployeIdAndNiveauMin(@Param("employeId") String employeId, 
                                                       @Param("niveauMin") Integer niveauMin);
    
    // Statistiques des compétences par catégorie pour un employé
    @Query("SELECT c.categorie.nom, COUNT(ce), AVG(ce.niveau) " +
           "FROM CompetenceEmploye ce JOIN ce.competence c " +
           "WHERE ce.employe.id = :employeId " +
           "GROUP BY c.categorie.id, c.categorie.nom")
    List<Object[]> getStatsByCategorieForEmploye(@Param("employeId") String employeId);
    
    // Supprimer toutes les compétences d'un employé
    void deleteByEmployeId(String employeId);
    
    // Supprimer une compétence pour tous les employés
    void deleteByCompetenceId(String competenceId);
}