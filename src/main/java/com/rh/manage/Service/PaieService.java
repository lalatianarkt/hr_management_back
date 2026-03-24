package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InformationSociete;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Paie;
import com.rh.manage.Model.PaieFille;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Repository.PaieRepository;
import com.rh.manage.Repository.PeriodePaieRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaieService {

    // private final Service.EmployeService employeService;
    @Autowired
    PaieRepository paieRepository;

    @Autowired
    EmployeService employeService;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    InformationSocieteService informationSocieteService;

    @Autowired
    AncienneteService ancienneteService;

    @Autowired
    MoisService moisService;

    @Autowired
    MouvementSoldeService mouvementSoldeService;

    @Autowired
    PeriodePaieRepository periodePaieRepository;
    
    // Constantes pour les statuts
    public static final int STATUT_BROUILLON = 0;
    public static final int STATUT_CLOTURE = 1;
    public static final int STATUT_VERROUILLE = 2;

    @Transactional
    public Map<String, Object> genererPaiesMultiples(PeriodePaie periodePaie, List<Paie> paies) throws Exception {
        List<Paie> saved = new ArrayList<>();
        List<String> erreurs = new ArrayList<>();
        List<String> doublons = new ArrayList<>();
        System.out.println("premier point+++++++++++++++++++++");
        try {
            List<Paie> lesNewPaies = new ArrayList<>();

            System.out.println("premier pas+++++++++++++++++++++");
            
            // Récupérer info société une fois
            List<InformationSociete> societes = informationSocieteService.getAllInformationSocietes();
            if (societes.isEmpty()) {
                throw new Exception("Aucune information de société configurée");
            }
            InformationSociete infoSociete = societes.get(0);
            System.out.println("au moins eto");
            for (Paie paie : paies) {
                try {
                    if(paieRepository.existsByEmployeIdAndPeriodeId(paie.getEmploye().getId(), paie.getPeriodePaie().getId())){
                        throw new Exception("Une paie pour cette période exsite déjà");
                    }
                    // Créer la nouvelle paie
                    Paie newPaie = new Paie();
                    System.out.println("id période créé : " + periodePaie.getId());
                    newPaie.setPeriodePaie(periodePaie);
                    // newPaie.setDateDebutPeriode(paie.getDateDebutPeriode());
                    // newPaie.setDateFinPeriode(paie.getDateFinPeriode());
                    employeService.getById(paie.getEmploye().getId());
                    newPaie.setEmploye(employeService.getById(paie.getEmploye().getId()).get());
                    informationSocieteService.getInformationSocieteById(infoSociete.getId());
                    newPaie.setInformationSociete(informationSocieteService.getInformationSocieteById(infoSociete.getId()));
                    newPaie.setStatutCloture(0); // Brouillon
                    
                    // Récupérer infos employé
                    List<InfosProfessionnelles> lesInfos = infosProfessionnellesService
                        .getInfosProfessionnellesByEmployeId(paie.getEmploye().getId());
                    
                    if (lesInfos == null || lesInfos.isEmpty()) {
                        erreurs.add("Aucune information professionnelle pour l'employé " + paie.getEmploye().getId());
                        continue;
                    }
                    
                    InfosProfessionnelles derniereInfo = lesInfos.get(0);
                    
                    // Vérifier les données nécessaires
                    if (derniereInfo.getPoste() == null) {
                        erreurs.add("Poste non défini pour l'employé " + paie.getEmploye().getId());
                        continue;
                    }
                    
                    if (derniereInfo.getMatricule() == null || derniereInfo.getMatricule().trim().isEmpty()) {
                        erreurs.add("Matricule non défini pour l'employé " + paie.getEmploye().getId());
                        continue;
                    }
                    
                    if (derniereInfo.getEmploye() == null) {
                        erreurs.add("Information employé manquante pour ID " + paie.getEmploye().getId());
                        continue;
                    }
                    
                    // Remplir les informations
                    newPaie.setFonction(derniereInfo.getPoste().getNom());
                    newPaie.setMatricule(Integer.parseInt(derniereInfo.getMatricule()));
                    newPaie.setNom(derniereInfo.getEmploye().getNom());
                    newPaie.setPrenom(derniereInfo.getEmploye().getPrenom());
                    newPaie.setDatePaiement(null);
                    newPaie.setSalaireBase(BigDecimal.valueOf(derniereInfo.getSalaireBase()));
                    // newPaie.setNumCnaps(derniereInfo.getEmploye().getNumCnaps());
                    newPaie.setDepartement(derniereInfo.getDepartement().getNom());
                    // Ancienneté
                    if (derniereInfo.getDateEmbauche() != null) {
                        String anciennete = ancienneteService.calculerAnciennete(derniereInfo.getDateEmbauche());
                        newPaie.setAncienneteAnMoisJour(anciennete);
                    }
                    
                    lesNewPaies.add(newPaie);
                    
                } catch (Exception e) {
                    erreurs.add("Erreur pour l'employé " + paie.getEmploye().getId() + ": " + e.getMessage());
                }
            }
            
            if (lesNewPaies.isEmpty()) {
                String message = "Aucune paie générée. ";
                if (!doublons.isEmpty()) message += "Doublons: " + doublons.size() + ". ";
                if (!erreurs.isEmpty()) message += "Erreurs: " + erreurs.size();
                throw new Exception(message);
            }

            System.out.println("size ++++++++ : " + lesNewPaies.size());
            
            saved = paieRepository.saveAll(lesNewPaies);
            paieRepository.flush();
            
        } catch (Exception e) {
            throw new Exception("Erreur lors de la génération: " + e.getMessage() + 
                            (erreurs.isEmpty() ? "" : " Détails: " + String.join("; ", erreurs)));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("paiesGenerees", saved);
        result.put("count", saved.size());
        result.put("erreurs", erreurs);
        result.put("doublons", doublons);
        result.put("totalTraite", paies.size());
        
        return result;
    }
    
    public Optional<LocalDate> findPeriodePaieActifParDepartement(String departement){
        return paieRepository.findDistinctDateDebutPeriodeByDepartementAndStatutClotureZero(departement);
    }  
    
    public Optional<List<Paie>> findByDepartementAndPeriodePaieId(String departement, String periodePaieId){
        return paieRepository.findByDepartementAndPeriodePaieId(departement, periodePaieId);
    }
    
    public Optional<Paie> getById(String id) {
        return paieRepository.findById(id);
    }
    
    public Optional<Paie> getByIdWithEmploye(String id) {
        return paieRepository.findByIdWithEmploye(id);
    }
    
    public List<Paie> getAll() {
        return paieRepository.findAll();
    }
    
    public Page<Paie> searchPaies(
            Integer matricule,
            String nom,
            String prenom,
            LocalDate dateDebut,
            LocalDate dateFin,
            Integer statutCloture,
            Pageable pageable) {
        
        return paieRepository.searchPaies(
                matricule, nom, prenom, 
                dateDebut, dateFin, statutCloture, 
                pageable);
    }
    
    public List<Paie> getByMatricule(Integer matricule) {
        return paieRepository.findByMatricule(matricule);
    }
    
    public List<Paie> getByEmployeId(String employeId) {
        return paieRepository.findByEmployeId(employeId);
    }

    public Optional<Paie> getDernierePaieNonCloturee(String employeId) {
        return paieRepository.findPaieActivePourEmploye(employeId);
    }
    
    public boolean hasPaieNonCloturee(String employeId) {
        return paieRepository.existsByEmployeIdAndStatutCloture(employeId, 0);
    }
    
    // public long countPaiesNonCloturees(String employeId) {
    //     return paieRepository.countPaiesNonCloturees(employeId);
    // }
    
    // public List<Paie> getByPeriode(LocalDate dateDebut, LocalDate dateFin) {
    //     return paieRepository.findByDateDebutPeriodeBetween(dateDebut, dateFin);
    // }
    
    // public List<Paie> getByMoisAndAnnee(int mois, int annee) {
    //     return paieRepository.findByDateDebutPeriodeYearAndMonth(annee, mois);
    // }
    
    // public List<Paie> getByStatutCloture(Integer statutCloture) {
    //     return paieRepository.findByStatutCloture(statutCloture);
    // }
    
    // public boolean existsPaieForEmployeInPeriode(String employeId, LocalDate dateDebut, LocalDate dateFin) {
    //     return paieRepository.existsByEmployeIdAndDateDebutPeriodeAndDateFinPeriode(employeId, dateDebut, dateFin);
    // }
    
    // public Optional<Paie> getLastPaieForEmploye(String employeId) {
    //     return paieRepository.findFirstByEmployeIdOrderByDateFinPeriodeDesc(employeId);
    // }
    
    public long countPaiesByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return paieRepository.countByPeriode(dateDebut, dateFin);
    }
    
    public BigDecimal sumSalaireBaseByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return paieRepository.sumSalaireBaseByPeriode(dateDebut, dateFin);
    }
    
    @Transactional
    public Paie create(Paie paie) {
        // Vérifier si une paie existe déjà pour cette période
        // if (paie.getEmploye() != null && 
        //     existsPaieForEmployeInPeriode(
        //         paie.getEmploye().getId(), 
        //         paie.getDateDebutPeriode(), 
        //         paie.getDateFinPeriode())) {
        //     throw new RuntimeException("Une paie existe déjà pour cet employé sur cette période");
        // }
        
        if (paie.getId() == null || paie.getId().isEmpty()) {
            paie.setId(generateId());
        }
        
        paie.setCreatedAt(LocalDateTime.now());
        paie.setModifiedAt(LocalDateTime.now());
        
        // Initialiser le statut de clôture si non défini
        if (paie.getStatutCloture() == null) {
            paie.setStatutCloture(0); // 0 = non clôturé
        }
        
        Paie savedPaie = paieRepository.save(paie);
        // log.info("Paie créée avec id: {}", savedPaie.getId());
        
        return savedPaie;
    }
    
    @Transactional
    public Paie update(String id, Paie paieDetails) {
        return paieRepository.findById(id)
                .map(existingPaie -> {
                    // Mettre à jour les champs modifiables
                    existingPaie.setModePaiement(paieDetails.getModePaiement());
                    existingPaie.setPeriodePaie(paieDetails.getPeriodePaie());
                    // existingPaie.setDateDebutPeriode(paieDetails.getDateDebutPeriode());
                    // existingPaie.setDateFinPeriode(paieDetails.getDateFinPeriode());
                    existingPaie.setDatePaiement(paieDetails.getDatePaiement());
                    existingPaie.setClassification(paieDetails.getClassification());
                    existingPaie.setSalaireBase(paieDetails.getSalaireBase());
                    existingPaie.setAncienneteAnMoisJour(paieDetails.getAncienneteAnMoisJour());
                    existingPaie.setDepartement(paieDetails.getDepartement());
                    existingPaie.setCongesPris(paieDetails.getCongesPris());
                    existingPaie.setSoldeConges(paieDetails.getSoldeConges());
                    existingPaie.setStatutCloture(paieDetails.getStatutCloture());
                    
                    existingPaie.setModifiedAt(LocalDateTime.now());
                    
                    return paieRepository.save(existingPaie);
                })
                .orElseThrow(() -> new RuntimeException("Paie non trouvée avec l'id: " + id));
    }

    public List<Paie> getByIdPeriod(String periodeId){
        return paieRepository.findByPeriodePaieId(periodeId);
    }

    @Transactional
    public void cloturePaieParPeriode(String periodeId) throws Exception{
        List<Paie> les_paies = getByIdPeriod(periodeId);
        try {
            for (Paie paie : les_paies) {
                cloturerPaie(paie.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("erreur dans clôture de paie");
        }
    }
    
    @Transactional
    public Paie cloturerPaie(String id) {
        return paieRepository.findById(id)
                .map(paie -> {
                    paie.setStatutCloture(1); // 1 = clôturé
                    paie.setModifiedAt(LocalDateTime.now());
                    
                    Paie updatedPaie = paieRepository.save(paie);
                    // log.info("Paie {} clôturée", id);
                    
                    return updatedPaie;
                })
                .orElseThrow(() -> new RuntimeException("Paie non trouvée avec l'id: " + id));
    }
    
    @Transactional
    public Paie decloturerPaie(String id) {
        return paieRepository.findById(id)
                .map(paie -> {
                    paie.setStatutCloture(0); // 0 = non clôturé
                    paie.setModifiedAt(LocalDateTime.now());
                    
                    Paie updatedPaie = paieRepository.save(paie);
                    // log.info("Paie {} déclôturée", id);
                    
                    return updatedPaie;
                })
                .orElseThrow(() -> new RuntimeException("Paie non trouvée avec l'id: " + id));
    }
    
    // public Paie calculateMontants(String paieId) {
    //     return paieRepository.findById(paieId)
    //             .map(paie -> {
    //                 // Récupérer toutes les lignes de paie filles
    //                 List<PaieFille> lignes = paieFilleService.getByPaieIdWithRubrique(paieId);
                    
    //                 // Calculer les totaux
    //                 BigDecimal totalGains = BigDecimal.ZERO;
    //                 BigDecimal totalRetenues = BigDecimal.ZERO;
                    
    //                 for (PaieFille ligne : lignes) {
    //                     // Logique pour distinguer gains et retenues selon le type de rubrique
    //                     // Vous devrez adapter cette logique selon votre modèle
    //                     if (ligne.getRubrique() != null) {
    //                         if (ligne.getRubrique().getType() != null) {
    //                             String type = ligne.getRubrique().getType().getLibelle().toLowerCase();
    //                             if (type.contains("gain") || type.contains("avantage")) {
    //                                 totalGains = totalGains.add(ligne.getMontant() != null ? ligne.getMontant() : BigDecimal.ZERO);
    //                             } else if (type.contains("retenue") || type.contains("deduction")) {
    //                                 totalRetenues = totalRetenues.add(ligne.getMontant() != null ? ligne.getMontant() : BigDecimal.ZERO);
    //                             }
    //                         }
    //                     }
    //                 }
                    
    //                 // Calculer les salaires
    //                 BigDecimal salaireBrut = paie.getSalaireBase().add(totalGains);
    //                 BigDecimal salaireNet = salaireBrut.subtract(totalRetenues);
                    
    //                 // Mettre à jour les champs calculés (transients)
    //                 paie.setTotalGains(totalGains);
    //                 paie.setTotalRetenues(totalRetenues);
    //                 paie.setSalaireBrut(salaireBrut);
    //                 paie.setSalaireNet(salaireNet);
                    
    //                 return paie;
    //             })
    //             .orElseThrow(() -> new RuntimeException("Paie non trouvée avec l'id: " + paieId));
    // }
    
    public List<Paie> getBySocieteId(Integer societeId) {
        return paieRepository.findByInformationSocieteId(societeId);
    }
    
    private String generateId() {
        return "PAIE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Optional<Paie> getLastPaieByEmploye(String idEmploye){
        return paieRepository.findFirstByEmployeIdOrderByCreatedAtDesc(idEmploye);
    }
}