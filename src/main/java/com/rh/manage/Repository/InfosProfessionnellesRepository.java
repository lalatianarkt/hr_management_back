package com.rh.manage.Repository;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;

@Repository
public interface InfosProfessionnellesRepository extends JpaRepository<InfosProfessionnelles, String> {

    // Méthode 1: Simple query derivation - Par manager ET statut = 0
    List<InfosProfessionnelles> findByManagerAndStatut(Manager manager, int statut);

    default Optional<InfosProfessionnelles> findLatestByEmployeId(String employeId) {
       List<InfosProfessionnelles> result = findAllByEmployeIdOrderByDateDebutDesc(employeId);
       return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Query("SELECT ip FROM InfosProfessionnelles ip " +
       "WHERE ip.employe.id = :employeId " +
       "ORDER BY ip.dateDebutAssignationPoste DESC")
    List<InfosProfessionnelles> findAllByEmployeIdOrderByDateDebutDesc(@Param("employeId") String employeId);
    
    // Méthode 2: Spécifique pour statut = 0
    default List<InfosProfessionnelles> findByManagerAndStatutZero(Manager manager) {
        return findByManagerAndStatut(manager, 0);
    }

    // Récupérer le manager actif d'un département
    @Query("SELECT ip.manager FROM InfosProfessionnelles ip " +
           "WHERE ip.departement.id = :departementId " +
           "AND ip.statut = 0") // statut actif
    Optional<Manager> findManagerByDepartementId(@Param("departementId") String departementId);

    // Vérifie si le matricule existe
    // boolean existsByMatricule(String matricule);
    // Optional<InfosProfessionnelles> findByMatricule(String matricule);
    // ✅ REQUÊTE NATIVE qui fonctionne
       @Query("""
       SELECT ip
       FROM InfosProfessionnelles ip
       WHERE ip.employe.id = :employeId
       AND ip.statut = 0
       """)
       Optional<InfosProfessionnelles> findByEmployeId(@Param("employeId") String employeId);


//     Optional<InfosProfessionnelles> findByEmployeIdAnd

    // ✅ Vérifier l'existence avec requête native
    @Query(value = "SELECT COUNT(*) > 0 FROM infos_professionnelles WHERE id_employe = :employeId", nativeQuery = true)
    boolean existsByEmployeId(@Param("employeId") String employeId);

    // Trouver toutes les InfosProfessionnelles actives pour un manager
    List<InfosProfessionnelles> findByManagerAndDateFinAssignationPosteIsNull(Manager manager);
    
    // Trouver par employé
    List<InfosProfessionnelles> findByEmploye(Employe employe); 

     // ✅ TROUVER LES EMPLOYÉS D'UN MANAGER SPÉCIFIQUE
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.manager.id = :managerId AND ip.employe.statut = 0")
    List<InfosProfessionnelles> findEmployesByManager(@Param("managerId") String managerId);
    
    // Récupérer les infos professionnelles par ID d'employé avec statut = 0 ET dateFin = null
    List<InfosProfessionnelles> findByEmployeIdAndStatutAndDateFinAssignationPosteIsNull(String employeId, int statut);
    
    // Récupérer la dernière info professionnelle active et en cours d'un employé
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.employe.id = :employeId AND ip.statut = 0 AND ip.dateFinAssignationPoste IS NULL ORDER BY ip.dateDebutAssignationPoste DESC")
    List<InfosProfessionnelles> findLatestActiveAndCurrentByEmployeId(@Param("employeId") String employeId);
    
    // Récupérer toutes les infos professionnelles actives et en cours
    List<InfosProfessionnelles> findByStatutAndDateFinAssignationPosteIsNull(int statut);
    
    // Vérifier si un employé a des infos professionnelles actives et en cours
    boolean existsByEmployeIdAndStatutAndDateFinAssignationPosteIsNull(String employeId, int statut);
    
    // Récupérer l'info professionnelle active et en cours d'un employé (une seule)
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.employe.id = :employeId AND ip.statut = 0 AND ip.dateFinAssignationPoste IS NULL")
    Optional<InfosProfessionnelles> findActiveAndCurrentByEmployeId(@Param("employeId") String employeId);
    
    // Récupérer les infos professionnelles par poste avec statut = 0 ET dateFin = null
    List<InfosProfessionnelles> findByPosteIdAndStatutAndDateFinAssignationPosteIsNull(String posteId, int statut);
    
    // Récupérer les infos professionnelles par département avec statut = 0 ET dateFin = null
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.poste.departement.id = :departementId AND ip.statut = 0 AND ip.dateFinAssignationPoste IS NULL")
    List<InfosProfessionnelles> findByDepartementIdAndStatutAndDateFinAssignationPosteIsNull(@Param("departementId") String departementId, int statut);

    // Récupérer les infos professionnelles par ID d'employé avec statut = 0
    // InfosProfessionnelles findByEmployeIdAndStatut(String employeId, int statut);
    
    // Récupérer la dernière info professionnelle avec statut = 0 d'un employé
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.employe.id = :employeId AND ip.statut = 0 ORDER BY ip.dateDebutAssignationPoste DESC")
    List<InfosProfessionnelles> findLatestByEmployeIdAndStatut(@Param("employeId") String employeId);
    
    // Récupérer une seule info professionnelle avec statut = 0 d'un employé
   @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.employe.id = :employeId AND (ip.statut = 0 OR ip.statut = 1)")
   List<InfosProfessionnelles> findByEmployeIdAndStatut(@Param("employeId") String employeId);

    @Query("select ip from InfosProfessionnelles ip where ip.manager.id = :managerId and ip.statut = 0")
    List<InfosProfessionnelles> findByManagerIdAndStatut(@Param("managerId") String managerId);

    @Query("""
        SELECT ip 
        FROM InfosProfessionnelles ip 
        WHERE ip.poste.departement.id = :departementId 
        AND ip.statut = 0
    """)
    List<InfosProfessionnelles> findByDepartementIdAndStatut(@Param("departementId") String departementId);
    
    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.employe.id = :employeId AND ip.statut = 0")
    Optional<InfosProfessionnelles> findActiveByEmployeId(@Param("employeId") String employeId);

    // Récupérer les infosPro par département avec pagination
    @Query("SELECT ip FROM InfosProfessionnelles ip " +
           "JOIN ip.poste p " +
           "JOIN p.departement d " +
           "WHERE d.id = :departementId " +
           "AND ip.statut = :statut")
    List<InfosProfessionnelles> findByDepartementAndStatut(
            @Param("departementId") String departementId,
            @Param("statut") int statut);

    // 1. Récupérer les informations professionnelles ACTIVES d'un employé
    @Query("SELECT ip FROM InfosProfessionnelles ip " +
           "WHERE ip.employe.id = :employeId " +
           "AND ip.statut = 0 " +
           "AND (ip.dateFinAssignationPoste IS NULL OR ip.dateFinAssignationPoste >= CURRENT_DATE) " +
           "ORDER BY ip.dateDebutAssignationPoste DESC")
    InfosProfessionnelles findCurrentByEmployeId(@Param("employeId") String employeId);

    // 1. Récupérer la dernière information professionnelle d'un employé (sans filtre sur le statut)
    @Query("SELECT ip FROM InfosProfessionnelles ip " +
       "WHERE ip.employe.id = :employeId " +
       "ORDER BY ip.dateDebutAssignationPoste DESC " +
       "LIMIT 1")
    InfosProfessionnelles findLastByEmployeId(@Param("employeId") String employeId);    

    @Query("SELECT ip FROM InfosProfessionnelles ip WHERE ip.typeContrat.intitule = :intitule")
    List<InfosProfessionnelles> findByTypeContratIntitule(@Param("intitule") String intitule);

    List<InfosProfessionnelles> findByStatut(int statut);

    // Version 1: Avec LEFT JOIN et vérification des null
    @Query("SELECT ip FROM InfosProfessionnelles ip " +
           "WHERE ip.employe.id NOT IN (" +
           "    SELECT m.employe.id FROM Manager m WHERE m.employe.id IS NOT NULL" +
           ") " +
           "AND (ip.dateFinAssignationPoste IS NULL OR ip.dateFinAssignationPoste > CURRENT_DATE) " +
           "ORDER BY ip.employe.nom ASC, ip.employe.prenom ASC")
    List<InfosProfessionnelles> findInfoProForNonManagers();

    Optional<InfosProfessionnelles> findByMatriculeAndStatut(String matricule, int statut);

    boolean existsByMatricule(String matricule);

    Optional<List<InfosProfessionnelles>> findByDepartementIdAndStatut(String departementId, int statut);
    
}


// select * from infos_professionnelles where id_departement = '' and where statut = 1 and where dateFin != null;