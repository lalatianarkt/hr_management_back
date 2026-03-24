package com.rh.manage.Service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonSerializable.Base;
import com.rh.manage.Enum.ModeCalcul;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Paie;
import com.rh.manage.Model.PaieFille;
import com.rh.manage.Model.RegleGestionConges;
import com.rh.manage.Model.RubriquePaie;
import com.rh.manage.Repository.PaieFilleRepository;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaieFilleService {
    @Autowired
    private PaieFilleRepository paieFilleRepository;

    @Autowired
    private RubriquePaieService rubriquePaieService;

    @Autowired
    private InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    private BaseIrsaService baseIrsaService;

    @Autowired
    private RegleGestionCongesService regleGestionCongesService;

    @Autowired
    private AncienneteService ancienneteService;

    @Autowired
    private PaieService paieService;

    public Optional<PaieFille> getById(Long id) {
        return paieFilleRepository.findById(id);
    }
    
    public List<PaieFille> getAll() {
        return paieFilleRepository.findAll();
    }

    public Optional<PaieFille> getByCodeAndPaieId(String code, String idPaie){
        return paieFilleRepository.findByRubrique_CodeAndPaie_Id(code, idPaie);
    }
    
    public List<PaieFille> getByPaieId(String paieId) {
        return paieFilleRepository.findByPaieId(paieId);
    }
    
    public List<PaieFille> getByPaieIdWithRubrique(String paieId) {
        return paieFilleRepository.findByPaieIdWithRubrique(paieId);
    }
    
    public List<PaieFille> getByRubriqueId(String rubriqueId) {
        return paieFilleRepository.findByRubriqueId(rubriqueId);
    }
    
    public Optional<PaieFille> getByPaieAndRubrique(String paieId, String rubriqueId) {
        return paieFilleRepository.findByPaieIdAndRubriqueId(paieId, rubriqueId);
    }
    
    public BigDecimal getTotalMontantByPaie(String paieId) {
        return paieFilleRepository.sumMontantByPaieId(paieId);
    }
    
    public long countByPaie(String paieId) {
        return paieFilleRepository.countByPaieId(paieId);
    }

    // public double calculIRSA(double salaire_base){
    //     baseIrsaService.calculerIRSA(null);
    // }

    public BigDecimal getTotalDeductibleIrsa(String paieId) {
        List<PaieFille> deductibles = paieFilleRepository.findDeductiblesIrsaByPaieId(paieId);
        return deductibles.stream()
            .map(PaieFille::getMontant)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public double calculSalaireBrutImposable(double salaire_brut, String idPaie){
        double resp = 0;
        List<PaieFille> les_imposables = paieFilleRepository.findImposablesByPaieId(idPaie);
        for (PaieFille paieFille : les_imposables) {
            resp = paieFille.getMontant().doubleValue() + resp;
        }
        double totalDeductIrsa = getTotalDeductibleIrsa(idPaie).doubleValue();

        return resp - totalDeductIrsa;
    }

    public double calculSalaireBrutTotal(String id_paie){
        double resp = 0;
        List<PaieFille> les_gains = paieFilleRepository.findGainsByPaieId(id_paie);
        for (PaieFille paieFille : les_gains) {
            resp = resp + paieFille.getMontant().doubleValue();
        }
        return resp;
    }

    public double calculSoumisCnaps(String idPaie, RubriquePaie rubriquePaie){
        double soumisCnaps = 0;
        List<PaieFille> les_paies = paieFilleRepository.findSoumisCotisationsByPaieId(idPaie);
        for (PaieFille paieFille : les_paies) {
            soumisCnaps = soumisCnaps + paieFille.getMontant().doubleValue();
                if(rubriquePaie.getPlafondMensuel() != null){
                    if(soumisCnaps >= Double.parseDouble(rubriquePaie.getPlafondMensuel())){
                    // break;
                    return Double.parseDouble(rubriquePaie.getPlafondMensuel());
                }
            }
        } 
        System.out.println("soumis cnaps : " + soumisCnaps);
        return soumisCnaps;
    }

    @Transactional
    public PaieFille create(PaieFille paieFille) throws Exception{
        PaieFille paieFilleToCreate = new PaieFille();

        RubriquePaie rubriquePaie = rubriquePaieService.getById(paieFille.getRubrique().getId()).get();
        Paie paie = paieService.getById(paieFille.getPaie().getId()).get();
        if(paieFilleRepository.findByPaieIdAndRubriqueId(paie.getId(), rubriquePaie.getId()).isPresent()){
            throw new Exception("Cette rubrique a déja été ajouté dans cette paie");
        }
        
        if(rubriquePaie.getOrdre() != 1){
           if(!paieFilleRepository.findPreviousPaieFilleByOrder
           (paieFille.getPaie().getId(), rubriquePaie.getOrdre()).isPresent()){
                throw new Exception("Veuillez saisir la rubrique par ordre. Vérifiez la rubrique précédente");
           }
        }   
        List<PaieFille> les_gains = paieFilleRepository.findGainsByPaieId(paie.getId());
        InfosProfessionnelles infoProActuel = infosProfessionnellesService.getInfosProEmploye(paie.getEmploye().getId());
        paieFilleToCreate.setPaie(paie);
        paieFilleToCreate.setRubrique(rubriquePaie);
        double salaire_base = infoProActuel.getSalaireBase();
        String modeCalculStr = rubriquePaie.getModeCalcul().name(); 
        String base = rubriquePaie.getFormule().getBase();  
        double fact = 0;   
        double montant = 1;  
        if ("AUTO".equals(modeCalculStr)) {
            if(base.equalsIgnoreCase("SALAIRE_BASE")){
               paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base));
               montant = salaire_base;
            } 
            // if(base.equalsIgnoreCase("PA")){
            //    RegleGestionConges regleGestionConges = regleGestionCongesService.getAll().get(0);
            //    regleGestionConges.getAncienneteRequis();
            //    int an = ancienneteService.calculerAncienneteEnAnnees(infoProActuel.getDateEmbauche());
            //    if(an >= regleGestionConges.getAncienneteRequisPrime()){
            //     paieFilleToCreate.setBase(BigDecimal.valueOf(regleGestionConges.getPrimeAnciennete()));
            //     montant = regleGestionConges.getPrimeAnciennete();
            //    }
            // }
            if(base.equalsIgnoreCase("AFAM")){
                paieFilleToCreate.setBase(BigDecimal.valueOf(regleGestionCongesService.getDerniereRegleGestionCongesParStatutActive().getAllocationFamiliale()));
                montant = regleGestionCongesService.getDerniereRegleGestionCongesParStatutActive().getAllocationFamiliale();
            }
            if(base.equalsIgnoreCase("SH")){
               paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base / 173.33));
               montant = salaire_base / 173.33;
            }
            if(base.equalsIgnoreCase("SJ")){
               paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base/30));
               montant = salaire_base / 30;
            }
            if(base.equalsIgnoreCase("SBRUT")){
              for (PaieFille gain : les_gains) {
                fact = fact +  gain.getMontant().doubleValue();
              }
              paieFilleToCreate.setBase(BigDecimal.valueOf(fact));
              montant = fact;
            } 
            if(base.equalsIgnoreCase("T_COT")){
                montant = calculSoumisCnaps(paie.getId(), rubriquePaie);
                paieFilleToCreate.setBase(BigDecimal.valueOf(montant));
                // paieFilleToCreate.setMontant(BigDecimal.valueOf(montant));    
            } 
            if(base.equalsIgnoreCase("IND_CONG")){
                montant
            }

            if(rubriquePaie.getFormule().getNombre() != null && rubriquePaie.getFormule().getTaux() != null){
                montant = montant * paieFille.getNombre().doubleValue() * paieFille.getTaux().doubleValue() / 100;
                // paieFilleToCreate.setMontant(BigDecimal.valueOf(montant));
            } 
            if(rubriquePaie.getFormule().getNombre() != null && rubriquePaie.getFormule().getTaux() == null){
                montant = montant * paieFille.getNombre().doubleValue();
                paieFilleToCreate.setNombre(BigDecimal.valueOf(paieFille.getNombre().doubleValue()));
                // paieFilleToCreate.setMontant(BigDecimal.valueOf(montant));
            }
            if(rubriquePaie.getFormule().getTaux() != null && rubriquePaie.getFormule().getNombre() == null){
                montant = montant * paieFille.getTaux().doubleValue() / 100;
                // System.out.println("taux : " + ato e);
                paieFilleToCreate.setTaux(BigDecimal.valueOf(paieFille.getTaux().doubleValue()));
                // paieFilleToCreate.setMontant(BigDecimal.valueOf(montant));
            }
 
            if(base.equalsIgnoreCase("SNET_IMP")){
                System.out.println("tafiditra ato au moins++++++++++++++++++");
                double sbrut_total = calculSalaireBrutTotal(paie.getId());
                System.out.println("abrut total : " + sbrut_total);                
                double base_irsa = sbrut_total;
                List<PaieFille> les_paies_deductibles_irsa = paieFilleRepository.findDeductiblesIrsaByPaieId(paie.getId());
                for (PaieFille paieFille2 : les_paies_deductibles_irsa) {
                    base_irsa = base_irsa - paieFille2.getMontant().doubleValue();
                    System.out.println("fille mandray anajara : " 
                    + paieFille2.getRubrique().getCode() + "" + "montant : " + paieFille2.getMontant());
                }
                System.out.println("base_irsa : " + base_irsa);
                paieFilleToCreate.setBase(BigDecimal.valueOf(base_irsa));
                montant = baseIrsaService.calculerIRSA(BigDecimal.valueOf(base_irsa)).doubleValue();
                System.out.println("montant irsa : " + montant);
                // paieFilleToCreate.setMontant(irsa);
            }
            
        } else if ("CALCULE".equals(modeCalculStr)) {
            if(base.equalsIgnoreCase("SALAIRE_BASE")){
               paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base));
               montant =  montant * salaire_base;
            } 
            if(base.equalsIgnoreCase("SH")){
               montant = salaire_base / 173.33;
               paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base / 173.33));
            }
            if(base.equalsIgnoreCase("SJ")){
                montant = salaire_base/30;
                paieFilleToCreate.setBase(BigDecimal.valueOf(salaire_base/30));
            }
            if(base.equalsIgnoreCase("SBRUT")){
              for (PaieFille gain : les_gains) {
                fact = fact +  gain.getMontant().doubleValue();
              }
              montant = fact;
              paieFilleToCreate.setBase(BigDecimal.valueOf(fact));
            } 

            if(rubriquePaie.getFormule().getNombre() != null){
                montant = montant * paieFille.getNombre().doubleValue();
                paieFilleToCreate.setNombre(paieFille.getNombre()); 
            }
            if(rubriquePaie.getFormule().getTaux() != null){
                montant = montant * paieFille.getTaux().doubleValue() / 100;
                paieFilleToCreate.setTaux(paieFille.getTaux());
            }
            
        } else if ("MANUEL".equals(modeCalculStr)) {
            montant = paieFille.getMontant().doubleValue();
            // paieFilleToCreate.setMontant(paieFille.getMontant());
        }
        paieFilleToCreate.setMontant(BigDecimal.valueOf(montant));
        System.out.println("nombre : " + paieFilleToCreate.getNombre());
        System.out.println("montant : " + paieFilleToCreate.getMontant());
        System.out.println("base :" + paieFilleToCreate.getBase());
        System.out.println("taux : " + paieFilleToCreate.getTaux());
                    
        return paieFilleRepository.save(paieFilleToCreate);
        // return paieFille;
    }
    
    @Transactional
    public PaieFille update(Long id, PaieFille paieFilleDetails) {
        return paieFilleRepository.findById(id)
                .map(existingPaieFille -> {
                    existingPaieFille.setTaux(paieFilleDetails.getTaux());
                    existingPaieFille.setBase(paieFilleDetails.getBase());
                    existingPaieFille.setNombre(paieFilleDetails.getNombre());
                    existingPaieFille.setModifiedAt(LocalDateTime.now());
                    
                    // Recalculer le montant
                    // calculerMontant(existingPaieFille);
                    
                    return paieFilleRepository.save(existingPaieFille);
                })
                .orElseThrow(() -> new RuntimeException("PaieFille non trouvée avec l'id: " + id));
    }
    
    @Transactional
    public void delete(Long id) {
        if (paieFilleRepository.existsById(id)) {
            paieFilleRepository.deleteById(id);
            // log.info("PaieFille avec id {} supprimée", id);
        } else {
            throw new RuntimeException("PaieFille non trouvée avec l'id: " + id);
        }
    }
    
    @Transactional
    public void deleteByPaieId(String paieId) {
        paieFilleRepository.deleteByPaieId(paieId);
        // log.info("Toutes les PaieFille pour la paie {} supprimées", paieId);
    }
    
    @Transactional
    public PaieFille createOrUpdate(Paie paie, RubriquePaie rubrique, BigDecimal base, 
                                   BigDecimal taux, BigDecimal nombre) {
        
        Optional<PaieFille> existing = paieFilleRepository
                .findByPaieIdAndRubriqueId(paie.getId(), rubrique.getId());
        
        if (existing.isPresent()) {
            PaieFille paieFille = existing.get();
            paieFille.setBase(base);
            paieFille.setTaux(taux);
            paieFille.setNombre(nombre);
            paieFille.setModifiedAt(LocalDateTime.now());
            calculerMontant(paieFille);
            return paieFilleRepository.save(paieFille);
        } else {
            PaieFille newPaieFille = new PaieFille();
            // newPaieFille.setId(generateId());
            newPaieFille.setPaie(paie);
            newPaieFille.setRubrique(rubrique);
            newPaieFille.setBase(base);
            newPaieFille.setTaux(taux);
            newPaieFille.setNombre(nombre);
            newPaieFille.setCreatedAt(LocalDateTime.now());
            newPaieFille.setModifiedAt(LocalDateTime.now());
            calculerMontant(newPaieFille);
            return paieFilleRepository.save(newPaieFille);
        }
    }
    
    private void calculerMontant(PaieFille paieFille) {
        BigDecimal montant = BigDecimal.ZERO;
        
        if (paieFille.getBase() != null && paieFille.getTaux() != null) {
            // Montant = Base * (Taux/100)
            BigDecimal tauxPourcentage = paieFille.getTaux().divide(BigDecimal.valueOf(100));
            montant = paieFille.getBase().multiply(tauxPourcentage);
            
            // Multiplier par le nombre si présent
            if (paieFille.getNombre() != null) {
                montant = montant.multiply(paieFille.getNombre());
            }
        } else if (paieFille.getMontant() != null) {
            // Si le montant est directement fourni, on l'utilise
            montant = paieFille.getMontant();
            // Multiplier par le nombre si présent
            if (paieFille.getNombre() != null) {
                montant = montant.multiply(paieFille.getNombre());
            }
        }
        
        paieFille.setMontant(montant);
    }
    
    private String generateId() {
        return "PF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
