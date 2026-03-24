package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.TypeEntree;
import com.rh.manage.Repository.TypeEntreeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TypeEntreeService {
    
    @Autowired
    private TypeEntreeRepository typeEntreeRepository;
    
    public TypeEntree createTypeEntree(TypeEntree typeEntree) {
        // log.info("Création d'un nouveau type d'entrée: {}", typeEntree.getLibelle());
        
        if (typeEntreeRepository.existsByLibelle(typeEntree.getLibelle())) {
            throw new RuntimeException("Un type d'entrée avec le libellé '" + 
                                     typeEntree.getLibelle() + "' existe déjà");
        }
        
        // typeEntree.setCreatedAt(LocalDateTime.now());
        
        return typeEntreeRepository.save(typeEntree);
    }
    
    public TypeEntree updateTypeEntree(String id, TypeEntree typeEntreeDetails) {
        // log.info("Mise à jour du type d'entrée avec ID: {}", id);
        
        TypeEntree existingTypeEntree = typeEntreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type d'entrée non trouvé avec l'ID: " + id));
        
        if (!existingTypeEntree.getLibelle().equals(typeEntreeDetails.getLibelle()) &&
            typeEntreeRepository.existsByLibelle(typeEntreeDetails.getLibelle())) {
            throw new RuntimeException("Un type d'entrée avec le libellé '" + 
                                     typeEntreeDetails.getLibelle() + "' existe déjà");
        }
        
        existingTypeEntree.setLibelle(typeEntreeDetails.getLibelle());
        existingTypeEntree.setModifiedAt(LocalDateTime.now());
        
        return typeEntreeRepository.save(existingTypeEntree);
    }
    
    public TypeEntree getTypeEntreeById(String id) {
        // log.info("Récupération du type d'entrée avec ID: {}", id);
        
        return typeEntreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type d'entrée non trouvé avec l'ID: " + id));
    }
    
    public List<TypeEntree> getAllTypeEntrees() {
        // log.info("Récupération de tous les types d'entrée");
        
        return typeEntreeRepository.findAll();
    }
    
    public void deleteTypeEntree(String id) {
        // log.info("Suppression du type d'entrée avec ID: {}", id);
        
        TypeEntree typeEntree = typeEntreeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type d'entrée non trouvé avec l'ID: " + id));
        
        typeEntreeRepository.delete(typeEntree);
    }
    
    public boolean existsByLibelle(String libelle) {
        return typeEntreeRepository.existsByLibelle(libelle);
    }
    
    public TypeEntree findByLibelle(String libelle) {
        return typeEntreeRepository.findByLibelle(libelle);
    }
}
