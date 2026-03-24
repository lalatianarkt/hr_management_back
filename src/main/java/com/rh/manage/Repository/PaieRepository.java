package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Paie;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaieRepository extends JpaRepository<Paie, String> {
       /**
        * Vérifie si une paie existe déjà pour un employé dans une période donnée
        * @param employeId L'identifiant de l'employé
        * @param periodeId L'identifiant de la période de paie
        * @return true si une paie existe déjà, false sinon
        */
       @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
              "FROM Paie p " +
              "WHERE p.employe.id = :employeId " +
              "AND p.periodePaie.id = :periodeId")
       boolean existsByEmployeIdAndPeriodeId(@Param("employeId") String employeId, 
                                          @Param("periodeId") String periodeId);
    List<Paie> findByMatricule(Integer matricule);
    // Trouver la dernière paie d'un employé (par date de paiement)
    Optional<Paie> findFirstByEmployeIdOrderByCreatedAtDesc(String idEmploye);
    
    // Recherche par nom
    List<Paie> findByNomContainingIgnoreCase(String nom);
    
    // Recherche par prénom
    List<Paie> findByPrenomContainingIgnoreCase(String prenom);
    
    // Recherche par nom complet
    List<Paie> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    // Recherche par statut de clôture
    List<Paie> findByStatutCloture(Integer statutCloture);

    @Query("SELECT p FROM Paie p " +
       "JOIN PeriodePaie pp ON p.periodePaie.id = pp.id " +
       "WHERE p.employe.id = :employeId " +
       "AND pp.statut = 0 " +  // période active (statut = 1)
       "ORDER BY pp.dateFin DESC")
    Optional<Paie> findPaieActivePourEmploye(@Param("employeId") String employeId);
    
    // Recherche par employé
    List<Paie> findByEmployeId(String employeId);

    @Query("SELECT p FROM Paie p WHERE p.employe.id = :employeId AND p.statutCloture = 0 ORDER BY p.periodePaie.dateFin DESC")
    Optional<Paie> findDernierePaieNonCloturee(@Param("employeId") String employeId);

    boolean existsByEmployeIdAndStatutCloture(String employeId, Integer statutCloture);
    
    // Recherche par département
    List<Paie> findByDepartement(String departement);

    // Recherche par mois/année avec jointure vers PeriodePaie
    @Query("SELECT p FROM Paie p JOIN p.periodePaie periode WHERE YEAR(periode.dateDebut) = :year AND MONTH(periode.dateDebut) = :month")
    List<Paie> findByPeriodeYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    // Recherche paginée avec filtre
    @Query("SELECT p FROM Paie p " +
           "JOIN p.periodePaie periode " +
           "WHERE (:matricule IS NULL OR p.matricule = :matricule) AND " +
           "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:prenom IS NULL OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))) AND " +
           "(:dateDebut IS NULL OR periode.dateDebut >= :dateDebut) AND " +
           "(:dateFin IS NULL OR periode.dateFin <= :dateFin) AND " +
           "(:statutCloture IS NULL OR p.statutCloture = :statutCloture)")
    Page<Paie> searchPaies(
            @Param("matricule") Integer matricule,
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("statutCloture") Integer statutCloture,
            Pageable pageable);
    
    // Vérifier si une paie existe pour un employé dans une période
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM Paie p JOIN p.periodePaie periode " +
           "WHERE p.employe.id = :employeId " +
           "AND periode.dateDebut = :dateDebut " +
           "AND periode.dateFin = :dateFin")
    boolean existsByEmployeIdAndPeriode(
            @Param("employeId") String employeId, 
            @Param("dateDebut") LocalDate dateDebut, 
            @Param("dateFin") LocalDate dateFin);
    
    // Récupérer la dernière paie d'un employé
    @Query("SELECT p FROM Paie p JOIN p.periodePaie periode WHERE p.employe.id = :employeId ORDER BY periode.dateFin DESC")
    List<Paie> findTopByEmployeIdOrderByPeriodeDateFinDesc(@Param("employeId") String employeId);
    
    Optional<Paie> findFirstByEmployeIdOrderByPeriodePaieDateFinDesc(String employeId);
    
    // Statistiques - Compter les paies dans une période
    @Query("SELECT COUNT(p) FROM Paie p JOIN p.periodePaie periode " +
           "WHERE periode.dateDebut >= :dateDebut AND periode.dateFin <= :dateFin")
    long countByPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
    
    // Somme des salaires de base par période
    @Query("SELECT COALESCE(SUM(p.salaireBase), 0) FROM Paie p JOIN p.periodePaie periode " +
           "WHERE periode.dateDebut >= :dateDebut AND periode.dateFin <= :dateFin")
    BigDecimal sumSalaireBaseByPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
    
    // Liste des paies avec détails employé
    @Query("SELECT p FROM Paie p JOIN FETCH p.employe WHERE p.id = :id")
    Optional<Paie> findByIdWithEmploye(@Param("id") String id);
    
    // Liste des paies pour une société
    List<Paie> findByInformationSocieteId(Integer infoSocieteId);

    @Query("SELECT DISTINCT periode.dateDebut FROM Paie p JOIN p.periodePaie periode " +
           "WHERE p.departement = :departement AND p.statutCloture = 0 " +
           "ORDER BY periode.dateDebut DESC")
    Optional<LocalDate> findDistinctDateDebutPeriodeByDepartementAndStatutClotureZero(@Param("departement") String departement);
    
    // === NOUVELLES METHODES AJOUTEES ===
    
    // Recherche par ID de période
    List<Paie> findByPeriodePaieId(String periodeId);

    Optional<List<Paie>> findByDepartementAndPeriodePaieId(String departement, String preiodePaieId);
    
    // Recherche par période et statut
    @Query("SELECT p FROM Paie p WHERE p.periodePaie.id = :periodeId AND p.statutCloture = :statutCloture")
    List<Paie> findByPeriodeIdAndStatutCloture(@Param("periodeId") String periodeId, 
                                               @Param("statutCloture") Integer statutCloture);
    
    // Recherche par statut de période
    @Query("SELECT p FROM Paie p JOIN p.periodePaie periode WHERE periode.statut = :statutPeriode")
    List<Paie> findByStatutPeriode(@Param("statutPeriode") Integer statutPeriode);
    
    // Recherche par employé et période
    @Query("SELECT p FROM Paie p WHERE p.employe.id = :employeId AND p.periodePaie.id = :periodeId")
    Optional<Paie> findByEmployeIdAndPeriodeId(@Param("employeId") String employeId, 
                                               @Param("periodeId") String periodeId);
    
    // Recherche par période (date de début et fin)
    @Query("SELECT p FROM Paie p JOIN p.periodePaie periode " +
           "WHERE periode.dateDebut = :dateDebut AND periode.dateFin = :dateFin")
    List<Paie> findByPeriodeDates(@Param("dateDebut") LocalDate dateDebut, 
                                  @Param("dateFin") LocalDate dateFin);
    
    // Recherche par période entre deux dates
    @Query("SELECT p FROM Paie p JOIN p.periodePaie periode " +
           "WHERE periode.dateDebut BETWEEN :startDate AND :endDate " +
           "OR periode.dateFin BETWEEN :startDate AND :endDate")
    List<Paie> findByPeriodeBetween(@Param("startDate") LocalDate startDate, 
                                    @Param("endDate") LocalDate endDate);
    
    // Compter les paies non clôturées pour un employé
    @Query("SELECT COUNT(p) FROM Paie p WHERE p.employe.id = :employeId AND p.statutCloture = 0")
    long countNonClotureesByEmployeId(@Param("employeId") String employeId);
    
    // Compter les paies clôturées pour une période
    @Query("SELECT COUNT(p) FROM Paie p JOIN p.periodePaie periode " +
           "WHERE periode.id = :periodeId AND p.statutCloture = 1")
    long countClotureesByPeriodeId(@Param("periodeId") String periodeId);
    
    // Masse salariale par département
    @Query("SELECT p.departement, COALESCE(SUM(p.salaireBase), 0) FROM Paie p " +
           "GROUP BY p.departement")
    List<Object[]> findMasseSalarialeParDepartement();
    
    // Masse salariale par département pour une période
    @Query("SELECT p.departement, COALESCE(SUM(p.salaireBase), 0) FROM Paie p " +
           "JOIN p.periodePaie periode " +
           "WHERE periode.dateDebut >= :dateDebut AND periode.dateFin <= :dateFin " +
           "GROUP BY p.departement")
    List<Object[]> findMasseSalarialeParDepartementPourPeriode(@Param("dateDebut") LocalDate dateDebut, 
                                                              @Param("dateFin") LocalDate dateFin);
    
    // Trouver toutes les périodes distinctes
    @Query("SELECT DISTINCT p.periodePaie FROM Paie p ORDER BY p.periodePaie.dateDebut DESC")
    List<com.rh.manage.Model.PeriodePaie> findAllDistinctPeriodes();
    
    // Trouver les paies avec la période jointe
    @Query("SELECT p FROM Paie p JOIN FETCH p.periodePaie WHERE p.id IN :ids")
    List<Paie> findAllWithPeriode(@Param("ids") List<String> ids);
    
    // Statistiques avancées par période
    @Query("SELECT " +
           "periode.id, " +
           "COUNT(p) as nombrePaies, " +
           "COALESCE(SUM(p.salaireBase), 0) as totalSalaireBase, " +
           "MIN(periode.dateDebut) as dateDebut, " +
           "MAX(periode.dateFin) as dateFin, " +
           "periode.statut " +
           "FROM Paie p JOIN p.periodePaie periode " +
           "GROUP BY periode.id, periode.statut " +
           "ORDER BY periode.dateDebut DESC")
    List<Object[]> findStatistiquesParPeriode();
    
    // Vérifier si une période a des paies
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM Paie p WHERE p.periodePaie.id = :periodeId")
    boolean existsByPeriodeId(@Param("periodeId") String periodeId);
    
    // Supprimer toutes les paies d'une période
    @Query("DELETE FROM Paie p WHERE p.periodePaie.id = :periodeId")
    void deleteByPeriodeId(@Param("periodeId") String periodeId);
    
    // Mettre à jour le statut de clôture pour une période
    @Modifying
    @Transactional
    @Query("UPDATE Paie p SET p.statutCloture = :statutCloture WHERE p.periodePaie.id = :periodeId")
    int updateStatutClotureByPeriodeId(@Param("periodeId") String periodeId, 
                                       @Param("statutCloture") Integer statutCloture);
}
