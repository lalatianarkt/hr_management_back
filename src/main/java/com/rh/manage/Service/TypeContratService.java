package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.TypeContrat;
import com.rh.manage.Repository.TypeContratRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TypeContratService {

    @Autowired
    private TypeContratRepository typeContratRepository;

    // Récupérer tous les types de contrats ordonnés par intitulé
    public List<TypeContrat> getAllTypeContrats() {
        return typeContratRepository.findAllByOrderByIntitule();
    }

    // Récupérer un type de contrat par son ID
    public Optional<TypeContrat> getTypeContratById(String id) {
        return typeContratRepository.findById(id);
    }

    // Récupérer un type de contrat par son intitulé
    public Optional<TypeContrat> getTypeContratByIntitule(String intitule) {
        return typeContratRepository.findByIntitule(intitule);
    }

    // Créer un nouveau type de contrat
    public TypeContrat createTypeContrat(TypeContrat typeContrat) {
        // Vérifier si le type de contrat existe déjà
        if (typeContratRepository.existsByIntitule(typeContrat.getIntitule())) {
            throw new RuntimeException("Un type de contrat avec l'intitulé '" + typeContrat.getIntitule() + "' existe déjà");
        }
        
        return typeContratRepository.save(typeContrat);
    }

    // Mettre à jour un type de contrat existant
    // public TypeContrat updateTypeContrat(String id, TypeContrat typeContratDetails) {
    //     Optional<TypeContrat> typeContratOptional = typeContratRepository.findById(id);
        
    //     if (typeContratOptional.isPresent()) {
    //         TypeContrat typeContrat = typeContratOptional.get();
            
    //         // Vérifier si le nouvel intitulé n'est pas déjà utilisé par un autre type de contrat
    //         if (!typeContrat.getIntitule().equals(typeContratDetails.getIntitule()) && 
    //             typeContratRepository.existsByIntitule(typeContratDetails.getIntitule())) {
    //             throw new RuntimeException("Un type de contrat avec l'intitulé '" + typeContratDetails.getIntitule() + "' existe déjà");
    //         }
            
    //         // Mettre à jour les champs
    //         typeContrat.setIntitule(typeContratDetails.getIntitule());
    //         typeContrat.setDescription(typeContratDetails.getDescription());
            
    //         return typeContratRepository.save(typeContrat);
    //     } else {
    //         throw new RuntimeException("Type de contrat non trouvé avec l'ID: " + id);
    //     }
    // }

    // Supprimer un type de contrat
    // public void deleteTypeContrat(String id) {
    //     Optional<TypeContrat> typeContrat = typeContratRepository.findById(id);
        
    //     if (typeContrat.isPresent()) {
    //         TypeContrat tc = typeContrat.get();
            
    //         // Vérifier s'il y a des contrats associés à ce type
    //         if (tc.getContrats() != null && !tc.getContrats().isEmpty()) {
    //             throw new RuntimeException("Impossible de supprimer ce type de contrat : il est utilisé par " + 
    //                                       tc.getContrats().size() + " contrat(s)");
    //         }
            
    //         typeContratRepository.deleteById(id);
    //     } else {
    //         throw new RuntimeException("Type de contrat non trouvé avec l'ID: " + id);
    //     }
    // }

    // Rechercher des types de contrats
    // public List<TypeContrat> searchTypeContrats(String searchTerm) {
    //     return typeContratRepository.searchTypeContrats(searchTerm);
    // }

    // Rechercher par intitulé
    public List<TypeContrat> searchByIntitule(String intitule) {
        return typeContratRepository.findByIntituleContainingIgnoreCase(intitule);
    }

    // Rechercher par description
    public List<TypeContrat> searchByDescription(String description) {
        return typeContratRepository.findByDescriptionContainingIgnoreCase(description);
    }

    // Vérifier si un type de contrat existe
    public boolean typeContratExists(String intitule) {
        return typeContratRepository.existsByIntitule(intitule);
    }

    // Compter le nombre de types de contrats
    public long countTypeContrats() {
        return typeContratRepository.count();
    }

    // Récupérer les types de contrats actifs (avec des contrats associés)
    // public List<TypeContrat> getTypesContratAvecContrats() {
    //     return typeContratRepository.findTypesContratAvecContrats();
    // }

    // // Récupérer les types de contrats les plus utilisés
    // public List<Object[]> getTypesContratPlusUtilises() {
    //     return typeContratRepository.findTypesContratPlusUtilises();
    // }

    // Vérifier si un type de contrat peut être supprimé (pas de contrats associés)
    // public boolean canDeleteTypeContrat(String id) {
    //     Optional<TypeContrat> typeContrat = typeContratRepository.findById(id);
    //     if (typeContrat.isPresent()) {
    //         TypeContrat tc = typeContrat.get();
    //         return tc.getContrats() == null || tc.getContrats().isEmpty();
    //     }
    //     return false;
    // }
}
