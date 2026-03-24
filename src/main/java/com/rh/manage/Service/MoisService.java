package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Mois;
import com.rh.manage.Repository.MoisRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoisService {
    @Autowired
    MoisRepository moisRepository;
    
    /**
     * Créer un nouveau mois
     */
    @Transactional
    public Mois createMois(Mois mois) {
        // Validation
        validateMois(mois);
        
        // Vérifier l'unicité du libellé
        if (moisRepository.existsByLibelle(mois.getLibelle())) {
            throw new IllegalArgumentException("Un mois avec ce libellé existe déjà");
        }
        
        // Dates
        LocalDateTime now = LocalDateTime.now();
        mois.setCreatedAt(now);
        mois.setModifiedAt(now);
        
        return moisRepository.save(mois);
    }
    
    /**
     * Mettre à jour un mois
     */
    @Transactional
    public Mois updateMois(Integer id, Mois moisDetails) {
        Mois mois = getMoisById(id);
        
        // Validation
        validateMois(moisDetails);
        
        // Vérifier l'unicité du libellé (excluant l'ID courant)
        if (moisRepository.existsByLibelleAndIdNot(moisDetails.getLibelle(), id)) {
            throw new IllegalArgumentException("Un autre mois avec ce libellé existe déjà");
        }
        
        // Mettre à jour les champs
        mois.setLibelle(moisDetails.getLibelle());
        mois.setModifiedAt(LocalDateTime.now());
        
        return moisRepository.save(mois);
    }
    
    /**
     * Supprimer un mois
     */
    @Transactional
    public void deleteMois(Integer id) {
        if (!moisRepository.existsById(id)) {
            throw new RuntimeException("Mois non trouvé avec ID: " + id);
        }
        moisRepository.deleteById(id);
    }
    
    /**
     * Obtenir un mois par son ID
     */
    public Mois getMoisById(Integer id) {
        return moisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mois non trouvé avec ID: " + id));
    }
    
    /**
     * Obtenir un mois par son libellé
     */
    public Mois getMoisByLibelle(String libelle) {
        return moisRepository.findByLibelle(libelle)
                .orElseThrow(() -> new RuntimeException("Mois non trouvé avec libellé: " + libelle));
    }
    
    /**
     * Obtenir tous les mois
     */
    public List<Mois> getAllMois() {
        return moisRepository.findAll();
    }
    
    /**
     * Rechercher un mois par libellé
     */
    public Mois searchByLibelle(String libelle) {
        return moisRepository.findByLibelleContainingIgnoreCase(libelle)
                .orElseThrow(() -> new RuntimeException("Aucun mois trouvé avec le libellé contenant: " + libelle));
    }
    
    /**
     * Valider les données d'un mois
     */
    private void validateMois(Mois mois) {
        if (mois.getLibelle() == null || mois.getLibelle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé est obligatoire");
        }
        
        if (mois.getLibelle().length() > 20) {
            throw new IllegalArgumentException("Le libellé ne peut pas dépasser 20 caractères");
        }
    }
}
