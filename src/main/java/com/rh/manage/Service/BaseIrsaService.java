package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.BaseIrsa;
import com.rh.manage.Repository.BaseIrsaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BaseIrsaService {
    
    @Autowired
    private BaseIrsaRepository baseIrsaRepository;
    
    // CRUD Operations
    
    @Transactional
    public BaseIrsa create(BaseIrsa baseIrsa) {
        // Vérifier si le numéro de tranche existe déjà
        if (baseIrsa.getNumTranche() != null && 
            baseIrsaRepository.existsByNumTranche(baseIrsa.getNumTranche())) {
            throw new IllegalArgumentException("Le numéro de tranche " + baseIrsa.getNumTranche() + " existe déjà");
        }
        
        return baseIrsaRepository.save(baseIrsa);
    }
    
    @Transactional(readOnly = true)
    public List<BaseIrsa> findAll() {
        return baseIrsaRepository.findAllByOrderByNumTrancheAsc();
    }
    
    @Transactional(readOnly = true)
    public Optional<BaseIrsa> findById(String id) {
        return baseIrsaRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<BaseIrsa> findByNumTranche(Integer numTranche) {
        return baseIrsaRepository.findByNumTranche(numTranche);
    }
    
    @Transactional
    public BaseIrsa update(String id, BaseIrsa baseIrsaDetails) {
        return baseIrsaRepository.findById(id)
                .map(existing -> {
                    // Vérifier si on change le numéro de tranche
                    if (baseIrsaDetails.getNumTranche() != null && 
                        !baseIrsaDetails.getNumTranche().equals(existing.getNumTranche()) &&
                        baseIrsaRepository.existsByNumTranche(baseIrsaDetails.getNumTranche())) {
                        throw new IllegalArgumentException("Le numéro de tranche " + 
                                baseIrsaDetails.getNumTranche() + " existe déjà");
                    }
                    
                    // Mettre à jour les champs
                    if (baseIrsaDetails.getTrancheMin() != null) {
                        existing.setTrancheMin(baseIrsaDetails.getTrancheMin());
                    }
                    if (baseIrsaDetails.getTrancheMax() != null) {
                        existing.setTrancheMax(baseIrsaDetails.getTrancheMax());
                    }
                    if (baseIrsaDetails.getTaux() != null) {
                        existing.setTaux(baseIrsaDetails.getTaux());
                    }
                    if (baseIrsaDetails.getNumTranche() != null) {
                        existing.setNumTranche(baseIrsaDetails.getNumTranche());
                    }
                    
                    existing.setModifiedAt(LocalDateTime.now());
                    
                    return baseIrsaRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("BaseIrsa non trouvée avec l'ID: " + id));
    }
    
    @Transactional
    public void delete(String id) {
        if (!baseIrsaRepository.existsById(id)) {
            throw new IllegalArgumentException("BaseIrsa non trouvée avec l'ID: " + id);
        }
        baseIrsaRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteByNumTranche(Integer numTranche) {
        Optional<BaseIrsa> baseIrsa = baseIrsaRepository.findByNumTranche(numTranche);
        if (baseIrsa.isPresent()) {
            baseIrsaRepository.delete(baseIrsa.get());
        } else {
            throw new IllegalArgumentException("Aucune tranche trouvée avec le numéro: " + numTranche);
        }
    }
    
    // Business Logic
    
    @Transactional(readOnly = true)
    public Optional<BaseIrsa> findTrancheForMontant(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        return baseIrsaRepository.findTrancheForMontant(montant);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal calculerIRSA(BigDecimal revenuImposable) {
        if (revenuImposable == null || revenuImposable.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        List<BaseIrsa> tranches = baseIrsaRepository.findAllByOrderByNumTrancheAsc();
        if (tranches.isEmpty()) {
            throw new IllegalStateException("Aucune tranche IRSA configurée");
        }
        
        BigDecimal impotTotal = BigDecimal.ZERO;
        BigDecimal montantRestant = revenuImposable;
        
        // Trier les tranches par ordre croissant
        tranches.sort(Comparator.comparing(BaseIrsa::getNumTranche));
        
        for (BaseIrsa tranche : tranches) {
            if (montantRestant.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            // Pour la dernière tranche (trancheMax = null)
            if (tranche.getTrancheMax() == null) {
                BigDecimal base = montantRestant.subtract(tranche.getTrancheMin() != null ? 
                        tranche.getTrancheMin() : BigDecimal.ZERO);
                if (base.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal impotTranche = base.multiply(tranche.getTaux())
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    impotTotal = impotTotal.add(impotTranche);
                }
                break;
            }
            
            // Pour les tranches intermédiaires
            BigDecimal limiteTranche = tranche.getTrancheMax().subtract(
                    tranche.getTrancheMin() != null ? tranche.getTrancheMin() : BigDecimal.ZERO);
            BigDecimal montantTranche = montantRestant.min(limiteTranche);
            
            if (montantTranche.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal impotTranche = montantTranche.multiply(tranche.getTaux())
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                impotTotal = impotTotal.add(impotTranche);
                montantRestant = montantRestant.subtract(montantTranche);
            }
        }
        
        return impotTotal;
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getRecapCalculIRSA(BigDecimal revenuImposable) {
        if (revenuImposable == null || revenuImposable.compareTo(BigDecimal.ZERO) <= 0) {
            Map<String, Object> recap = new HashMap<>();
            recap.put("revenuImposable", BigDecimal.ZERO);
            recap.put("impotTotal", BigDecimal.ZERO);
            recap.put("tranches", new ArrayList<>());
            return recap;
        }
        
        List<BaseIrsa> tranches = baseIrsaRepository.findAllByOrderByNumTrancheAsc();
        List<Map<String, Object>> detailsTranches = new ArrayList<>();
        BigDecimal impotTotal = BigDecimal.ZERO;
        BigDecimal montantRestant = revenuImposable;
        
        tranches.sort(Comparator.comparing(BaseIrsa::getNumTranche));
        
        for (BaseIrsa tranche : tranches) {
            if (montantRestant.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            
            Map<String, Object> detailTranche = new HashMap<>();
            detailTranche.put("numTranche", tranche.getNumTranche());
            detailTranche.put("trancheMin", tranche.getTrancheMin());
            detailTranche.put("trancheMax", tranche.getTrancheMax());
            detailTranche.put("taux", tranche.getTaux() + "%");
            
            BigDecimal impotTranche = BigDecimal.ZERO;
            
            if (tranche.getTrancheMax() == null) {
                // Dernière tranche
                BigDecimal base = montantRestant.subtract(tranche.getTrancheMin() != null ? 
                        tranche.getTrancheMin() : BigDecimal.ZERO);
                if (base.compareTo(BigDecimal.ZERO) > 0) {
                    impotTranche = base.multiply(tranche.getTaux())
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    montantRestant = BigDecimal.ZERO;
                }
            } else {
                // Tranche intermédiaire
                BigDecimal limiteTranche = tranche.getTrancheMax().subtract(
                        tranche.getTrancheMin() != null ? tranche.getTrancheMin() : BigDecimal.ZERO);
                BigDecimal montantTranche = montantRestant.min(limiteTranche);
                
                if (montantTranche.compareTo(BigDecimal.ZERO) > 0) {
                    impotTranche = montantTranche.multiply(tranche.getTaux())
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    montantRestant = montantRestant.subtract(montantTranche);
                }
            }
            
            detailTranche.put("montantTranche", impotTranche);
            detailsTranches.add(detailTranche);
            impotTotal = impotTotal.add(impotTranche);
        }
        
        Map<String, Object> recap = new HashMap<>();
        recap.put("revenuImposable", revenuImposable);
        recap.put("impotTotal", impotTotal);
        recap.put("tauxEffectif", impotTotal.divide(revenuImposable, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")));
        recap.put("tranches", detailsTranches);
        
        return recap;
    }
    
    @Transactional(readOnly = true)
    public boolean validateTranches() {
        List<BaseIrsa> tranches = baseIrsaRepository.findAllByOrderByNumTrancheAsc();
        
        if (tranches.isEmpty()) {
            return false;
        }
        
        // Vérifier que les tranches sont continues
        for (int i = 0; i < tranches.size() - 1; i++) {
            BaseIrsa current = tranches.get(i);
            BaseIrsa next = tranches.get(i + 1);
            
            if (current.getTrancheMax() == null) {
                return false; // Seule la dernière tranche peut avoir trancheMax = null
            }
            
            if (!current.getTrancheMax().equals(next.getTrancheMin())) {
                return false; // Les tranches doivent se suivre
            }
        }
        
        return true;
    }
    
    @Transactional
    public void reorderTranches() {
        List<BaseIrsa> tranches = baseIrsaRepository.findAllByOrderByNumTrancheAsc();
        
        for (int i = 0; i < tranches.size(); i++) {
            BaseIrsa tranche = tranches.get(i);
            if (tranche.getNumTranche() != i + 1) {
                tranche.setNumTranche(i + 1);
                tranche.setModifiedAt(LocalDateTime.now());
                baseIrsaRepository.save(tranche);
            }
        }
    }
}
