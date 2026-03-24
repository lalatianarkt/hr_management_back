package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.RegleGestionConges;
import com.rh.manage.Model.SoldeAnnuel;
import com.rh.manage.Model.TypeEnumConge;
import com.rh.manage.Repository.SoldeAnnuelRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SoldeAnnuelService {

    @Autowired
    CalculSoldeService calculSoldeService;

    @Autowired
    EmployeService employeService;

    @Autowired
    MouvementSoldeService mouvementSoldeService;

    @Autowired
    RegleGestionCongesService regleGestionCongesService;

    private final SoldeAnnuelRepository repository;

    public SoldeAnnuelService(SoldeAnnuelRepository repository) {
        this.repository = repository;
    }

    public List<SoldeAnnuel> getAllSolde() {
        return repository.findAll();
    }

    public Optional<SoldeAnnuel> getSoldeById(String id) {
        return repository.findById(id);
    }

    public List<SoldeAnnuel> getSoldeByEmploye(String idEmploye) {
        return repository.findByIdEmploye(idEmploye);
    }

    public SoldeAnnuel saveSolde(SoldeAnnuel solde) {
        return repository.save(solde);
    }

    public void deleteSolde(String id) {
        repository.deleteById(id);
    }

    @Transactional
public List<SoldeAnnuel> clotureGlobale(int annee) {
    List<Employe> les_employes = employeService.getAll();
    List<SoldeAnnuel> soldesClotures = new ArrayList<>();
    
    int anneeProchaine = annee + 1;
    int moisJanvierProchain = 1; // Janvier
    
    for (Employe employe : les_employes) {
        try {
            System.out.println("🧾 Traitement employé ID: " + employe.getId() + " - " + employe.getNom());
            
            // 1. Créer et sauvegarder le solde annuel pour l'année en cours
            SoldeAnnuel soldeAnnuel = new SoldeAnnuel();
            soldeAnnuel.setIdEmploye(employe.getId());
            soldeAnnuel.setAnnee(annee);
            soldeAnnuel.setDateCloture(LocalDate.now());
            
            Optional<MouvementSolde> dernierMouvementOpt = mouvementSoldeService
                    .getDernierMouvementSoldeParEmployeEtParAnnee(employe, annee);
            
            if (dernierMouvementOpt.isPresent()) {
                MouvementSolde dernierMouvement = dernierMouvementOpt.get();
                soldeAnnuel.setNbCongePris(dernierMouvement.getNbCongePris());
                soldeAnnuel.setNbCongeRestant(dernierMouvement.getNbCongeRestant());
                soldeAnnuel.setNbCongeTotal(dernierMouvement.getNbCongeTotal());
                
                System.out.println("📊 Solde trouvé - Restant: " + dernierMouvement.getNbCongeRestant());
            } else {
                BigDecimal soldeConge = calculSoldeService.calculSoldeCongeParAnciennete(employe);
                soldeAnnuel.setNbCongePris(0.0);
                soldeAnnuel.setNbCongeRestant(soldeConge.doubleValue());
                soldeAnnuel.setNbCongeTotal(soldeConge.doubleValue());
                
                System.out.println("🆕 Solde calculé - Total: " + soldeConge.doubleValue());
            }
            
            soldeAnnuel.setStatutCloture(1);
            SoldeAnnuel saved = saveSolde(soldeAnnuel);
            soldesClotures.add(saved);
            
            System.out.println("✅ Solde annuel " + annee + " sauvegardé");
            
            // 2. Créer un mouvement de solde pour janvier de l'année prochaine
            creerMouvementSoldeJanvierProchain(employe, soldeAnnuel, anneeProchaine, moisJanvierProchain);
            
            // 3. Reporter les congés pour l'année prochaine
            reporterCongeAnnuel(employe, annee);
            
        } catch (Exception e) {
            System.out.println("❌ Erreur pour employé " + employe.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    System.out.println("🎯 Clôture globale terminée pour " + annee + 
                      " - " + soldesClotures.size() + " soldes traités");
    return soldesClotures;
}

private void creerMouvementSoldeJanvierProchain(Employe employe, SoldeAnnuel soldeAnnuel, 
                                                int anneeProchaine, int moisJanvierProchain) {
    try {
        // Vérifier si un mouvement existe déjà pour janvier de l'année prochaine
        boolean mouvementExiste = mouvementSoldeService
            .existeMouvementPourMoisEtAnnee(employe, moisJanvierProchain, anneeProchaine);
        
        if (mouvementExiste) {
            System.out.println("⚠️ Mouvement existe déjà pour " + employe.getId() + 
                             " - " + moisJanvierProchain + "/" + anneeProchaine);
            return;
        }
        
        // Créer le nouveau mouvement pour janvier prochain
        MouvementSolde mouvementJanvier = new MouvementSolde();
        mouvementJanvier.setEmploye(employe);
        mouvementJanvier.setTypeMouvement(TypeEnumConge.REPORT);
        mouvementJanvier.setStatut(1); // Validé
        mouvementJanvier.setMois(moisJanvierProchain);
        mouvementJanvier.setAnnee(anneeProchaine);
        
        // Récupérer la règle de gestion pour l'acquisition mensuelle
        RegleGestionConges regles = regleGestionCongesService.getDerniereRegleGestionCongesParStatutActive();
        double acquisitionMensuelle = regles != null ? regles.getSoldeMensuel() : 2.5; // Valeur par défaut
        
        // Calculer le nouveau solde
        double soldeReporte = soldeAnnuel.getNbCongeRestant();
        double nouvellesAcquisitions = acquisitionMensuelle; // Pour janvier
        
        double nouveauTotal = soldeReporte + nouvellesAcquisitions;
        double nouveauRestant = nouveauTotal; // Au début de l'année, aucun congé pris
        
        // Appliquer la limite si nécessaire (ex: maximum 30 jours)
        double limiteMax = regles != null ? regles.getLimiteReportAnnuel() : 90.0;
        if (nouveauTotal > limiteMax) {
            System.out.println("📉 Limite atteinte: " + nouveauTotal + " > " + limiteMax);
            nouveauTotal = limiteMax;
            nouveauRestant = limiteMax;
        }
        
        mouvementJanvier.setNbCongeTotal(nouveauTotal);
        mouvementJanvier.setNbCongeRestant(nouveauRestant);
        mouvementJanvier.setNbCongePris(0.0);
        mouvementJanvier.setCommentaire("Report annuel de " + soldeAnnuel.getAnnee() + 
                                       " vers " + anneeProchaine);
        
        // Sauvegarder le mouvement
        MouvementSolde savedMouvement = mouvementSoldeService.saveMouvement(mouvementJanvier);
        
        System.out.println("📅 Mouvement créé pour " + employe.getId() + 
                         " - " + moisJanvierProchain + "/" + anneeProchaine +
                         " - Solde: " + nouveauTotal + " jours");
        
        // Optionnel: créer aussi un mouvement pour décembre de l'année en cours
        creerMouvementClotureDecembre(employe, soldeAnnuel, anneeProchaine);
        
    } catch (Exception e) {
        System.out.println("❌ Erreur création mouvement janvier " + anneeProchaine + 
                          " pour employé " + employe.getId() + ": " + e.getMessage());
    }
}

private void creerMouvementClotureDecembre(Employe employe, SoldeAnnuel soldeAnnuel, int anneeProchaine) {
    try {
        int moisDecembre = 12;
        int anneeEnCours = soldeAnnuel.getAnnee();
        
        // Créer un mouvement de clôture pour décembre
        MouvementSolde mouvementCloture = new MouvementSolde();
        mouvementCloture.setEmploye(employe);
        mouvementCloture.setTypeMouvement(TypeEnumConge.REPORT);
        mouvementCloture.setStatut(1);
        mouvementCloture.setMois(moisDecembre);
        mouvementCloture.setAnnee(anneeEnCours);
        mouvementCloture.setNbCongeTotal(soldeAnnuel.getNbCongeTotal());
        mouvementCloture.setNbCongeRestant(soldeAnnuel.getNbCongeRestant());
        mouvementCloture.setNbCongePris(soldeAnnuel.getNbCongePris());
        mouvementCloture.setCommentaire("Clôture annuelle " + anneeEnCours + 
                                       " - Report vers " + anneeProchaine);
        
        mouvementSoldeService.saveMouvement(mouvementCloture);
        
        System.out.println("🔚 Mouvement clôture décembre créé pour " + employe.getId());
        
    } catch (Exception e) {
        System.out.println("⚠️ Erreur création mouvement clôture: " + e.getMessage());
    }
}

    public SoldeAnnuel reporterCongeAnnuel(Employe employe, int annee){
       SoldeAnnuel nouveauSoldeAnnuel = new SoldeAnnuel();
       nouveauSoldeAnnuel.setAnnee(annee + 1);
       nouveauSoldeAnnuel.setDateCloture(LocalDate.now());
       nouveauSoldeAnnuel.setIdEmploye(employe.getId());
       nouveauSoldeAnnuel.setStatutCloture(0); //ouvert
       SoldeAnnuel soldeAnnuel = getSoldeByEmployeAndAnnee(employe.getId(), annee).get();
       double reste = soldeAnnuel.getNbCongeRestant();
       RegleGestionConges regleGestionConges = regleGestionCongesService.getDerniereRegleGestionCongesParStatutActive();
       double limiteReport = regleGestionConges.getLimiteReportAnnuel();
       if(reste - limiteReport > 0){
         nouveauSoldeAnnuel.setNbCongePris(0.0);
         nouveauSoldeAnnuel.setNbCongeRestant(limiteReport);
         nouveauSoldeAnnuel.setNbCongeTotal(limiteReport);
       } else if(reste - limiteReport < 0){
         nouveauSoldeAnnuel.setNbCongePris(0.0);
         nouveauSoldeAnnuel.setNbCongeRestant(reste);
         nouveauSoldeAnnuel.setNbCongeTotal(reste);
       } else{
         nouveauSoldeAnnuel.setNbCongePris(0.0);
         nouveauSoldeAnnuel.setNbCongeRestant(0.0);
         nouveauSoldeAnnuel.setNbCongeTotal(0.0);
       }
       repository.save(nouveauSoldeAnnuel);
       return nouveauSoldeAnnuel;
    }

    public List<SoldeAnnuel> getSoldeByAnnee(Integer annee) {
        System.out.println("🔍 Service: Recherche des soldes pour l'année " + annee);
        
        List<SoldeAnnuel> result = repository.findByAnnee(annee);
        
        System.out.println("📊 Service: " + (result != null ? result.size() : 0) + 
                          " soldes trouvés pour l'année " + annee);
        
        return result;
    }

    // Méthode pour récupérer par employé ET année
    public Optional<SoldeAnnuel> getSoldeByEmployeAndAnnee(String idEmploye, Integer annee) {
        return repository.findByIdEmployeAndAnnee(idEmploye, annee);
    }

    // Méthode pour récupérer les années disponibles
    public List<Integer> getAnneesDisponibles() {
        List<Integer> annees = repository.findDistinctAnnees();
        System.out.println("📅 Service: " + annees.size() + " années disponibles trouvées");
        return annees;
    }

    // Méthode avec tri
    public List<SoldeAnnuel> getSoldeByAnneeOrdered(Integer annee) {
        return repository.findByAnneeOrderByEmploye(annee);
    }
}
