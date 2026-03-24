package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.TypeTempsTravail;
import com.rh.manage.Repository.TypeTempsTravailRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeTempsTravailService {
    
    @Autowired
    private TypeTempsTravailRepository repository;
    
    // Créer un nouveau type de temps de travail
    public TypeTempsTravail create(TypeTempsTravail typeTempsTravail) {
        return repository.save(typeTempsTravail);
    }
    
    // Mettre à jour un type de temps de travail
    public TypeTempsTravail update(Long id, TypeTempsTravail typeTempsTravailDetails) {
        TypeTempsTravail typeTempsTravail = findById(id)
                .orElseThrow(() -> new RuntimeException("Type de temps de travail non trouvé avec l'id: " + id));
        
        typeTempsTravail.setTempsTravail(typeTempsTravailDetails.getTempsTravail());
        typeTempsTravail.setModifiedAt(LocalDateTime.now());
        
        return repository.save(typeTempsTravail);
    }
    
    // Supprimer un type de temps de travail
    public void delete(Long id) {
        TypeTempsTravail typeTempsTravail = findById(id)
                .orElseThrow(() -> new RuntimeException("Type de temps de travail non trouvé avec l'id: " + id));
        repository.delete(typeTempsTravail);
    }
    
    // Trouver par ID
    @Transactional(readOnly = true)
    public Optional<TypeTempsTravail> findById(Long id) {
        return repository.findById(id);
    }
    
    // Trouver par temps de travail
    @Transactional(readOnly = true)
    public Optional<TypeTempsTravail> findByTempsTravail(String tempsTravail) {
        return repository.findByTempsTravail(tempsTravail);
    }
    
    // Lister tous les types triés par temps de travail
    @Transactional(readOnly = true)
    public List<TypeTempsTravail> findAll() {
        return repository.findAllByOrderByTempsTravailAsc();
    }
    
    // Rechercher des types par mot-clé
    @Transactional(readOnly = true)
    public List<TypeTempsTravail> search(String keyword) {
        return repository.findByTempsTravailContainingIgnoreCase(keyword);
    }
    
    // Vérifier si le temps de travail existe
    @Transactional(readOnly = true)
    public boolean tempsTravailExists(String tempsTravail) {
        return repository.existsByTempsTravail(tempsTravail);
    }
    
    // Vérifier si le temps de travail existe pour un autre type (pour l'update)
    @Transactional(readOnly = true)
    public boolean tempsTravailExistsForOtherType(String tempsTravail, Long id) {
        return repository.findByTempsTravail(tempsTravail)
                .map(type -> !type.getId().equals(id))
                .orElse(false);
    }
    
    // Compter le nombre total de types
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
