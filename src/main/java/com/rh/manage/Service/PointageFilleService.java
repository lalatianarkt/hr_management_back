package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.PointageFille;
import com.rh.manage.Repository.PointageFilleRepository;
import com.rh.manage.Repository.PointageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PointageFilleService {
    
    @Autowired
    PointageFilleRepository pointageFilleRepository;

    @Autowired
    PointageRepository pointageRepository;
    
    /**
     * Créer un pointage fille
     */
    @Transactional
    public PointageFille createPointageFille(PointageFille pointageFille) {
        // Générer un ID si non fourni
        if (pointageFille.getId() == null || pointageFille.getId().isEmpty()) {
            pointageFille.setId(generatePointageFilleId());
        }
        
        // Vérifier que le pointage parent existe
        String pointageId = pointageFille.getPointage().getId();
        if (!pointageRepository.existsById(pointageId)) {
            throw new IllegalArgumentException("Pointage parent non trouvé: " + pointageId);
        }
        
        // Validation du type d'action
        if (pointageFille.getTypeAction() != null) {
            PointageFille.TypeAction type = PointageFille.TypeAction.fromCode(pointageFille.getTypeAction());
            if (type == null) {
                throw new IllegalArgumentException("Type d'action invalide: " + pointageFille.getTypeAction());
            }
        }
        
        // Sauvegarder
        PointageFille saved = pointageFilleRepository.save(pointageFille);
        // log.info("Pointage fille créé: {} pour le pointage {}", saved.getId(), pointageId);
        
        return saved;
    }
    
    /**
     * Créer plusieurs pointages filles
     */
    @Transactional
    public List<PointageFille> createMultiplePointageFilles(List<PointageFille> pointageFilles) {
        if (pointageFilles == null || pointageFilles.isEmpty()) {
            return Collections.emptyList();
        }
        
        long startTime = System.currentTimeMillis();
                
        // 2. Batch insert via saveAll() (déjà optimisé)
        List<PointageFille> saved = pointageFilleRepository.saveAll(pointageFilles);
        
        // 3. Flush explicit pour libérer mémoire et valider
        pointageFilleRepository.flush();
        
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Inserté {} pointages filles en {} ms" + " size" + saved.size() + " duration "+ duration);
        
        return saved;
    }
    
    /**
     * Mettre à jour un pointage fille
     */
    @Transactional
    public PointageFille updatePointageFille(String id, PointageFille pointageFilleDetails) {
        PointageFille pointageFille = getPointageFilleById(id);
        
        // Mettre à jour les champs modifiables
        pointageFille.setDateHeurePointage(pointageFilleDetails.getDateHeurePointage());
        pointageFille.setTypeAction(pointageFilleDetails.getTypeAction());
        pointageFille.setSource(pointageFilleDetails.getSource());
        
        // Si changement de pointage parent
        if (pointageFilleDetails.getPointage() != null && 
            !pointageFilleDetails.getPointage().getId().equals(pointageFille.getPointage().getId())) {
            throw new IllegalArgumentException("Le pointage parent ne peut pas être modifié");
        }
        
        PointageFille updated = pointageFilleRepository.save(pointageFille);
        // log.info("Pointage fille mis à jour: {}", id);
        
        return updated;
    }
    
    /**
     * Obtenir un pointage fille par son ID
     */
    public PointageFille getPointageFilleById(String id) {
        return pointageFilleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointage fille non trouvé: " + id));
    }
    
    /**
     * Obtenir tous les pointages filles d'un pointage parent
     */
    public List<PointageFille> getPointagesFillesByPointage(String pointageId) {
        return pointageFilleRepository.findByPointageIdOrderByDateHeurePointageAsc(pointageId);
    }
    
    /**
     * Obtenir les pointages filles d'un pointage parent ordonnés
     */
    public List<PointageFille> getPointagesFillesByPointageOrdered(String pointageId) {
        return pointageFilleRepository.findByPointageIdOrderByDateHeurePointageAsc(pointageId);
    }
    
    /**
     * Obtenir les pointages filles par type d'action
     */
    public List<PointageFille> getPointagesFillesByType(String typeAction) {
        return pointageFilleRepository.findByTypeAction(typeAction);
    }
    
    /**
     * Obtenir les pointages filles par source
     */
    public List<PointageFille> getPointagesFillesBySource(String source) {
        return pointageFilleRepository.findBySource(source);
    }
    
    /**
     * Obtenir les pointages filles par période
     */
    public List<PointageFille> getPointagesFillesByPeriod(LocalDateTime start, LocalDateTime end) {
        return pointageFilleRepository.findByDateHeurePointageBetween(start, end);
    }
    
    /**
     * Supprimer un pointage fille
     */
    @Transactional
    public void deletePointageFille(String id) {
        if (!pointageFilleRepository.existsById(id)) {
            throw new RuntimeException("Pointage fille non trouvé: " + id);
        }
        pointageFilleRepository.deleteById(id);
        // log.info("Pointage fille supprimé: {}", id);
    }
    
    /**
     * Vérifier si un pointage a des pointages filles
     */
    public boolean hasPointagesFilles(String pointageId) {
        return pointageFilleRepository.existsByPointageId(pointageId);
    }
    
    /**
     * Compter les pointages filles d'un pointage
     */
    public long countPointagesFilles(String pointageId) {
        return pointageFilleRepository.countByPointageId(pointageId);
    }
    
    /**
     * Obtenir le premier pointage fille d'un pointage parent
     */
    public PointageFille getFirstPointageFille(String pointageId) {
        return pointageFilleRepository.findFirstByPointageIdOrderByDateHeurePointageAsc(pointageId);
    }
    
    /**
     * Obtenir le dernier pointage fille d'un pointage parent
     */
    public PointageFille getLastPointageFille(String pointageId) {
        return pointageFilleRepository.findFirstByPointageIdOrderByDateHeurePointageDesc(pointageId);
    }
    
    /**
     * Obtenir les entrées sans sorties correspondantes
     */
    public List<PointageFille> getEntreesSansSortie() {
        return pointageFilleRepository.findEntreesSansSortie();
    }
    
    /**
     * Générer un ID unique pour le pointage fille
     */
    private String generatePointageFilleId() {
        return "PTF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + 
               "-" + System.currentTimeMillis() % 10000;
    }
}