package com.rh.manage.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.Employe;

@Repository
public interface DemandeCongeRepository extends JpaRepository<DemandeConge, String> {

    Optional<DemandeConge> findByEmployeIdAndDateDebut(String employeId, LocalDate dateDebut);

    Optional<List<DemandeConge>> findByEmployeId(String employeId);

    List<DemandeConge> findByDecisionManager(Integer decisionManager);

    // Méthode optionnelle si vous voulez filtrer directement en base de données
    @Query("SELECT d FROM DemandeConge d " +
        "WHERE d.employe.id = :employeId AND " +
        "(:dateDebut IS NULL OR d.dateDebut >= :dateDebut) AND " +
        "(:dateFin IS NULL OR d.dateFin <= :dateFin) " +
        "ORDER BY d.dateDebut")
    List<DemandeConge> findByEmployeIdAndPeriode(
        @Param("employeId") String employeId,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin);

    /**
     * Vérifie s'il existe déjà un congé qui chevauche la période demandée
     * pour un employé donné.
     */
    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM DemandeConge d
        WHERE d.employe.id = :employeId
        AND d.decisionManager NOT IN (2, 6)
        AND d.dateDebut <= :dateFin
        AND d.dateFin >= :dateDebut
    """)
    boolean existeChevauchementConge(
        @Param("employeId") String employeId,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin
    );

    // Recherche multicritère avec Query (correction d'employe)
    @Query("SELECT d FROM DemandeConge d WHERE " +
       "(:idEmploye IS NULL OR LOWER(d.employe.id) LIKE LOWER(CONCAT('%', :idEmploye, '%'))) AND " +
       "(:typeCongeId IS NULL OR d.typeConge.id = :typeCongeId) AND " +
       "(:dateDebut IS NULL OR d.dateDebut >= :dateDebut) AND " +
       "(:dateFin IS NULL OR d.dateFin <= :dateFin) AND " +
       "(:dateDemandeMin IS NULL OR d.dateDemande >= :dateDemandeMin)")
    Page<DemandeConge> filtrerDemandes(
        @Param("idEmploye") String idEmploye,
        @Param("typeCongeId") String typeCongeId,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin,
        @Param("dateDemandeMin") LocalDate dateDemandeMin,
        Pageable pageable);

    @Query("SELECT d FROM DemandeConge d WHERE " +
        "(:idEmploye IS NULL OR LOWER(d.employe.id) LIKE LOWER(CONCAT('%', :idEmploye, '%'))) AND " +
        "(:typeCongeId IS NULL OR d.typeConge.id = :typeCongeId) AND " +
        "(:dateDebut IS NULL OR d.dateDebut >= :dateDebut) AND " +
        "(:dateFin IS NULL OR d.dateFin <= :dateFin) AND " +
        "(:dateDemandeMin IS NULL OR d.dateDemande >= :dateDemandeMin)")
    List<DemandeConge> filtrerDemandes(
        @Param("idEmploye") String idEmploye,
        @Param("typeCongeId") String typeCongeId,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin,
        @Param("dateDemandeMin") LocalDate dateDemandeMin);

    @Query("SELECT d FROM DemandeConge d WHERE d.dateDemande >= :dateLimite")
    Page<DemandeConge> findByDateDemandeAfter(@Param("dateLimite") LocalDate dateLimite, Pageable pageable);

    /**
     * Récupère toutes les demandes de congé associées à un manager spécifique
     * @param managerId L'ID du manager
     * @return Liste des demandes de congé gérées par ce manager
     */
    @Query("SELECT d FROM DemandeConge d WHERE d.manager.id = :managerId")
    List<DemandeConge> findByManagerId(@Param("managerId") String managerId);

    /**
     * Compte le nombre total de demandes de congé pour l'année en cours
     * L'année en cours est déterminée par la date de demande
     * 
     * @return Nombre total de demandes pour l'année en cours
     */
    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE)")
    Long countTotalDemandesAnneeEnCours();

    
    
    /**
     * Version alternative avec paramètre explicite
     * 
     * @param annee Année à filtrer
     * @return Nombre total de demandes pour l'année spécifiée
     */
    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE YEAR(d.dateDemande) = :annee")
    Long countTotalDemandesParAnnee(@Param("annee") int annee);
    
    /**
     * Autre alternative si vous voulez inclure tous les statuts
     * pour l'année en cours
     */
    @Query("SELECT COUNT(d) FROM DemandeConge d WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE)")
    Long countAllDemandesAnneeEnCours();

    /**
     * Calcule la moyenne des jours de congés demandés pour l'année en cours
     * @return La moyenne des jours de congés (peut être null si aucune demande)
     */
    @Query("SELECT AVG(d.nbJours) FROM DemandeConge d WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE)")
    Double calculerMoyenneJoursCongesAnneeEnCours();

    /**
     * Trouve le mois avec le plus de demandes de congé pour l'année en cours
     * Retourne le mois (1-12) et le nombre de demandes
     */
    @Query("SELECT MONTH(d.dateDemande) as mois, COUNT(d) as nombreDemandes " +
           "FROM DemandeConge d " +
           "WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(d.dateDemande) " +
           "ORDER BY nombreDemandes DESC")
    List<Object[]> findMoisAvecPlusDeDemandes();
    
    /**
     * Trouve le mois avec le plus de jours de congé demandés pour l'année en cours
     * (critère: somme des nbJours)
     */
    @Query("SELECT MONTH(d.dateDemande) as mois, SUM(d.nbJours) as totalJours " +
           "FROM DemandeConge d " +
           "WHERE YEAR(d.dateDemande) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(d.dateDemande) " +
           "ORDER BY totalJours DESC")
    List<Object[]> findMoisAvecPlusDeJoursConges();
    
    /**
     * Version pour une année spécifique
     */
    @Query("SELECT MONTH(d.dateDemande) as mois, COUNT(d) as nombreDemandes " +
           "FROM DemandeConge d " +
           "WHERE YEAR(d.dateDemande) = :annee " +
           "GROUP BY MONTH(d.dateDemande) " +
           "ORDER BY nombreDemandes DESC")
    List<Object[]> findMoisAvecPlusDeDemandesPourAnnee(@Param("annee") int annee);

    
    /**
     * Récupère toutes les demandes de congé par type de congé (idTypeConge)
     */
    List<DemandeConge> findByTypeCongeId(String idTypeConge);

    /**
     * Compte le nombre total de demandes pour un type de congé spécifique
     * @param idTypeConge L'ID du type de congé
     * @return Nombre de demandes pour ce type
     */
    Long countByTypeCongeId(String idTypeConge);

    @Query(value = "SELECT d.* FROM demande_conge d " +
       "WHERE d.id_employe IN (" +
       "   SELECT d2.id_employe FROM demande_conge d2 " +
       "   WHERE YEAR(d2.date_demande) = YEAR(CURRENT_DATE) " +
       "   GROUP BY d2.id_employe " +
       "   ORDER BY COUNT(d2.id) DESC " +
       "   LIMIT 5" +
       ") " +
       "AND YEAR(d.date_demande) = YEAR(CURRENT_DATE) " +
       "ORDER BY d.date_demande DESC", 
       nativeQuery = true)
    List<DemandeConge> findDemandesDesTop5Employes();

    /**
     * Récupère les demandes de congé avec limite et tri par date de demande décroissante
     * @param limit Nombre maximum de demandes à retourner
     * @return Liste des dernières demandes
     */
    @Query("SELECT d FROM DemandeConge d ORDER BY d.dateDemande DESC")
    List<DemandeConge> findDemandesRecentest(Pageable pageable);
}

