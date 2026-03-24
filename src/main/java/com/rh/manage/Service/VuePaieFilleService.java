package com.rh.manage.Service;

import com.rh.manage.Model.VuePaieFille;
import com.rh.manage.Repository.VuePaieFilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class VuePaieFilleService {
    
    @Autowired
    private VuePaieFilleRepository repository;
    
    // Récupérer toutes les lignes
    public List<VuePaieFille> getAll() {
        return repository.findAll();
    }
    
    // Récupérer par ID de paie
    public List<VuePaieFille> getByIdPaie(String idPaie) {
        return repository.findByIdPaieOrderByOrdre(idPaie);
    }
    
    // Récupérer par code de rubrique
    public List<VuePaieFille> getByCode(String code) {
        return repository.findByCode(code);
    }
    
    // Récupérer une ligne spécifique
    public VuePaieFille getByIdPaieAndCode(String idPaie, String code) {
        return repository.findByIdPaieAndCode(idPaie, code);
    }
    
    // Calculer le total des montants pour une paie
    public BigDecimal getTotalMontantByIdPaie(String idPaie) {
        return repository.sumMontantByIdPaie(idPaie);
    }
    
    // Obtenir les totaux par code de rubrique
    public Map<String, BigDecimal> getTotalsByCode() {
        List<Object[]> results = repository.sumMontantByCode();
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        
        for (Object[] row : results) {
            String code = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            totals.put(code, total);
        }
        
        return totals;
    }
    
    // Obtenir les statistiques par paie
    public List<Map<String, Object>> getStatsByPaie() {
        List<Object[]> results = repository.getStatsByPaie();
        List<Map<String, Object>> stats = new ArrayList<>();
        
        for (Object[] row : results) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("idPaie", row[0]);
            stat.put("nombreRubriques", row[1]);
            stat.put("totalMontant", row[2]);
            stat.put("tauxMoyen", row[3]);
            stats.add(stat);
        }
        
        return stats;
    }
    
    // Obtenir les codes distincts
    public List<String> getDistinctCodes() {
        return repository.findDistinctCodes();
    }
    
    // Obtenir les ID de paies distincts
    public List<String> getDistinctIdPaies() {
        return repository.findDistinctIdPaies();
    }
    
    // Rechercher les rubriques qui dépassent les plafonds
    // public Map<String, List<VuePaieFille>> getRubriquesExceeded() {
    //     Map<String, List<VuePaieFille>> result = new HashMap<>();
        
    //     result.put("plafondMensuel", repository.findExceedingPlafondMensuel());
    //     result.put("plafondAnnuel", repository.findExceedingPlafondAnnuel());
    //     result.put("sansPlafond", repository.findWithoutPlafond());
        
    //     return result;
    // }
    
    // Calculer les totaux par préfixe de code
    public Map<String, Map<String, Object>> getTotalsByCodePrefix() {
        List<Object[]> results = repository.getTotalsByCodePrefix();
        Map<String, Map<String, Object>> totals = new HashMap<>();
        
        for (Object[] row : results) {
            String prefix = (String) row[0];
            Long count = (Long) row[1];
            BigDecimal sum = (BigDecimal) row[2];
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("nombre", count);
            stats.put("total", sum);
            stats.put("moyenne", count > 0 ? sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
            
            totals.put(prefix, stats);
        }
        
        return totals;
    }
    
    // Obtenir un résumé pour une paie
    public Map<String, Object> getResumeForPaie(String idPaie) {
        List<VuePaieFille> lignes = repository.findByIdPaieOrderByOrdre(idPaie);
        Map<String, Object> resume = new HashMap<>();
        
        BigDecimal totalMontant = BigDecimal.ZERO;
        BigDecimal totalBase = BigDecimal.ZERO;
        int nombreRubriques = 0;
        List<String> codes = new ArrayList<>();
        
        for (VuePaieFille ligne : lignes) {
            totalMontant = totalMontant.add(ligne.getMontant() != null ? ligne.getMontant() : BigDecimal.ZERO);
            totalBase = totalBase.add(ligne.getBase() != null ? ligne.getBase() : BigDecimal.ZERO);
            nombreRubriques++;
            codes.add(ligne.getCode());
        }
        
        resume.put("idPaie", idPaie);
        resume.put("nombreRubriques", nombreRubriques);
        resume.put("totalMontant", totalMontant);
        resume.put("totalBase", totalBase);
        resume.put("codesRubriques", codes);
        resume.put("montantMoyen", nombreRubriques > 0 ? 
            totalMontant.divide(BigDecimal.valueOf(nombreRubriques), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
        
        return resume;
    }
    
    // Vérifier si une rubrique existe pour une paie
    public boolean existsRubriqueForPaie(String idPaie, String code) {
        VuePaieFille ligne = repository.findByIdPaieAndCode(idPaie, code);
        return ligne != null;
    }
    
    // Obtenir les rubriques groupées par paie
    public Map<String, List<VuePaieFille>> getGroupedByPaie() {
        List<VuePaieFille> allLignes = repository.findAll();
        Map<String, List<VuePaieFille>> grouped = new HashMap<>();
        
        for (VuePaieFille ligne : allLignes) {
            grouped.computeIfAbsent(ligne.getIdPaie(), k -> new ArrayList<>()).add(ligne);
        }
        
        return grouped;
    }

}
