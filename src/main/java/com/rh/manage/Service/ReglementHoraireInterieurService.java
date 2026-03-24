package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.ReglementHoraireInterieur;
import com.rh.manage.Repository.ReglementHoraireInterieurRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

@Service
@Transactional
public class ReglementHoraireInterieurService {
    
    @Autowired
    private ReglementHoraireInterieurRepository reglementHoraireInterieurRepository;
    
    // Récupérer tous les règlements
    public List<ReglementHoraireInterieur> getAllReglements() {
        return reglementHoraireInterieurRepository.findAll();
    }
     
    public ReglementHoraireInterieur getReglementActif(){
        return reglementHoraireInterieurRepository.findByStatut(0);
    }
    
    // Récupérer un règlement par ID
    public ReglementHoraireInterieur getReglementById(Long id) {
        return reglementHoraireInterieurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Règlement horaire non trouvé avec l'ID: " + id));
    }
    
    // Créer un nouveau règlement
    public ReglementHoraireInterieur createReglement(ReglementHoraireInterieur reglement) {
        // Validation des données
        validateReglement(reglement);
        
        // Vérifier s'il existe déjà un règlement avec les mêmes heures
        if (reglementHoraireInterieurRepository.existsByHeureMatEntreeAndHeureApremSortie(
                reglement.getHeureMatEntree(), reglement.getHeureApremSortie())) {
            throw new RuntimeException("Un règlement avec ces heures existe déjà");
        }
        
        // Calculer les heures normales si non fournies
        calculerHeuresNormales(reglement);
        
        return reglementHoraireInterieurRepository.save(reglement);
    }
    
    // Mettre à jour un règlement existant
    public ReglementHoraireInterieur updateReglement(Long id, ReglementHoraireInterieur reglementUpdate) {
        ReglementHoraireInterieur existingReglement = getReglementById(id);
        
        // Validation des données
        validateReglement(reglementUpdate);
        
        // Mettre à jour les champs
        existingReglement.setHeureMatEntree(reglementUpdate.getHeureMatEntree());
        existingReglement.setHeureApremSortie(reglementUpdate.getHeureApremSortie());
        existingReglement.setHeureNormaleJournaliere(reglementUpdate.getHeureNormaleJournaliere());
        existingReglement.setHeureNormaleSemaine(reglementUpdate.getHeureNormaleSemaine());
        existingReglement.setHeureNormaleMois(reglementUpdate.getHeureNormaleMois());
        existingReglement.setDureeNormalePauseMinutes(reglementUpdate.getDureeNormalePauseMinutes());
        
        // Mettre à jour la date de modification
        existingReglement.setModifiedAt(LocalDateTime.now());
        
        // Recalculer si nécessaire
        calculerHeuresNormales(existingReglement);
        
        return reglementHoraireInterieurRepository.save(existingReglement);
    }
    
    // Supprimer un règlement
    public void deleteReglement(Long id) {
        ReglementHoraireInterieur reglement = getReglementById(id);
        reglementHoraireInterieurRepository.delete(reglement);
    }
    
    // Récupérer le règlement actif (le plus récent)
    public ReglementHoraireInterieur getCurrentReglement() {
        return reglementHoraireInterieurRepository.findFirstByOrderByCreatedAtDesc();
    }
    
    // Validation des données
    private void validateReglement(ReglementHoraireInterieur reglement) {
        if (reglement.getHeureMatEntree() == null) {
            throw new RuntimeException("L'heure d'entrée matinale est obligatoire");
        }
        
        if (reglement.getHeureApremSortie() == null) {
            throw new RuntimeException("L'heure de sortie de l'après-midi est obligatoire");
        }
        
        // Vérifier que l'heure de sortie est après l'heure d'entrée
        if (reglement.getHeureApremSortie().isBefore(reglement.getHeureMatEntree())) {
            throw new RuntimeException("L'heure de sortie doit être après l'heure d'entrée");
        }
    }
    
