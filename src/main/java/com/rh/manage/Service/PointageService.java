package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Pointage;
import com.rh.manage.Repository.PointageRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PointageService {
    
    @Autowired
    PointageRepository pointageRepository;

    @Autowired
    EmployeService employeService;

    /**
     * Récupère les pointages d'aujourd'hui par défaut
     * Utilisé quand l'utilisateur n'a PAS appliqué de filtres
     */
    public Page<Pointage> getPointagesAujourdhuiPaginated(
            int page, 
            int size, 
            String sortBy, 
            String direction) {
        
        Pageable pageable = createPageable(page, size, sortBy, direction);
        
        // Utilise la méthode spécifique pour aujourd'hui
        return pointageRepository.findAllPointagesAujourdhui(pageable);
    }
    
    /**
     * Créer un nouveau pointage
     */
    public Pointage createPointage(Pointage pointage) {        
        // calculerValeursAutomatiques(pointage);       
        Pointage saved = pointageRepository.save(pointage);     
        return saved;
    }

    /**
     * Convertit une Page en format de réponse standard
     */
    public Map<String, Object> createPagedResponse(Page<Pointage> page) {
        return Map.of(
            "content", page.getContent(),
            "currentPage", page.getNumber(),
            "totalPages", page.getTotalPages(),
            "totalElements", page.getTotalElements(),
            "pageSize", page.getSize(),
            "first", page.isFirst(),
            "last", page.isLast(),
            "empty", page.isEmpty(),
            "sort", page.getSort().toString()
        );
    }

    /**
     * Récupère les pointages paginés par période (dates peuvent être null)
     */
    public Page<Pointage> getPointagesByPeriodPaginated(
            LocalDate startDate, 
            LocalDate endDate, 
            int page, 
            int size, 
            String sortBy, 
            String direction) {
        
        Pageable pageable = createPageable(page, size, sortBy, direction);
        
        // Si les deux dates sont null, retourner les pointages du jour
        if (startDate == null && endDate == null) {
            return pointageRepository.findPointagesDuJour(pageable);
        }
        
        return pointageRepository.findByDatePointageBetween(startDate, endDate, pageable);
    }

    /**
     * Crée un objet Pageable avec tri
     */
    private Pageable createPageable(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        return PageRequest.of(page, size, sort);
    }

    

    /**
     * Récupère les pointages paginés avec filtres
     */
    public Page<Pointage> getPointagesActivesByFiltersPaginated(
            String employeId,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sortBy,
            String direction) {
        
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return pointageRepository.findByFilters(employeId, startDate, endDate, 0, pageable);
    }

    @Transactional
    public List<Pointage> createMultiplePointages(List<Pointage> les_pointages) {
        if (les_pointages == null || les_pointages.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 2. Utiliser saveAll pour l'insertion batch
        List<Pointage> saved = pointageRepository.saveAll(les_pointages);
        
        // 3. Flush pour forcer l'écriture et libérer la mémoire
        pointageRepository.flush();
        
        return saved;
    }
    
    /**
     * Mettre à jour un pointage
     */
    @Transactional
    public Pointage updatePointage(String id, Pointage pointageDetails) {
        Pointage pointage = getPointageById(id);
        
        // Mettre à jour les champs modifiables
        pointage.setDateHeureArrivee(pointageDetails.getDateHeureArrivee());
        pointage.setDateHeureDepart(pointageDetails.getDateHeureDepart());
        pointage.setCommentaire(pointageDetails.getCommentaire());
        pointage.setStatutPointage(pointageDetails.getStatutPointage());
        pointage.setIsShiftJour(pointageDetails.getIsShiftJour());
        pointage.setIsWeekEnd(pointageDetails.getIsWeekEnd());
        pointage.setIsFerie(pointageDetails.getIsFerie());
        
        // Recalculer les durées
        calculerValeursAutomatiques(pointage);
        
        Pointage updated = pointageRepository.save(pointage);
        // log.info("Pointage mis à jour: {}", id);
        
        return updated;
    }
    
    /**
     * Obtenir un pointage par son ID
     */
    public Pointage getPointageById(String id) {
        return pointageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pointage non trouvé: " + id));
    }
    
    /**
     * Obtenir tous les pointages d'un employé
     */
    public List<Pointage> getPointagesByEmploye(String employeId) {
        return pointageRepository.findByEmployeId(employeId);
    }
    
    /**
     * Obtenir les pointages par période
     */
    public List<Pointage> getPointagesByPeriod(LocalDate startDate, LocalDate endDate) {
        return pointageRepository.findByDatePointageBetween(startDate, endDate);
    }
    
    /**
     * Obtenir les pointages d'un employé par période
     */
    public List<Pointage> getPointagesByEmployeAndPeriod(String employeId, LocalDate startDate, LocalDate endDate) {
        return pointageRepository.findByEmployeIdAndDatePointageBetween(employeId, startDate, endDate);
    }
    
    /**
     * Supprimer un pointage
     */
    @Transactional
    public void deletePointage(String id) {
        if (!pointageRepository.existsById(id)) {
            throw new RuntimeException("Pointage non trouvé: " + id);
        }
        pointageRepository.deleteById(id);
        // log.info("Pointage supprimé: {}", id);
    }
    
    /**
     * Valider un pointage (changer statut)
     */
    @Transactional
    public Pointage validerPointage(String id) {
        Pointage pointage = getPointageById(id);
        pointage.setStatutPointage(Pointage.Statut.VALIDE.getCode());
        return pointageRepository.save(pointage);
    }
    
    /**
     * Marquer un pointage comme payé
     */
    @Transactional
    public Pointage marquerPaye(String id) {
        Pointage pointage = getPointageById(id);
        pointage.setStatutPointage(Pointage.Statut.PAYE.getCode());
        return pointageRepository.save(pointage);
    }
    
    /**
     * Calculer le total d'heures travaillées pour un employé sur une période
     */
    public Double getTotalHeuresTravaillees(String employeId, LocalDate startDate, LocalDate endDate) {
        Double total = pointageRepository.sumHeuresTravailleesByEmployeAndPeriod(employeId, startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    /**
     * Calculer les valeurs automatiques (durées, etc.)
     */
    private void calculerValeursAutomatiques(Pointage pointage) {
        if (pointage.getDateHeureArrivee() != null && pointage.getDateHeureDepart() != null) {
            // Calculer la durée travaillée en minutes
            long minutes = ChronoUnit.MINUTES.between(
                    pointage.getDateHeureArrivee(), 
                    pointage.getDateHeureDepart());
            
            if (minutes > 0) {
                pointage.setDureeHeureTravailleeMinute((double) minutes);
            }
            
            // Vérifier si c'est un week-end
            if (pointage.getDatePointage() != null) {
                pointage.setIsWeekEnd(
                        pointage.getDatePointage().getDayOfWeek().getValue() >= 6);
            }
            
            // Initialiser le statut si non défini
            if (pointage.getStatutPointage() == null) {
                pointage.setStatutPointage(Pointage.Statut.BROUILLON.getCode());
            }
        }
    }
    
    /**
     * Générer un ID unique pour le pointage
     */
    private String generatePointageId() {
        return "PTG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() + 
               "-" + System.currentTimeMillis() % 10000;
    }
}