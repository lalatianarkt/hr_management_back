package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.PeriodePaie;
import com.rh.manage.Model.RegleGestionConges;
import com.rh.manage.Model.TypeEnumConge;
import com.rh.manage.Repository.MouvementSoldeRepository;
import com.rh.manage.Repository.PeriodePaieRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MouvementSoldeService {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private AncienneteService ancienneteService;

    @Autowired
    private InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    RegleGestionCongesService regleGestionCongesService;

    @Autowired
    PeriodePaieRepository periodePaieRepository;

    private final MouvementSoldeRepository repository;

    public MouvementSoldeService(MouvementSoldeRepository repository) {
        this.repository = repository;
    }

    public List<MouvementSolde> getAllMouvements() {
        return repository.findAll();
    }

    public Optional<MouvementSolde> getMouvementById(int id) {
        return repository.findById(id);
    }

    public List<MouvementSolde> getMouvementsByEmploye(Employe employe) {
        return repository.findByEmploye(employe);
    }

    public List<MouvementSolde> getMouvementsByType(TypeEnumConge typeMouvement) {
        return repository.findByTypeMouvement(typeMouvement);
    }

    public MouvementSolde saveMouvement(MouvementSolde mouvement) {
        return repository.save(mouvement);
    }

    public void deleteMouvement(int id) {
        repository.deleteById(id);
    }

    public Optional<MouvementSolde> getDernierMouvementSoldeParEmployeEtParAnnee(Employe employe, int annee){
        return repository.findFirstByEmployeAndAnneeAndStatutOrderByMoisDescCreatedAtDesc(employe, annee, 1);
    } 

    public Optional<MouvementSolde> findByEmployeAndStatutOrderByCreatedAtDesc(Employe employe){
        return repository.findFirstByEmployeAndStatutOrderByMoisDescCreatedAtDesc(employe, 1);
    }
    
    

    // ✅ CHAQUE MOIS (cron job automatique)
    
    
    // AUJOURD'HUI À 11H HEURE MADAGASCAR
    // @Scheduled(cron = "0 05 16 * * *", zone = "Indian/Antananarivo") Ceci c pour 16h 15
    @Scheduled(cron = "0 0 1 1 * *") // Le 1er de chaque mois à minuit 
    // @Scheduled(cron = "0 30 15 * * *")  // 15:30 tous les jours
    // @Transactional 
    public void ajouterCongesMensuel() {
        System.out.println("💰 Ajout des 2.5 jours/mois...");

        List<Employe> employesActifs = employeService.getEmployesActifs();

        for (Employe employe : employesActifs) {
            try {
                // Création du nouveau mouvement
                MouvementSolde nouveauMouvement = new MouvementSolde();
                // nouveauMouvement.setIdEmploye(employe.getId());
                nouveauMouvement.setEmploye(employe);
                nouveauMouvement.setAnnee(2026); // ou LocalDate.now().getYear()
                nouveauMouvement.setMois(1); // ou LocalDate.now().getMonthValue()
                nouveauMouvement.setCommentaire("Acquisition mensuelle 2.5 jours");
                nouveauMouvement.setTypeMouvement(TypeEnumConge.ACQUISITION);
                nouveauMouvement.setStatut(1); // si applicable

                // Récupérer le dernier mouvement s'il existe
                Optional<MouvementSolde> optDernierMouvement =
                        findByEmployeAndStatutOrderByCreatedAtDesc(employe);

                if (optDernierMouvement.isPresent()) {
                    MouvementSolde dernierMouvement = optDernierMouvement.get();
                    double ancienSolde = dernierMouvement.getNbCongeRestant();
                    double nouveauSolde = ancienSolde + 2.5;
                    nouveauMouvement.setNbCongeTotal(dernierMouvement.getNbCongeTotal() + 2.5);
                    nouveauMouvement.setNbCongeRestant(nouveauSolde);
                    nouveauMouvement.setNbCongePris(dernierMouvement.getNbCongePris());
                    System.out.println("💰 Mise à jour pour " + employe.getId() + " - Nouveau solde : " + nouveauSolde);
                } else {
                    // Aucun mouvement précédent, initialisation
                    nouveauMouvement.setNbCongeTotal(2.5);
                    nouveauMouvement.setNbCongeRestant(2.5);
                    nouveauMouvement.setNbCongePris(0.0);
                    System.out.println("⚠️ Aucun mouvement précédent, initialisation pour " + employe.getId());
                }

                repository.save(nouveauMouvement);

            } catch (Exception e) {
                System.out.println("❌ Erreur pour l'employé " + employe.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("💰 Ajout des 2.5 jours/mois terminé.");
    } 

    public List<MouvementSolde> getMouvementsPrisByEmploye(Employe employe) {
        return repository.findByEmployeAndTypeMouvementOrderByCreatedAtAsc(
                employe,
                TypeEnumConge.PRISE
        ); 
    }

    public Optional<MouvementSolde> getDernierMouvementParEmploye(Employe employe){
        return repository.findFirstByEmployeAndStatutOrderByMoisDescCreatedAtDesc(employe, 1);
    } 

    public Optional<List<MouvementSolde>> getMouvementSoldeParEmploye(String idEmploye){
        return repository.findMouvementsReportByEmployeId(idEmploye);
    }
        
    @Transactional
    public List<MouvementSolde> validerReportSolde(List<Employe> lesEmployes) throws Exception {
        try {
            List<MouvementSolde> mouvements = new ArrayList<>();
            RegleGestionConges regles = regleGestionCongesService.getDerniereRegleGestionCongesParStatutActive();
            
            if (regles == null) {
                throw new IllegalStateException("Aucune règle de gestion des congés active");
            }
            
            double acquisitionSolde = regles.getSoldeMensuel();
            PeriodePaie periodePaieActive = periodePaieRepository.findByStatut(0).get();
            int moisActuel = periodePaieActive.getMois().getNum();
            int anneeActuelle = periodePaieActive.getAnnee();
            
            for (Employe employe : lesEmployes) {
                System.out.println("employe concerné : " + employe.getNomComplet());
                System.out.println("idEmp : " + employe.getId());
                MouvementSolde prochainMouvement = creerMouvementReport(employe, moisActuel, anneeActuelle, acquisitionSolde);
                System.out.println("prochainMouvement : " + prochainMouvement.getNbCongePris());
                mouvements.add(prochainMouvement);
            }
            
            return repository.saveAll(mouvements);
            
        } catch (Exception e) {
            throw new Exception("Erreur lors de la validation du report des soldes de congés", e);
        }
    }

    private MouvementSolde creerMouvementReport(Employe employe, int moisActuel, int anneeActuelle, double acquisitionSolde) {
        MouvementSolde prochainMouvement = new MouvementSolde();
        
        // 1. Lier à l'employé
        prochainMouvement.setEmploye(employe);
        
        // 2. Définir type et statut
        prochainMouvement.setTypeMouvement(TypeEnumConge.REPORT);
        prochainMouvement.setStatut(1); // À définir: 1 = validé ?
        
        // 3. Calculer la date du mouvement
        // MouvementSolde dernierMouvement = getDernierMouvementParEmploye(employe);
        
        int moisMouvement = moisActuel;
        int anneeMouvement = anneeActuelle;
        if (moisActuel < 12) {
            moisMouvement = moisActuel + 1;
            anneeMouvement = anneeActuelle;
        } else {
            moisMouvement = 1;
            anneeMouvement = anneeActuelle + 1; 
        }
        prochainMouvement.setMois(moisMouvement);
        prochainMouvement.setAnnee(anneeMouvement);

        // System.out.println("mi-existe ve lay mouvement ?? : " + getDernierMouvementParEmploye(employe).isPresent());
        // if (getDernierMouvementParEmploye(employe).isPresent()) {
        //     MouvementSolde dernierMouvement = getDernierMouvementParEmploye(employe).get();            
        // }
        
        
        
        // 4. Calculer le solde
        if (getDernierMouvementParEmploye(employe).isPresent()) {
            
            MouvementSolde dernierMouvement = getDernierMouvementParEmploye(employe).get();
            // REPORT: on garde le solde restant
            double nouveauSolde = dernierMouvement.getNbCongeRestant();
            prochainMouvement.setNbCongeRestant(nouveauSolde);
            prochainMouvement.setNbCongeTotal(nouveauSolde);
            prochainMouvement.setNbCongePris(0.0);
        } else {
            // PREMIER MOUVEMENT: calcul basé sur l'ancienneté
            InfosProfessionnelles infoPro = infosProfessionnellesService
                .findLastByEmployeId(employe.getId());
            System.out.println("azo ary ve le infoPro e : " + infoPro.getDateEmbauche());
            
            if (infoPro == null || infoPro.getDateEmbauche() == null) {
                throw new IllegalArgumentException("Employé " + employe.getId() + " n'a pas de date d'embauche");
            }
            
            double ancienneteMois = ancienneteService.calculerAncienneteEnMois(infoPro.getDateEmbauche());
            double soldeExact = ancienneteMois * acquisitionSolde;
            // Arrondi à 0.5 près
            soldeExact = Math.round(soldeExact * 2) / 2.0;
            // double soldeInitial = ancienneteMois * acquisitionSolde;
            
            prochainMouvement.setNbCongeRestant(soldeExact);
            prochainMouvement.setNbCongeTotal(soldeExact);
            prochainMouvement.setNbCongePris(0.0);
        }

        return prochainMouvement;
    }
    public Optional<MouvementSolde> findTopByEmployeOrderByAnneeDescMoisDesc(Employe employe){
        return repository.findTopByEmployeOrderByAnneeDescMoisDesc(employe);
    }
     
    public boolean existsByEmployeAndMoisAndAnnee(Employe employe, int mois, int annee){
        return repository.existsByEmployeAndMoisAndAnnee(employe, mois, annee);
    }
    
    public Optional<MouvementSolde> findByEmployeAndMoisAndAnnee(Employe employe, int mois, int annee){
        return repository.findByEmployeAndMoisAndAnnee(employe, mois, annee);
    }
    
    public boolean existeMouvementPourMoisEtAnnee(Employe employe, int mois, int annee) {
        return repository.existsByEmployeAndMoisAndAnnee(employe, mois, annee);
    }
}
