package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Model.VuePaieCompleteId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VuePaieCompleteRepository extends JpaRepository<VuePaieComplete, VuePaieCompleteId> {
    
    // Recherche par matricule
    List<VuePaieComplete> findByMatricule(String matricule);
    
    // Recherche par nom
    List<VuePaieComplete> findByNomContainingIgnoreCase(String nom);
    
    // Recherche par prénom
    List<VuePaieComplete> findByPrenomContainingIgnoreCase(String prenom);
    
    // Recherche par nom complet
    List<VuePaieComplete> findByNomCompletContainingIgnoreCase(String nomComplet);
    
    // Recherche par département
    List<VuePaieComplete> findByDepartement(String departement);

       // Méthode de recherche avec pagination - ACCEPTE LES DATES EN STRING
       @Query("SELECT v FROM VuePaieComplete v WHERE " +
              "(:departement IS NULL OR v.departement = :departement) AND " +
              "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
              "(:matricule IS NULL OR :matricule = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
              "(:search IS NULL OR :search = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.nomComplet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.fonction) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
              "(:dateDebut IS NULL OR :dateDebut = '' OR v.dateDebutPeriode >= CAST(:dateDebut AS date)) AND " +
              "(:dateFin IS NULL OR :dateFin = '' OR v.dateFinPeriode <= CAST(:dateFin AS date))")
       Page<VuePaieComplete> findByDepartementAndStatutClotureWithPagination(
              @Param("departement") String departement,
              @Param("statutCloture") Integer statutCloture,
              @Param("matricule") String matricule,
              @Param("search") String search,
              @Param("dateDebut") String dateDebut,
              @Param("dateFin") String dateFin,
              Pageable pageable);

       // Méthode de comptage total - ACCEPTE LES DATES EN STRING
       @Query("SELECT COUNT(v) FROM VuePaieComplete v WHERE " +
              "(:departement IS NULL OR v.departement = :departement) AND " +
              "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
              "(:matricule IS NULL OR :matricule = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
              "(:search IS NULL OR :search = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.nomComplet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.fonction) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
              "(:dateDebut IS NULL OR :dateDebut = '' OR v.dateDebutPeriode >= CAST(:dateDebut AS date)) AND " +
              "(:dateFin IS NULL OR :dateFin = '' OR v.dateFinPeriode <= CAST(:dateFin AS date))")
       long countByDepartementAndStatutClotureWithFilters(
              @Param("departement") String departement,
              @Param("statutCloture") Integer statutCloture,
              @Param("matricule") String matricule,
              @Param("search") String search,
              @Param("dateDebut") String dateDebut,
              @Param("dateFin") String dateFin);

       // Méthode de comptage par statut spécifique - ACCEPTE LES DATES EN STRING
       @Query("SELECT COUNT(v) FROM VuePaieComplete v WHERE " +
              "(:departement IS NULL OR v.departement = :departement) AND " +
              "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
              "v.statutCloture = :statut AND " +
              "(:matricule IS NULL OR :matricule = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
              "(:search IS NULL OR :search = '' OR " +
              "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.nomComplet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
              "LOWER(v.fonction) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
              "(:dateDebut IS NULL OR :dateDebut = '' OR v.dateDebutPeriode >= CAST(:dateDebut AS date)) AND " +
              "(:dateFin IS NULL OR :dateFin = '' OR v.dateFinPeriode <= CAST(:dateFin AS date))")
       long countByDepartementAndStatutClotureWithFiltersAndStatut(
              @Param("departement") String departement,
              @Param("statutCloture") Integer statutCloture,
              @Param("statut") Integer statut,
              @Param("matricule") String matricule,
              @Param("search") String search,
              @Param("dateDebut") String dateDebut,
              @Param("dateFin") String dateFin);


//     @Query("SELECT v FROM VuePaieComplete v WHERE " +
//            "(:departement IS NULL OR v.departement = :departement) AND " +
//            "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
//            "(:matricule IS NULL OR :matricule = '' OR " +
//            "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
//            "(:search IS NULL OR :search = '' OR " +
//            "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(v.nomComplet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//            "LOWER(v.fonction) LIKE LOWER(CONCAT('%', :search, '%')))")
//     Page<VuePaieComplete> findByDepartementAndStatutClotureWithPagination(
//             @Param("departement") String departement,
//             @Param("statutCloture") Integer statutCloture,
//             @Param("matricule") String matricule,
//             @Param("search") String search,
//             Pageable pageable);

    @Query("SELECT COUNT(v) FROM VuePaieComplete v WHERE " +
           "(:departement IS NULL OR v.departement = :departement) AND " +
           "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
           "(:matricule IS NULL OR :matricule = '' OR " +
           "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(v.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.nomComplet) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(v.fonction) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByDepartementAndStatutClotureWithFilters(
            @Param("departement") String departement,
            @Param("statutCloture") Integer statutCloture,
            @Param("matricule") String matricule,
            @Param("search") String search);
    
    // Recherche par fonction
    List<VuePaieComplete> findByFonction(String fonction);
    
    // Recherche par statut de clôture
    List<VuePaieComplete> findByStatutCloture(Integer statutCloture);
    
    // Recherche par catégorie de salaire
    List<VuePaieComplete> findByCategorieSalaire(String categorieSalaire);
    
    // Recherche par période
    List<VuePaieComplete> findByDateDebutPeriodeBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Recherche par mois et année
    @Query("SELECT v FROM VuePaieComplete v WHERE v.moisPaie = :mois AND v.anneePaie = :annee")
    List<VuePaieComplete> findByMoisAndAnnee(@Param("mois") Integer mois, @Param("annee") Integer annee);
    
    // Recherche par employé
    List<VuePaieComplete> findByIdEmploye(String idEmploye);
    
    // Recherche par ID de paie
    // List<VuePaieComplete> findByPaieId(String paieId);
    
    // Recherche avec filtres multiples
    @Query("SELECT v FROM VuePaieComplete v WHERE " +
           "(:departement IS NULL OR v.departement = :departement) AND " +
           "(:statutCloture IS NULL OR v.statutCloture = :statutCloture) AND " +
           "(:categorieSalaire IS NULL OR v.categorieSalaire = :categorieSalaire) AND " +
           "(:dateDebut IS NULL OR v.dateDebutPeriode >= :dateDebut) AND " +
           "(:dateFin IS NULL OR v.dateFinPeriode <= :dateFin)")
    List<VuePaieComplete> findByCriteria(
            @Param("departement") String departement,
            @Param("statutCloture") Integer statutCloture,
            @Param("categorieSalaire") String categorieSalaire,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
    
    // Statistiques par département
    @Query("SELECT v.departement, COUNT(v), SUM(v.salaireBrut), AVG(v.salaireBrut) " +
           "FROM VuePaieComplete v GROUP BY v.departement")
    List<Object[]> getStatsByDepartement();
    
    // Statistiques par mois/année
    @Query("SELECT v.anneePaie, v.moisPaie, COUNT(v), SUM(v.salaireBrut), SUM(v.salaireNet) " +
           "FROM VuePaieComplete v GROUP BY v.anneePaie, v.moisPaie ORDER BY v.anneePaie DESC, v.moisPaie DESC")
    List<Object[]> getStatsByMoisAnnee();
    
    // Trouver les paies avec salaire brut supérieur à un montant
    @Query("SELECT v FROM VuePaieComplete v WHERE v.salaireBrut > :montant ORDER BY v.salaireBrut DESC")
    List<VuePaieComplete> findBySalaireBrutGreaterThan(@Param("montant") BigDecimal montant);
    
    // Trouver les paies avec taux de prélèvement élevé
    @Query("SELECT v FROM VuePaieComplete v WHERE v.tauxPrelevementPercent > :taux ORDER BY v.tauxPrelevementPercent DESC")
    List<VuePaieComplete> findByTauxPrelevementGreaterThan(@Param("taux") BigDecimal taux);
    
    // Obtenir les périodes distinctes
    @Query("SELECT DISTINCT v.dateDebutPeriode FROM VuePaieComplete v ORDER BY v.dateDebutPeriode DESC")
    List<LocalDate> findDistinctPeriodes();
    
    // Obtenir les départements distincts
    @Query("SELECT DISTINCT v.departement FROM VuePaieComplete v ORDER BY v.departement")
    List<String> findDistinctDepartements();
    
    // Obtenir les catégories de salaire distinctes
    @Query("SELECT DISTINCT v.categorieSalaire FROM VuePaieComplete v")
    List<String> findDistinctCategoriesSalaire();

    // 1. Récupérer toutes les paies d'un département
    // List<VuePaieComplete> findByDepartement(String departement);

    VuePaieComplete findByPaieId(String paieId);
    
    // 2. Récupérer les paies d'un département avec un statut spécifique
    List<VuePaieComplete> findByDepartementAndStatutCloture(String departement, Integer statutCloture);
    
    // 3. Récupérer les paies d'un département dans une période
    @Query("SELECT v FROM VuePaieComplete v WHERE v.departement = :departement " +
           "AND v.dateDebutPeriode >= :dateDebut AND v.dateFinPeriode <= :dateFin")
    List<VuePaieComplete> findByDepartementAndPeriode(
            @Param("departement") String departement,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
}
