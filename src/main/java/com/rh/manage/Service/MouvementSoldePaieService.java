package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.MouvementSoldePaie;
import com.rh.manage.Repository.MouvementSoldePaieRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MouvementSoldePaieService {

    @Autowired
    private MouvementSoldeService mouvementSoldeService;

    @Autowired
    private MouvementSoldePaieRepository repository;

    /**
     * Créer un nouveau mouvement de solde
     */
    @Transactional
    public MouvementSoldePaie createMouvement(MouvementSoldePaie mouvement) {
        mouvement.setDateHeureSaisie(LocalDateTime.now());
        return repository.save(mouvement);
    }

    public MouvementSoldePaie createWithPaie(MouvementSoldePaie mouvement){

    }

    /**
     * Récupérer tous les mouvements avec pagination
     */
    public Page<MouvementSoldePaie> getAllMouvements(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : 
                    Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    /**
     * Récupérer tous les mouvements
     */
    public List<MouvementSoldePaie> getAllMouvements() {
        return repository.findAll();
    }

    /**
     * Récupérer un mouvement par son ID
     */
    public MouvementSoldePaie getMouvementById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement non trouvé avec l'id: " + id));
    }

    /**
     * Récupérer un mouvement par son ID unique
     */
    public MouvementSoldePaie getMouvementByIdMouvementSolde(String idMouvementSolde) {
        return repository.findByIdMouvementSolde(idMouvementSolde)
                .orElseThrow(() -> new RuntimeException("Mouvement non trouvé avec l'id: " + idMouvementSolde));
    }

    /**
     * Récupérer tous les mouvements pour une paie
     */
    public List<MouvementSoldePaie> getMouvementsByPaie(String idPaie) {
        return repository.findByIdPaie(idPaie);
    }

    /**
     * Récupérer les mouvements dans une période
     */
    public List<MouvementSoldePaie> getMouvementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByDateHeureSaisieBetween(startDate, endDate);
    }

    /**
     * Mettre à jour un mouvement
     */
    @Transactional
    public MouvementSoldePaie updateMouvement(Long id, MouvementSoldePaie mouvementDetails) {
        MouvementSoldePaie mouvement = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement non trouvé avec l'id: " + id));
        
        mouvement.setIdPaie(mouvementDetails.getIdPaie());
        mouvement.setNbCongeAReporter(mouvementDetails.getNbCongeAReporter());
        mouvement.setNbCongeDansPaie(mouvementDetails.getNbCongeDansPaie());
        mouvement.setNbCongeDansMouvementSolde(mouvementDetails.getNbCongeDansMouvementSolde());
        
        return repository.save(mouvement);
    }

    /**
     * Supprimer un mouvement
     */
    @Transactional
    public void deleteMouvement(Long id) {
        repository.deleteById(id);
    }

    /**
     * Supprimer tous les mouvements pour une paie
     */
    @Transactional
    public void deleteMouvementsByPaie(String idPaie) {
        repository.deleteByIdPaie(idPaie);
    }

    /**
     * Compter le nombre de mouvements pour une paie
     */
    public long countByPaie(String idPaie) {
        return repository.countByIdPaie(idPaie);
    }

    /**
     * Récupérer le dernier mouvement pour une paie
     */
    public MouvementSoldePaie getLastMouvementByPaie(String idPaie) {
        return repository.findTopByIdPaieOrderByDateHeureSaisieDesc(idPaie)
                .orElse(null);
    }
}
