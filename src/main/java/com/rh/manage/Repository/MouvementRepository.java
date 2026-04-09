package com.rh.manage.Repository;

import com.rh.manage.Model.Mouvement;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MouvementRepository extends JpaRepository<Mouvement, String> {

    /**
     * Trouver les mouvements par statut
     */
    List<Mouvement> findByStatut(Integer statut);
    
    /**
     * Trouver les mouvements par une liste de statuts
     */
    List<Mouvement> findByStatutIn(List<Integer> statuts);

    /**
     * Trouver les mouvements par employé demandeur
     */
    List<Mouvement> findByEmployeDemandeur(Employe employeDemandeur);

    /**
     * Trouver les mouvements par employé validateur
     */
    List<Mouvement> findByEmployeValidateur(Employe employeValidateur);

    /**
     * Trouver les mouvements par info pro actuelle
     */
    List<Mouvement> findByInfosProActuel(InfosProfessionnelles infosProActuel);

    /**
     * Trouver les mouvements par info pro proposée
     */
    List<Mouvement> findByInfosProPropose(InfosProfessionnelles infosProPropose);

    /**
     * Trouver les mouvements par type de mouvement
     */
    List<Mouvement> findByTypeMouvementId(String idTypeMouvement);

    /**
     * Trouver les mouvements entre deux dates de demande
     */
    List<Mouvement> findByDateDemandeBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Trouver les mouvements validés entre deux dates
     */
    List<Mouvement> findByDateValidationBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Vérifier si un employé a des mouvements en attente
     */
    boolean existsByEmployeDemandeurAndStatut(Employe employe, Integer statut);

    /**
     * Trouver le dernier mouvement d'un employé
     */
    Optional<Mouvement> findFirstByEmployeDemandeurOrderByDateDemandeDesc(Employe employe);

    /**
     * Récupérer tous les mouvements dont le statut n'est pas égal à 6
     */
    @Query("SELECT m FROM Mouvement m WHERE m.statut <> 6")
    List<Mouvement> getMouvement();

    /**
     * Requête personnalisée : Mouvements en attente avec details
     */
//     @Query("SELECT m FROM Mouvement m WHERE m.statut = 1 ORDER BY m.dateDemande ASC")
//     List<Mouvement> findMouvementsEnAttenteAvecDetails();

//     /**
//      * Requête personnalisée : Statistiques par type de mouvement
//      */
//     @Query("SELECT m.typeMouvement.type, COUNT(m), AVG(DATEDIFF(m.dateValidation, m.dateDemande)) " +
//            "FROM Mouvement m " +
//            "WHERE m.statut = 2 " +
//            "GROUP BY m.typeMouvement.type")
//     List<Object[]> getStatistiquesParTypeMouvement();

//     /**
//      * Requête personnalisée : Mouvements d'un employé avec pagination
//      */
//     @Query("SELECT m FROM Mouvement m WHERE m.employeDemandeur.id = :idEmploye ORDER BY m.dateDemande DESC")
//     List<Mouvement> findMouvementsByEmployeId(@Param("idEmploye") String idEmploye);

//     /**
//      * Requête personnalisée : Mouvements validés par un RH spécifique
//      */
//     @Query("SELECT m FROM Mouvement m WHERE m.employeValidateur.id = :idValidateur AND m.statut = 2")
//     List<Mouvement> findMouvementsValidesParValidateur(@Param("idValidateur") String idValidateur);

//     /**
//      * Requête personnalisée : Délai moyen de traitement par validateur
//      */
//     @Query("SELECT e.nom, e.prenom, COUNT(m), AVG(DATEDIFF(m.dateValidation, m.dateDemande)) " +
//            "FROM Mouvement m " +
//            "JOIN m.employeValidateur e " +
//            "WHERE m.statut = 2 " +
//            "GROUP BY e.id, e.nom, e.prenom")
//     List<Object[]> getDelaiMoyenTraitementParValidateur();

//     /**
//      * Requête personnalisée : Mouvements avec informations complètes pour le reporting
//      */
//     @Query("SELECT m, ed, ev, ipa, ipp " +
//            "FROM Mouvement m " +
//            "LEFT JOIN m.employeDemandeur ed " +
//            "LEFT JOIN m.employeValidateur ev " +
//            "LEFT JOIN m.infosProActuel ipa " +
//            "LEFT JOIN m.infosProPropose ipp " +
//            "WHERE m.dateDemande BETWEEN :startDate AND :endDate")
//     List<Object[]> findMouvementsCompletsPourReporting(
//         @Param("startDate") LocalDate startDate, 
//         @Param("endDate") LocalDate endDate
//     );

//     /**
//      * Requête personnalisée : Nombre de mouvements par statut
//      */
//     @Query("SELECT m.statut, COUNT(m) FROM Mouvement m GROUP BY m.statut")
//     List<Object[]> countMouvementsByStatut();

//     /**
//      * Trouver les mouvements expirés (en attente depuis plus de X jours)
//      */
//     @Query("SELECT m FROM Mouvement m WHERE m.statut = 1 AND m.dateDemande < :expirationDate")
//     List<Mouvement> findMouvementsExpires(@Param("expirationDate") LocalDate expirationDate);

//     /**
//      * Vérifier s'il existe un mouvement en cours pour une info pro spécifique
//      */
//     @Query("SELECT COUNT(m) > 0 FROM Mouvement m WHERE (m.infosProActuel.id = :idInfoPro OR m.infosProPropose.id = :idInfoPro) AND m.statut = 1")
//     boolean existsMouvementEnCoursPourInfoPro(@Param("idInfoPro") String idInfoPro);
}
