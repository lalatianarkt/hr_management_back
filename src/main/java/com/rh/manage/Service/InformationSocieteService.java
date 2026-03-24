package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.InformationSociete;
import com.rh.manage.Repository.InformationSocieteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InformationSocieteService {
    @Autowired
    InformationSocieteRepository informationSocieteRepository;
    
    /**
     * Créer une nouvelle information de société
     */
    @Transactional
    public InformationSociete createInformationSociete(InformationSociete informationSociete) {
        // Validation
        validateInformationSociete(informationSociete);
        
        // Vérifier l'unicité du nom
        if (informationSocieteRepository.existsByNomCompany(informationSociete.getNomCompany())) {
            throw new IllegalArgumentException("Une société avec ce nom existe déjà");
        }
        
        // Dates
        LocalDateTime now = LocalDateTime.now();
        informationSociete.setCreatedAt(now);
        informationSociete.setModifiedAt(now);
        
        return informationSocieteRepository.save(informationSociete);
    }
    
    /**
     * Mettre à jour une information de société
     */
    @Transactional
    public InformationSociete updateInformationSociete(Integer id, InformationSociete informationSocieteDetails) {
        InformationSociete informationSociete = getInformationSocieteById(id);
        
        // Validation
        validateInformationSociete(informationSocieteDetails);
        
        // Vérifier l'unicité du nom (excluant l'ID courant)
        InformationSociete existing = informationSocieteRepository.findByNomCompany(informationSocieteDetails.getNomCompany()).orElse(null);
        if (existing != null && !existing.getId().equals(id)) {
            throw new IllegalArgumentException("Une autre société avec ce nom existe déjà");
        }
        
        // Mettre à jour les champs
        informationSociete.setNomCompany(informationSocieteDetails.getNomCompany());
        informationSociete.setLogo(informationSocieteDetails.getLogo());
        informationSociete.setModifiedAt(LocalDateTime.now());
        
        return informationSocieteRepository.save(informationSociete);
    }
    
    /**
     * Supprimer une information de société
     */
    @Transactional
    public void deleteInformationSociete(Integer id) {
        if (!informationSocieteRepository.existsById(id)) {
            throw new RuntimeException("Information société non trouvée avec ID: " + id);
        }
        informationSocieteRepository.deleteById(id);
    }
    
    /**
     * Obtenir une information de société par son ID
     */
    public InformationSociete getInformationSocieteById(Integer id) {
        return informationSocieteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Information société non trouvée avec ID: " + id));
    }
    
    /**
     * Obtenir une information de société par son nom
     */
    public InformationSociete getInformationSocieteByNom(String nomCompany) {
        return informationSocieteRepository.findByNomCompany(nomCompany)
                .orElseThrow(() -> new RuntimeException("Information société non trouvée avec nom: " + nomCompany));
    }
    
    /**
     * Obtenir toutes les informations de société
     */
    public List<InformationSociete> getAllInformationSocietes() {
        return informationSocieteRepository.findAll();
    }
    
    /**
     * Obtenir la première société (pour système mono-société)
     */
    public InformationSociete getFirstSociete() {
        return informationSocieteRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Aucune société trouvée"));
    }
    
    /**
     * Rechercher une société par nom
     */
    public InformationSociete searchByNom(String nomCompany) {
        return informationSocieteRepository.findByNomCompanyContainingIgnoreCase(nomCompany)
                .orElseThrow(() -> new RuntimeException("Aucune société trouvée avec le nom contenant: " + nomCompany));
    }
    
    /**
     * Valider les données d'une information de société
     */
    private void validateInformationSociete(InformationSociete informationSociete) {
        if (informationSociete.getNomCompany() == null || informationSociete.getNomCompany().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la société est obligatoire");
        }
        
        if (informationSociete.getNomCompany().length() > 150) {
            throw new IllegalArgumentException("Le nom de la société ne peut pas dépasser 150 caractères");
        }
        
        if (informationSociete.getLogo() != null && informationSociete.getLogo().length() > 150) {
            throw new IllegalArgumentException("Le chemin du logo ne peut pas dépasser 150 caractères");
        }
    }
}
