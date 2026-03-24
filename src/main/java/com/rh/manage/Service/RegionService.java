package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Region;
import com.rh.manage.Repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RegionService {
    
    @Autowired
    private RegionRepository repository;
    
    // Créer une nouvelle région
    public Region create(Region region) {
        if (region.getId() == null) {
            region.setId(java.util.UUID.randomUUID().toString());
        }
        return repository.save(region);
    }
    
    // Mettre à jour une région
    public Region update(String id, Region regionDetails) {
        Region region = findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'id: " + id));
        
        region.setNom(regionDetails.getNom());
        region.setModifiedAt(LocalDateTime.now());
        
        return repository.save(region);
    }
    
    // Supprimer une région
    public void delete(String id) {
        Region region = findById(id)
                .orElseThrow(() -> new RuntimeException("Région non trouvée avec l'id: " + id));
        repository.delete(region);
    }
    
    // Trouver par ID
    @Transactional(readOnly = true)
    public Optional<Region> findById(String id) {
        return repository.findById(id);
    }
    
    // Trouver par nom
    @Transactional(readOnly = true)
    public Optional<Region> findByNom(String nom) {
        return repository.findByNom(nom);
    }
    
    // Lister toutes les régions triées par nom
    @Transactional(readOnly = true)
    public List<Region> findAll() {
        return repository.findAllByOrderByNomAsc();
    }
    
    // Lister toutes les régions (non triées)
    @Transactional(readOnly = true)
    public List<Region> findAllRegions() {
        return repository.findAll();
    }
    
    // Rechercher des régions par mot-clé
    @Transactional(readOnly = true)
    public List<Region> search(String keyword) {
        return repository.findByNomContainingIgnoreCase(keyword);
    }
    
    // Vérifier si le nom existe
    @Transactional(readOnly = true)
    public boolean nomExists(String nom) {
        return repository.existsByNom(nom);
    }
    
    // Vérifier si le nom existe pour une autre région (pour l'update)
    @Transactional(readOnly = true)
    public boolean nomExistsForOtherRegion(String nom, String id) {
        return repository.findByNom(nom)
                .map(region -> !region.getId().equals(id))
                .orElse(false);
    }
    
    // Compter le nombre total de régions
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}