    // Calculer automatiquement les heures normales
    public void calculerHeuresNormales(ReglementHoraireInterieur reglement) {
        // Si l'heure journalière n'est pas fournie, la calculer
        if (reglement.getHeureNormaleJournaliere() == null && 
            reglement.getHeureMatEntree() != null && 
            reglement.getHeureApremSortie() != null &&
            reglement.getDureeNormalePauseMinutes() != null) {
            
            // Calcul de l'heure journalière
            LocalTime entree = reglement.getHeureMatEntree();
            LocalTime sortie = reglement.getHeureApremSortie();
            
            // Calcul des heures travaillées
            long minutesTravaillees = Duration.between(entree, sortie).toMinutes();
            BigDecimal minutesTravailleesBD = BigDecimal.valueOf(minutesTravaillees);
            BigDecimal pauseMinutes = reglement.getDureeNormalePauseMinutes();
            
            // Soustraire la pause
            BigDecimal minutesEffectives = minutesTravailleesBD.subtract(pauseMinutes);
            BigDecimal heuresJournalieres = minutesEffectives.divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
            
            reglement.setHeureNormaleJournaliere(heuresJournalieres);
            
            // Calculer l'heure hebdomadaire (5 jours)
            BigDecimal heuresHebdomadaires = heuresJournalieres.multiply(BigDecimal.valueOf(5));
            long minutesHebdo = heuresHebdomadaires.multiply(BigDecimal.valueOf(60)).longValue();
            reglement.setHeureNormaleSemaine(LocalTime.of((int)(minutesHebdo / 60), (int)(minutesHebdo % 60)));
            
            // Calculer l'heure mensuelle (22 jours)
            BigDecimal heuresMensuelles = heuresJournalieres.multiply(BigDecimal.valueOf(22));
            long minutesMensuelles = heuresMensuelles.multiply(BigDecimal.valueOf(60)).longValue();
            reglement.setHeureNormaleMois(LocalTime.of((int)(minutesMensuelles / 60), (int)(minutesMensuelles % 60)));
        }
    }
    
    // Vérifier si une heure est dans les heures normales (version avec LocalTime)
    public boolean isWithinWorkingHours(LocalTime heure) {
        ReglementHoraireInterieur current = getCurrentReglement();
        if (current == null) return false;
        
        LocalTime entree = current.getHeureMatEntree();
        LocalTime sortie = current.getHeureApremSortie();
        
        return !heure.isBefore(entree) && !heure.isAfter(sortie);
    }
    
    // Vérifier si une heure (String) est dans les heures normales
    public boolean isWithinWorkingHours(String heureStr) {
        try {
            LocalTime heure = LocalTime.parse(heureStr);
            return isWithinWorkingHours(heure);
        } catch (Exception e) {
            throw new RuntimeException("Format d'heure invalide: " + heureStr + ". Utilisez HH:mm");
        }
    }
    
    // Calculer les heures à partir de paramètres de base
    public ReglementHoraireInterieur calculerHeuresFromParams(LocalTime heureEntree, LocalTime heureSortie, BigDecimal pauseMinutes) {
        ReglementHoraireInterieur temp = new ReglementHoraireInterieur();
        temp.setHeureMatEntree(heureEntree);
        temp.setHeureApremSortie(heureSortie);
        temp.setDureeNormalePauseMinutes(pauseMinutes);
        
        calculerHeuresNormales(temp);
        
        return temp;
    }
    
    // Calculer les heures à partir de Strings
    public ReglementHoraireInterieur calculerHeuresFromStrings(String heureEntreeStr, String heureSortieStr, String pauseMinutesStr) {
        try {
            LocalTime heureEntree = LocalTime.parse(heureEntreeStr);
            LocalTime heureSortie = LocalTime.parse(heureSortieStr);
            BigDecimal pauseMinutes = new BigDecimal(pauseMinutesStr);
            
            return calculerHeuresFromParams(heureEntree, heureSortie, pauseMinutes);
        } catch (Exception e) {
            throw new RuntimeException("Données invalides pour le calcul: " + e.getMessage());
        }
    }
    
    // Calculer la durée de travail journalière
    public BigDecimal calculateDailyWorkingHours(ReglementHoraireInterieur reglement) {
        if (reglement.getHeureMatEntree() == null || reglement.getHeureApremSortie() == null) {
            return BigDecimal.ZERO;
        }
        
        Duration duration = Duration.between(reglement.getHeureMatEntree(), reglement.getHeureApremSortie());
        long minutes = duration.toMinutes();
        
        if (reglement.getDureeNormalePauseMinutes() != null) {
            minutes -= reglement.getDureeNormalePauseMinutes().longValue();
        }
        
        return BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
    }
}