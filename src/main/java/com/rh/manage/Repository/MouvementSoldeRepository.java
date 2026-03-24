// package com.rh.manage.Repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import com.rh.manage.Model.MouvementSolde;
// import com.rh.manage.Model.TypeEnumConge;

// import java.util.List;
// import java.util.Optional;

// @Repository
// public interface MouvementSoldeRepository extends JpaRepository<MouvementSolde, Integer> {
//     List<MouvementSolde> findByIdEmploye(String idEmploye);
//     List<MouvementSolde> findByAnneeAndIdEmploye(Integer annee, String idEmploye);
//     List<MouvementSolde> findByTypeMouvement(TypeEnumConge typeMouvement);

//     @Query("SELECT m FROM MouvementSolde m " +
//        "WHERE m.idEmploye = :idEmploye " +
//        "AND m.statut = 1 " +
//        "AND m.annee = :annee " +
//        "ORDER BY m.annee DESC, m.createdAt DESC")
//     List<MouvementSolde> findByEmployeAndAnneeAndStatutOrderByAnneeDescCreatedAtDesc(
//             @Param("idEmploye") String idEmploye,
//             @Param("annee") Integer annee);

//     @Query("SELECT m FROM MouvementSolde m " +
//        "WHERE m.idEmploye = :idEmploye " +
//        "AND m.statut = 1 " +
//        "ORDER BY m.createdAt DESC")
//     Optional<MouvementSolde> findByEmployeAndStatutOrderByCreatedAtDesc(
//             @Param("idEmploye") String idEmploye);

//     @Query("SELECT m FROM MouvementSolde m " +
//         "WHERE m.idEmploye = :idEmploye " +
//         "AND m.statut = 1 " +
//         "AND m.annee = :annee " +
//         "ORDER BY m.createdAt DESC " +
//         "LIMIT 1")
//     Optional<MouvementSolde> findLastByEmployeAndAnneeAndStatutOrderByAnneeDescCreatedAtDesc(
//             @Param("idEmploye") String idEmploye,
//             @Param("annee") Integer annee);

//     List<MouvementSolde> findByIdEmployeAndTypeMouvementOrderByCreatedAtAsc(
//             String idEmploye,
//             TypeEnumConge typeMouvement
//     );

//     Optional<MouvementSolde> findFirstByIdEmployeAndAnneeAndStatutOrderByCreatedAtDesc(
//         String idEmploye,
//         Integer annee,
//         Integer statut
//    );

//    Optional<MouvementSolde> findFirstByIdEmployeAndStatutOrderByMoisDescCreatedAtDesc(
//         String idEmploye,
//         Integer statut
//   );


// }


package com.rh.manage.Repository;

import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.TypeEnumConge;
import com.rh.manage.Model.Employe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MouvementSoldeRepository extends JpaRepository<MouvementSolde, Integer> {
    // Par employé spécifique

    @Query("SELECT m FROM MouvementSolde m WHERE m.typeMouvement = 'REPORT' AND m.employe.id = :employeId ORDER BY m.annee DESC, m.mois DESC")
    Optional<List<MouvementSolde>> findMouvementsReportByEmployeId(@Param("employeId") String employeId);
    /* ===============================
       Méthodes existantes (corrigées)
       =============================== */

    // Anciennement findByIdEmploye(String)
    List<MouvementSolde> findByEmploye(Employe employe);

    // Anciennement findByAnneeAndIdEmploye
    List<MouvementSolde> findByAnneeAndEmploye(Integer annee, Employe employe);

    List<MouvementSolde> findByTypeMouvement(TypeEnumConge typeMouvement);

    Optional<MouvementSolde> findTopByEmployeOrderByAnneeDescMoisDesc(Employe employe);
    
    // Nouvelle méthode pour vérifier l'existence
    boolean existsByEmployeAndMoisAndAnnee(Employe employe, int mois, int annee);
    
    // Pour trouver par employe, mois et année
    Optional<MouvementSolde> findByEmployeAndMoisAndAnnee(Employe employe, int mois, int annee);
    /* ===============================
       Requêtes JPQL existantes
       =============================== */

    @Query("""
        SELECT m FROM MouvementSolde m
        WHERE m.employe = :employe
          AND m.statut = 1
          AND m.annee = :annee
        ORDER BY m.annee DESC, m.createdAt DESC
    """)
    List<MouvementSolde> findByEmployeAndAnneeAndStatutOrderByAnneeDescCreatedAtDesc(
            @Param("employe") Employe employe,
            @Param("annee") Integer annee
    );


    @Query("""
        SELECT m FROM MouvementSolde m
        WHERE m.employe = :employe
          AND m.statut = 1
        ORDER BY m.createdAt DESC
    """)
    Optional<MouvementSolde> findByEmployeAndStatutOrderByCreatedAtDesc(
            @Param("employe") Employe employe
    );


    /* ⚠️ JPQL ne supporte PAS LIMIT → corrigé proprement */
    @Query("""
        SELECT m FROM MouvementSolde m
        WHERE m.employe = :employe
          AND m.statut = 1
          AND m.annee = :annee
        ORDER BY m.createdAt DESC
    """)
    List<MouvementSolde> findLastByEmployeAndAnneeAndStatutOrderByAnneeDescCreatedAtDesc(
            @Param("employe") Employe employe,
            @Param("annee") Integer annee
    );
// findByIdEmployeAndTypeMouvementOrderByCreatedAtAsc

    List<MouvementSolde> findByEmployeAndTypeMouvementOrderByCreatedAtAsc(
            Employe employe,
            TypeEnumConge typeMouvement
    );


    Optional<MouvementSolde> findFirstByEmployeAndAnneeAndStatutOrderByCreatedAtDesc(
            Employe employe,
            Integer annee,
            Integer statut
    );


    Optional<MouvementSolde> findFirstByEmployeAndStatutOrderByMoisDescCreatedAtDesc(
            Employe employe,
            Integer statut
    );

    Optional<MouvementSolde> findFirstByEmployeAndAnneeAndStatutOrderByMoisDescCreatedAtDesc(
            Employe employe,
            Integer annee,
            Integer statut
    );
}

