package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Abreviation;
import com.rh.manage.Repository.AbreviationRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AbreviationService {
    
    @Autowired
    private AbreviationRepository abreviationRepository;
    
    // Créer une nouvelle abréviation
    public Abreviation createAbreviation(Abreviation abreviation) {
        if (abreviationRepository.existsByAbreviation(abreviation.getAbreviation())) {
            throw new IllegalArgumentException("Cette abréviation existe déjà: " + abreviation.getAbreviation());
        }
        return abreviationRepository.save(abreviation);
    }
    
    // Récupérer toutes les abréviations
    public List<Abreviation> getAllAbreviations() {
        return abreviationRepository.findAllByOrderByLibelleAsc();
    }
    
    // Récupérer une abréviation par son ID
    public Optional<Abreviation> getAbreviationById(Long id) {
        return abreviationRepository.findById(id);
    }
    
    // Récupérer une abréviation par son code
    public Optional<Abreviation> getAbreviationByCode(String code) {
        return abreviationRepository.findByAbreviation(code);
    }
    
    // Mettre à jour une abréviation
    public Abreviation updateAbreviation(Long id, Abreviation updatedAbreviation) {
        return abreviationRepository.findById(id)
                .map(abreviation -> {
                    // Vérifier si l'abréviation est modifiée et si elle existe déjà
                    if (!abreviation.getAbreviation().equals(updatedAbreviation.getAbreviation()) 
                        && abreviationRepository.existsByAbreviation(updatedAbreviation.getAbreviation())) {
                        throw new IllegalArgumentException("Cette abréviation existe déjà: " + updatedAbreviation.getAbreviation());
                    }
                    
                    abreviation.setLibelle(updatedAbreviation.getLibelle());
                    abreviation.setAbreviation(updatedAbreviation.getAbreviation());
                    abreviation.setDescription(updatedAbreviation.getDescription());
                    
                    return abreviationRepository.save(abreviation);
                })
                .orElseThrow(() -> new RuntimeException("Abréviation non trouvée avec l'ID: " + id));
    }
    
    // Supprimer une abréviation
    public void deleteAbreviation(Long id) {
        if (!abreviationRepository.existsById(id)) {
            throw new RuntimeException("Abréviation non trouvée avec l'ID: " + id);
        }
        abreviationRepository.deleteById(id);
    }
    
    // Rechercher par mot-clé
    public List<Abreviation> searchAbreviations(String keyword) {
        return abreviationRepository.searchByKeyword(keyword);
    }
    
    // Rechercher par libellé
    public List<Abreviation> searchByLibelle(String libelle) {
        return abreviationRepository.findByLibelleContainingIgnoreCase(libelle);
    }
    
    // Vérifier si une abréviation existe
    public boolean existsByAbreviation(String abreviation) {
        return abreviationRepository.existsByAbreviation(abreviation);
    }
    
    // Récupérer toutes les abréviations triées par code
    public List<Abreviation> getAllOrderedByAbreviation() {
        return abreviationRepository.findAllByOrderByAbreviationAsc();
    }
}
