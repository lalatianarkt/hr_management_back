package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.MouvementSoldePaie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MouvementSoldePaieRepository extends JpaRepository<MouvementSoldePaie, Long> {

    /**
     * Trouver un mouvement par son ID unique
     */
    Optional<MouvementSoldePaie> findByIdMouvementSolde(String idMouvementSolde);

    /**
     * Trouver tous les mouvements pour une paie spécifique
     */
    List<MouvementSoldePaie> findByIdPaie(String idPaie);

    /**
     * Trouver les mouvements dans une période de temps
     */
    @Query("SELECT m FROM MouvementSoldePaie m WHERE m.dateHeureSaisie BETWEEN :startDate AND :endDate")
    List<MouvementSoldePaie> findByDateHeureSaisieBetween(@Param("startDate") LocalDateTime startDate, 
                                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Trouver les mouvements avec pagination
     */
    Page<MouvementSoldePaie> findAll(Pageable pageable);

    /**
     * Compter le nombre de mouvements pour une paie
     */
    long countByIdPaie(String idPaie);

    /**
     * Supprimer tous les mouvements pour une paie
     */
    void deleteByIdPaie(String idPaie);

    /**
     * Trouver le dernier mouvement pour une paie
     */
    @Query("SELECT m FROM MouvementSoldePaie m WHERE m.idPaie = :idPaie ORDER BY m.dateHeureSaisie DESC")
    Optional<MouvementSoldePaie> findTopByIdPaieOrderByDateHeureSaisieDesc(@Param("idPaie") String idPaie);

    /**
     * Trouver les mouvements avec un solde de congés spécifique
     */
    List<MouvementSoldePaie> findByNbCongeDansMouvementSoldeGreaterThan(Double seuil);
}
