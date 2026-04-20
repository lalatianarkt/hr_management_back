package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Mouvement;
import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.MouvementSoldePaie;
import com.rh.manage.Model.PaieFille;
import com.rh.manage.Repository.MouvementSoldePaieRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MouvementSoldePaieService {

    @Autowired
    private MouvementSoldeService mouvementSoldeService;

    @Autowired
    private MouvementSoldePaieRepository repository;

    @Autowired
    private EmployeService employeService;

    /**
     * Créer un nouveau mouvement de solde
     */
    @Transactional
    public MouvementSoldePaie createMouvement(MouvementSoldePaie mouvement) {
        return repository.save(mouvement);
    }

    @Transactional
    public MouvementSoldePaie createWithPaie(MouvementSoldePaie mouvement, PaieFille paieFille){ 
        String idEmp = mouvement.getIdEmploye();
        Employe employe = employeService.getById(idEmp).get();
        int anneeActuel = LocalDateTime.now().getYear();
        double nb_conge_dans_paie = Double.parseDouble(paieFille.getNombre().toString());
        System.out.println("nombre : " + paieFille.getNombre());
        mouvement.setNbCongeDansPaie(nb_conge_dans_paie);
        System.out.println("dernierMvt : " + mouvementSoldeService.getDernierMouvementSoldeParEmployeEtParAnnee(employe, anneeActuel).isPresent());
        if(mouvementSoldeService.getDernierMouvementSoldeParEmployeEtParAnnee(employe, anneeActuel).isPresent()){
            MouvementSolde mvtSolde = mouvementSoldeService.getDernierMouvementSoldeParEmployeEtParAnnee(employe, anneeActuel).get();
            double nb_conge_pris_reel = mvtSolde.getNbCongePris();

            mouvement.setIdMouvementSolde(mvtSolde.getId());
            mouvement.setNbCongeDansMouvementSolde(nb_conge_pris_reel);

            if(nb_conge_pris_reel - nb_conge_dans_paie == 0){
                mouvement.setNbCongeAReporter(0.0);
            } else { //que ce soit sup ou inf
                mouvement.setNbCongeAReporter(nb_conge_pris_reel - nb_conge_dans_paie); 
            } 
        } else {
            mouvement.setNbCongeDansMouvementSolde(0.0);
            mouvement.setNbCongeAReporter(0.0);
        }
        MouvementSoldePaie mvtInserted = createMouvement(mouvement);
        return mvtInserted;
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
    public MouvementSoldePaie getMouvementById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement non trouvé avec l'id: " + id));
    }

    /**
     * Récupérer un mouvement par son ID unique
     */
    public MouvementSoldePaie getMouvementByIdMouvementSolde(int idMouvementSolde) {
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
    public MouvementSoldePaie updateMouvement(int id, MouvementSoldePaie mouvementDetails) {
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
    public void deleteMouvement(int id) {
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
