package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.PointageFille;
import com.rh.manage.Model.Pointage;
import com.rh.manage.Repository.PointageFilleRepository;
import com.rh.manage.Repository.PointageRepository;
import com.rh.manage.Model.ReglementHoraireInterieur;
import java.math.BigDecimal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
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

    @Autowired
    ReglementHoraireInterieurService reglementHoraireInterieurService;
    
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
    public PointageFille updatePointageFille(String id, PointageFille pointageFilleDetails, String commentaire) {
        if (commentaire == null || commentaire.trim().isEmpty()) {
            throw new IllegalArgumentException("Le commentaire est obligatoire pour toute modification");
        }
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
        recalculerPointageParent(pointageFille.getPointage().getId(), commentaire);
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
     * Obtenir les pointages filles d'un employÃ© pour une date donnÃ©e
     */
    public List<PointageFille> getPointagesFillesByEmployeAndDate(String employeId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return pointageFilleRepository
                .findByEmployeIdAndDateBetweenOrderByDateHeurePointageAsc(employeId, start, end);
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

    private void recalculerPointageParent(String pointageId, String commentaire) {
        Pointage pointage = pointageRepository.findById(pointageId)
                .orElseThrow(() -> new RuntimeException("Pointage parent non trouvÃ©: " + pointageId));

        List<PointageFille> filles = pointageFilleRepository
                .findByPointageIdOrderByDateHeurePointageAsc(pointageId);

        if (filles.isEmpty()) {
            pointage.setDureeHeureTravailleeMinute(0.0);
            pointage.setDureeRetardMinute(0.0);
            pointageRepository.save(pointage);
            return;
        }

        LocalDateTime heureArrivee = filles.get(0).getDateHeurePointage();
        LocalDateTime heureDepart = filles.get(filles.size() - 1).getDateHeurePointage();

        pointage.setDateHeureArrivee(heureArrivee);
        pointage.setDateHeureDepart(heureDepart);

        long totalMinutes = Duration.between(heureArrivee, heureDepart).toMinutes();
        if (totalMinutes < 0) totalMinutes = 0;

        ReglementHoraireInterieur reglement = reglementHoraireInterieurService.getReglementActif();
        LocalTime heureEntreeNormale = reglement != null ? reglement.getHeureMatEntree() : null;
        LocalTime heureSortieNormale = reglement != null ? reglement.getHeureApremSortie() : null;
        BigDecimal pauseNormaleMinutes = reglement != null ? reglement.getDureeNormalePauseMinutes() : null;
        long pauseSeuil = pauseNormaleMinutes != null ? pauseNormaleMinutes.longValue() : 60;

        long pauseMinutes = 0;
        long retardPause = 0;

        for (int i = 1; i < filles.size() - 1; i += 2) {
            LocalDateTime debutPause = filles.get(i).getDateHeurePointage();
            LocalDateTime finPause = filles.get(i + 1).getDateHeurePointage();
            long gap = Duration.between(debutPause, finPause).toMinutes();
            if (gap > 0) {
                pauseMinutes += gap;
                if (gap > pauseSeuil) {
                    retardPause += (gap - pauseSeuil);
                }
            }
        }

        long travailMinutes = totalMinutes - pauseMinutes;
        if (travailMinutes < 0) travailMinutes = 0;

        long retardEntree = 0;
        if (heureEntreeNormale != null && heureArrivee.toLocalTime().isAfter(heureEntreeNormale)) {
            retardEntree = Duration.between(heureEntreeNormale, heureArrivee.toLocalTime()).toMinutes();
        }

        pointage.setDureeHeureTravailleeMinute((double) travailMinutes);
        pointage.setDureeRetardMinute((double) (retardEntree + retardPause));

        long minutesSup = 0;
        if (heureSortieNormale != null && heureDepart.toLocalTime().isAfter(heureSortieNormale)) {
            minutesSup = Duration.between(heureSortieNormale, heureDepart.toLocalTime()).toMinutes();
        }
        pointage.setDureeHeureSupplementaire((double) Math.max(0, minutesSup));

        String prefix = "[MODIF " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "] ";
        String existing = pointage.getCommentaire();
        if (existing != null && !existing.trim().isEmpty()) {
            pointage.setCommentaire(existing + "\n" + prefix + commentaire.trim());
        } else {
            pointage.setCommentaire(prefix + commentaire.trim());
        }

        pointageRepository.save(pointage);
    }

    /**
     * Générer un ID unique pour le pointage fille
     */
    private String generatePointageFilleId() {
        return "PTF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + 
               "-" + System.currentTimeMillis() % 10000;
    }
}
