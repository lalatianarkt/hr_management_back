// package com.rh.manage.Service;

// import java.time.LocalDate;
// import java.time.YearMonth;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Locale;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.rh.manage.Dto.AlerteDTO;
// import com.rh.manage.Dto.DashboardRHDTO;
// import com.rh.manage.Dto.DemandeCongeEnCoursDTO;
// import com.rh.manage.Dto.KPIDTO;
// import com.rh.manage.Dto.PointageEmployeAgregatDTO;
// import com.rh.manage.Dto.PointageEmployeDTO;
// import com.rh.manage.Dto.StatsAbsencesCongesDTO;
// import com.rh.manage.Dto.StatsEffectifDTO;
// import com.rh.manage.Dto.StatsPointageDTO;
// import com.rh.manage.Dto.StatsPresenceDTO;
// import com.rh.manage.Dto.TendanceMensuelleDTO;
// import com.rh.manage.Dto.TopEmployeDTO;
// import com.rh.manage.Dto.StatistiquesAbsenceDTO;
// import com.rh.manage.Dto.StatistiquesPointageDTO;
// import com.rh.manage.Model.Departement;
// import com.rh.manage.Model.InfosProfessionnelles;
// import com.rh.manage.Model.Pointage;
// import com.rh.manage.Model.VueDemandeConge;
// import com.rh.manage.Model.VuePaieComplete;
// import com.rh.manage.View.VuePointageEmploye;

// @Service
// @Transactional
// public class DashboardRHService {
    
//     @Autowired 
//     private EmployeService employeService;
    
//     @Autowired
//     private DepartementService departementService;

//     @Autowired
//     private DemandeCongeService demandeCongeService;

//     @Autowired
//     private VueDemandeCongeService vueDemandeCongeService;

//     @Autowired
//     private VuePaieCompleteService vuePaieCompleteService;

//     @Autowired
//     private InfosProfessionnellesService infosProfessionnellesService;

//     @Autowired
//     private AbsenceCongeService absenceCongeService;

//     @Autowired
//     private PeriodePaieService periodePaieService;

//     // @Autowired
//     // private VuePointageEmployeService vuePointageEmployeService;

//     @Autowired
//     private VuePointageEmployeV2Service vuePointageEmployeV2Service;

//     @Autowired
//     private PointageService pointageService;
    
//     /**
//      * Calcule tous les KPI principaux
//      */
//     private void calculerKPIs(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         // 1. Effectif total
//         Long nbTotalEmployeActif = employeService.countTotalEmployes();
//         System.out.println("kpi : nb employé : " + nbTotalEmployeActif);
//         KPIDTO kpiEffectif = new KPIDTO();
//         kpiEffectif.setTitre("Effectif Actif");
//         kpiEffectif.setValeur(nbTotalEmployeActif != null ? nbTotalEmployeActif : 0);
//         kpiEffectif.setUnite("employés");
//         kpiEffectif.setCouleur("success");
//         kpiEffectif.setIcone("team");
//         kpiEffectif.setDescription("Nombre total d'employés actifs dans l'entreprise");
//         dashboard.setEffectifTotal(kpiEffectif);
//         System.out.println("effectif : " + dashboard.getEffectifTotal());
        
//         // 2. Taux de présence (à calculer à partir des pointages)
//         double tauxPresence = calculerTauxPresence(dateDebut, dateFin);
//         KPIDTO kpiPresence = new KPIDTO();
//         kpiPresence.setTitre("Taux de Présence");
//         kpiPresence.setValeur(Math.round(tauxPresence * 10.0) / 10.0);
//         kpiPresence.setUnite("%");
//         kpiPresence.setCouleur(tauxPresence >= 95 ? "success" : tauxPresence >= 90 ? "warning" : "danger");
//         kpiPresence.setIcone("check-circle");
//         kpiPresence.setDescription("Pourcentage moyen de présence des employés");
//         dashboard.setTauxPresence(kpiPresence);
        
//         // 3. Taux d'absentéisme
//         StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales();
//         double tauxAbsenteeisme = statAbsence != null ? statAbsence.getTauxAbsence() : 0;
//         KPIDTO kpiAbsenteeisme = new KPIDTO();
//         kpiAbsenteeisme.setTitre("Taux d'Absentéisme");
//         kpiAbsenteeisme.setValeur(Math.round(tauxAbsenteeisme * 10.0) / 10.0);
//         kpiAbsenteeisme.setUnite("%");
//         kpiAbsenteeisme.setCouleur(tauxAbsenteeisme <= 3 ? "success" : tauxAbsenteeisme <= 8 ? "warning" : "danger");
//         kpiAbsenteeisme.setIcone("warning");
//         kpiAbsenteeisme.setDescription("Pourcentage de jours d'absence non justifiés");
//         dashboard.setTauxAbsenteeisme(kpiAbsenteeisme);
//         System.out.println("taux absentéisme : " + kpiAbsenteeisme);
        
//         // 4. Congés pris
//         int totalConges = statAbsence != null ? statAbsence.getTotalConges().intValue() : 0;
//         KPIDTO kpiConges = new KPIDTO();
//         kpiConges.setTitre("Congés Pris");
//         kpiConges.setValeur(totalConges);
//         kpiConges.setUnite("jours");
//         kpiConges.setCouleur("info");
//         kpiConges.setIcone("coffee");
//         kpiConges.setDescription("Nombre total de jours de congés pris cette période");
//         dashboard.setCongesPris(kpiConges);
        
//         // 5. Heures travaillées
//         double moyHeuresTravaillees = calculerMoyenneHeuresTravaillees(dateDebut, dateFin);
//         System.out.println("moyenneTravaillees : " + moyHeuresTravaillees);
//         KPIDTO kpiHeures = new KPIDTO();
//         kpiHeures.setTitre("Heures Travaillées");
//         kpiHeures.setValeur(Math.round(moyHeuresTravaillees * 10.0) / 10.0);
//         kpiHeures.setUnite("h/mois");
//         kpiHeures.setCouleur("info");
//         kpiHeures.setIcone("clock-circle");
//         kpiHeures.setDescription("Moyenne d'heures travaillées par employé par mois");
//         dashboard.setHeuresTravaillees(kpiHeures);
//         System.out.println("fa tsy tokony eto ho eto ve zao e ? ");
        
//         // 6. Masse salariale
//         double masseSalariale = calculerMasseSalariale(dateDebut, dateFin);
//         System.out.println("eto amlé masse salariale : " + masseSalariale);
//         KPIDTO kpiMasseSalariale = new KPIDTO();
//         kpiMasseSalariale.setTitre("Masse Salariale");
//         kpiMasseSalariale.setValeur(Math.round(masseSalariale / 1000.0 * 10.0) / 10.0); // En milliers
//         kpiMasseSalariale.setUnite("k€/mois");
//         kpiMasseSalariale.setCouleur(masseSalariale > 0 ? "success" : "warning");
//         kpiMasseSalariale.setIcone("dollar");
//         kpiMasseSalariale.setDescription("Masse salariale mensuelle totale");
//         dashboard.setMasseSalariale(kpiMasseSalariale);
        

//         // 7. Heures supplémentaires
//         double moyHeuresSupplementaires = calculerMoyenneHeuresSupplementaires(dateDebut, dateFin);
//         KPIDTO kpiHeuresSup = new KPIDTO();
//         kpiHeuresSup.setTitre("Heures Supplémentaires");
//         kpiHeuresSup.setValeur(Math.round(moyHeuresSupplementaires * 10.0) / 10.0);
//         kpiHeuresSup.setUnite("h/mois");
//         kpiHeuresSup.setCouleur(moyHeuresSupplementaires > 10 ? "warning" : "info");
//         kpiHeuresSup.setIcone("plus-circle");
//         kpiHeuresSup.setDescription("Moyenne d'heures supplémentaires par employé");
//         dashboard.setHeuresSupplementaires(kpiHeuresSup);
        
//         // 8. Retards cumulés
//         double totalRetards = calculerTotalRetards(dateDebut, dateFin);
//         KPIDTO kpiRetards = new KPIDTO();
//         kpiRetards.setTitre("Retards Cumulés");
//         kpiRetards.setValeur(Math.round(totalRetards * 10.0) / 10.0);
//         kpiRetards.setUnite("h");
//         kpiRetards.setCouleur(totalRetards > 50 ? "danger" : totalRetards > 20 ? "warning" : "info");
//         kpiRetards.setIcone("clock");
//         kpiRetards.setDescription("Total des heures de retard cette période");
//         dashboard.setRetardsCumules(kpiRetards);
//     }
    
//     /**
//      * Calcule les statistiques d'effectif
//      */
//     private void calculerStatsEffectif(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         StatsEffectifDTO stats = new StatsEffectifDTO();
        
//         Long totalActifs = employeService.countTotalEmployes();
//         Long totalInactifs = employeService.countTotalEmployeInactif();
        
//         stats.setTotalEmployes(totalActifs != null ? totalActifs.intValue() : 0);
//         stats.setEmployesActifs(totalActifs != null ? totalActifs.intValue() : 0);
        
//         // Répartition par département
//         List<Departement> departementsActifs = departementService.getDepartementActif();
//         Map<String, Integer> parDepartement = new HashMap<>();
        
//         for (Departement departement : departementsActifs) {
//             Optional<List<InfosProfessionnelles>> infosPro = infosProfessionnellesService.findByDepartement(departement.getId());
//             int nbParDepartement = 0;
//             nbParDepartement = infosPro != null ? infosPro.get().size() : 0;
//             parDepartement.put(departement.getNom(), nbParDepartement);
//         }
        
//         stats.setParDepartement(parDepartement);
        
//         // Répartition par poste (simplifié)
//         Map<String, Integer> parPoste = new HashMap<>();
//         try {
//             // Récupérer tous les employés et compter par poste
//             List<InfosProfessionnelles> allInfos = infosProfessionnellesService.getAll();
//             if (allInfos != null) {
//                 for (InfosProfessionnelles info : allInfos) {
//                     String poste = info.getPoste().getNom();
//                     if (poste != null) {
//                         parPoste.put(poste, parPoste.getOrDefault(poste, 0) + 1);
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println("Erreur lors du calcul des postes: " + e.getMessage());
//         }
        
//         stats.setParPoste(parPoste);
        
//         // Nouveaux employés (embauchés dans les 30 derniers jours)
//         LocalDate dateLimite = LocalDate.now().minusDays(30);
//         int nouveauxEmployes = 0; // À implémenter selon votre modèle
//         stats.setNouveauxEmployesMois(nouveauxEmployes);
        
//         dashboard.setStatsEffectif(stats);
        
//         // Mettre à jour la répartition pour le dashboard
//         dashboard.setRepartitionParDepartement(parDepartement);
//     }
    
//     /**
//      * Calcule les statistiques de présence
//      */
//     private void calculerStatsPresence(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         StatsPresenceDTO stats = new StatsPresenceDTO();
        
//         // Taux de présence global
//         double tauxPresenceGlobal = calculerTauxPresence(dateDebut, dateFin);
//         stats.setTauxPresenceGlobal(Math.round(tauxPresenceGlobal * 10.0) / 10.0);
        
//         // Taux de retard global
//         double tauxRetardGlobal = calculerTauxRetard(dateDebut, dateFin);
//         stats.setTauxRetardGlobal(Math.round(tauxRetardGlobal * 10.0) / 10.0);
        
//         // Heures travaillées moyennes
//         double moyHeures = calculerMoyenneHeuresTravaillees(dateDebut, dateFin);
//         stats.setHeuresTravailleesMoyennes(Math.round(moyHeures * 10.0) / 10.0);
        
//         // Jours travail moyen (par défaut 20)
//         stats.setJoursTravailMoyen(20);
        
//         // Taux par département
//         Map<String, Double> tauxPresenceParDept = new HashMap<>();
//         Map<String, Double> tauxRetardParDept = new HashMap<>();
        
//         List<Departement> departements = departementService.getDepartementActif();
//         for (Departement dept : departements) {
//             // Calcul simplifié - à adapter selon vos besoins
//             tauxPresenceParDept.put(dept.getNom(), tauxPresenceGlobal);
//             tauxRetardParDept.put(dept.getNom(), tauxRetardGlobal);
//         }
        
//         stats.setTauxPresenceParDepartement(tauxPresenceParDept);
//         stats.setTauxRetardParDepartement(tauxRetardParDept);
        
//         dashboard.setStatsPresence(stats);
//     }
    
//     /**
//      * Calcule les statistiques d'absences et congés
//      */
//     private void calculerStatsAbsencesConges(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         StatsAbsencesCongesDTO stats = new StatsAbsencesCongesDTO();
        
//         StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales();
        
//         if (statAbsence != null) {
//             stats.setTotalAbsencesMois(statAbsence.getTotalAbsences().intValue());
//             stats.setTotalCongesPrisMois(statAbsence.getTotalConges().intValue());
//             stats.setTauxAbsenteeisme(statAbsence.getTauxAbsence());
//             stats.setTauxUtilisationConges(statAbsence.getTauxConge());
//         }
        
//         // Demandes en attente
//         List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
//         stats.setTotalCongesEnAttente(demandesEnAttente != null ? demandesEnAttente.size() : 0);
        
//         // Répartition par département (simplifiée)
//         Map<String, Integer> absencesParDept = new HashMap<>();
//         Map<String, Integer> congesParDept = new HashMap<>();
        
//         List<Departement> departements = departementService.getDepartementActif();
//         for (Departement dept : departements) {
//             // Valeurs par défaut - à adapter avec des requêtes réelles
//             absencesParDept.put(dept.getNom(), 0);
//             congesParDept.put(dept.getNom(), 0);
//         }
        
//         stats.setAbsencesParDepartement(absencesParDept);
//         stats.setCongesParDepartement(congesParDept);
        
//         dashboard.setStatsAbsencesConges(stats);
//     }
    
//     /**
//      * Calcule les statistiques de pointage
//      */
//     private void calculerStatsPointage(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         StatsPointageDTO stats = new StatsPointageDTO();
        
//         StatistiquesPointageDTO statsPointage = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
//         // StatistiquesPointageDTO stat = new StatistiquesPointageDTO();
//         stats.setTotalHeuresTravaillees(statsPointage.getTotalHeuresTravaillees());
//         stats.setTotalHeuresSupplementaires(statsPointage.getTotalHeuresSupplementaires());
//         stats.setTotalRetards(statsPointage.getTotalRetard());
        
        
//         double tempsEffectif = statsPointage.getTotalHeuresTravaillees() - statsPointage.getTotalHeuresSupplementaires();
//         stats.setTempsEffectifMoyen(statsPointage.getNombreEmployes() > 0 ? tempsEffectif / statsPointage.getNombreEmployes() : 0); 
        
//         // Répartition par département
//         Map<String, Double> heuresParDept = new HashMap<>();
//         Map<String, Double> heuresSupParDept = new HashMap<>();
        
//         // À implémenter selon vos vues/requêtes
//         List<Departement> departements = departementService.getDepartementActif();
//         for (Departement dept : departements) {
//             heuresParDept.put(dept.getNom(), statsPointage.getTotalHeuresTravaillees() / Math.max(departements.size(), 1));
//             heuresSupParDept.put(dept.getNom(), statsPointage.getTotalHeuresSupplementaires() / Math.max(departements.size(), 1));
//         }
        
//         stats.setHeuresParDepartement(heuresParDept);
//         stats.setHeuresSupParDepartement(heuresSupParDept);
        
//         dashboard.setStatsPointage(stats);
//     }
    
//     /**
//      * Calcule les tendances mensuelles
//      */
//     private void calculerTendances(DashboardRHDTO dashboard) {
//         List<TendanceMensuelleDTO> tendances = new ArrayList<>();
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);
        
//         // Générer les 6 derniers mois
//         for (int i = 5; i >= 0; i--) {
//             YearMonth yearMonth = YearMonth.now().minusMonths(i);
//             TendanceMensuelleDTO tendance = new TendanceMensuelleDTO();
            
//             tendance.setMois(yearMonth.format(formatter));
            
//             // Effectif (simplifié)
//             tendance.setEffectif(employeService.countTotalEmployes() != null ? 
//                 employeService.countTotalEmployes().intValue() : 0);
            
//             // Taux de présence (simplifié)
//             tendance.setTauxPresence(90.0 + (Math.random() * 10)); // Valeur exemple
            
//             // Heures travaillées (simplifié)
//             tendance.setHeuresTravaillees(160.0 + (Math.random() * 20));
            
//             // Congés pris (simplifié)
//             tendance.setCongesPris(5.0 + (Math.random() * 10));
            
//             // Variations (simplifiées)
//             tendance.setVariationEffectif(i == 0 ? 0 : (Math.random() * 5 - 2.5));
//             tendance.setVariationPresence(i == 0 ? 0 : (Math.random() * 3 - 1.5));
            
//             tendances.add(tendance);
//         }
        
//         dashboard.setTendances(tendances);
//     }
    
//     /**
//      * Calcule les tops employés (absents et performants)
//      */
//     private void calculerTopEmployes(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         // Top absentéisme
//         List<TopEmployeDTO> topAbsenteeisme = new ArrayList<>();
        
//         try {
//             // Récupérer les données d'absence
//             StatistiquesAbsenceDTO statsAbsence = absenceCongeService.getStatistiquesGlobales();
//             // À adapter avec une méthode qui retourne les tops par employé
//         } catch (Exception e) {
//             System.out.println("Erreur lors du calcul du top absentéisme: " + e.getMessage());
//         }
        
//         dashboard.setTopAbsenteeisme(topAbsenteeisme);
        
//         // Top performers (heures travaillées)
//         List<TopEmployeDTO> topPerformers = new ArrayList<>();
        
//         try {
//             StatistiquesPointageDTO statsPointage = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
//             List<PointageEmployeAgregatDTO> les_stat = statsPointage.getTopTravailleurs();
//             if (statsPointage != null) {
//                 // Trier par heures travaillées
//                 // List<StatistiquesPointageDTO> sorted = statsPointage.stream()
//                 //     .sorted((a, b) -> Double.compare(b.getHeuresTravaillees(), a.getHeuresTravaillees()))
//                 //     .limit(5)
//                 //     .collect(Collectors.toList());
                
//                 int rang = 1;
//                 for (PointageEmployeAgregatDTO stat : les_stat) {
//                     TopEmployeDTO top = new TopEmployeDTO();
//                     top.setMatricule(stat.getMatricule());
//                     top.setNomComplet(stat.getNomComplet());
//                     InfosProfessionnelles info_pro = infosProfessionnellesService.findInfosProActifByMatricule(stat.getMatricule());
//                     top.setDepartement(info_pro.getDepartement().getNom());
//                     PointageEmployeDTO pointageEmploye = vuePointageEmployeV2Service.getByMatricule(stat.getMatricule());
//                     top.setValeur(pointageEmploye.getTotalHeureTravaillee());
//                     top.setIndicateur("heures_travaillees");
//                     top.setRang(rang++);
//                     topPerformers.add(top);
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println("Erreur lors du calcul des top performers: " + e.getMessage());
//         }
        
//         dashboard.setTopPerformers(topPerformers);
//     }
    
//     /**
//      * Calcule les alertes
//      */
//     private void calculerAlertes(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         List<AlerteDTO> alertes = new ArrayList<>();
        
//         // Vérifier le taux d'absentéisme
//         StatistiquesAbsenceDTO statsAbsence = absenceCongeService.getStatistiquesGlobales();
//         if (statsAbsence != null && statsAbsence.getTauxAbsence() > 10) {
//             AlerteDTO alerte = new AlerteDTO(
//                 "absenteeisme",
//                 "Taux d'absentéisme élevé",
//                 String.format("Le taux d'absentéisme est de %.1f%%, supérieur au seuil de 10%%", 
//                     statsAbsence.getTauxAbsence()),
//                 "eleve"
//             );
//             alerte.setActionRecommandee("Analyser les causes et mettre en place des actions correctives");
//             alertes.add(alerte);
//         }
        
//         // Vérifier les demandes de congés en attente
//         List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
//         if (demandesEnAttente != null && demandesEnAttente.size() > 5) {
//             AlerteDTO alerte = new AlerteDTO(
//                 "conge",
//                 "Demandes de congés en attente",
//                 String.format("%d demandes de congés sont en attente de validation", 
//                     demandesEnAttente.size()),
//                 "moyen"
//             );
//             alerte.setActionRecommandee("Traiter les demandes de congés en attente");
//             alertes.add(alerte);
//         }
        
//         dashboard.setAlertes(alertes);
//     }
    
//     /**
//      * Récupère les demandes de congés en cours
//      */
//     private void calculerDemandesCongesEnAttente(DashboardRHDTO dashboard) {
//         List<DemandeCongeEnCoursDTO> demandes = new ArrayList<>();
        
//         try {
//             List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
//             if (demandesEnAttente != null) {
//                 for (VueDemandeConge demande : demandesEnAttente) {
//                     DemandeCongeEnCoursDTO dto = new DemandeCongeEnCoursDTO();
//                     dto.setEmployeNom(demande.getNomCompletEmploye());
//                     dto.setMatricule(demande.getMatricule());
//                     dto.setDepartement(demande.getNomDepartement());
//                     dto.setDateDebut(demande.getDateDebut());
//                     dto.setDateFin(demande.getDateFin());
//                     dto.setDureeJours(demande.getNbJours());
//                     dto.setStatut(demande.getDecisionManagerLibelle());
//                     dto.setDateDemande(demande.getDateDemande());
//                     demandes.add(dto);
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println("Erreur lors de la récupération des demandes de congés: " + e.getMessage());
//         }
        
//         dashboard.setDemandesCongesEnAttente(demandes);
//     }
    
//     /**
//      * Calcule les répartitions et masses salariales
//      */
//     private void calculerRepartitions(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
//         // Masse salariale par département
//         Map<String, Double> masseSalarialeParDept = new HashMap<>();
        
//         try {
//             List<VuePaieComplete> paies = vuePaieCompleteService.getByPeriode(dateDebut, dateFin);
//             double salaireBrutTotal = 0;
            
//             if (paies != null) {
//                 for (VuePaieComplete paie : paies) {
//                     if (paie.getSalaireBrut() != null && paie.getDepartement() != null) {
//                         double salaire = paie.getSalaireBrut().doubleValue();
//                         salaireBrutTotal += salaire;
                        
//                         String dept = paie.getDepartement();
//                         masseSalarialeParDept.put(dept, 
//                             masseSalarialeParDept.getOrDefault(dept, 0.0) + salaire);
//                     }
//                 }
//             }
            
//             dashboard.setMasseSalarialeParDepartement(masseSalarialeParDept);
            
//         } catch (Exception e) {
//             System.out.println("Erreur lors du calcul de la masse salariale: " + e.getMessage());
//         }
//     }
    
// // ==================== MÉTHODES DE CALCUL AUXILIAIRES ====================
//     private double calculerTauxPresence(LocalDate dateDebut, LocalDate dateFin) {
//         try {

//             // 1. Récupérer les statistiques de pointage
//             StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
//             // StatistiquesPointageDTO stats = vuePointageEmployeService.getStatistiques(dateDebut, dateFin);
//             System.out.println("stats : " + stats.getNombreEmployes());
//             // Vérifier si stats est null
//             if (stats == null) {
//                 throw new IllegalStateException("Les statistiques de pointage sont nulles");
//             }
            
//             // 3. Récupérer le nombre de pointages pour la période
//             List<Pointage> pointages = pointageService.getPointagesByPeriod(dateDebut, dateFin);
//             System.out.println("pointages size : " + pointages.size());
//             if (pointages == null) {
//                 throw new IllegalStateException("La liste des pointages est nulle");
//             }
            
//             int nbPointages = pointages.size();
            
//             // 4. Calculer le nombre de jours ouvrables dans la période
//             long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);
//             if (joursOuvrables == 0) {
//                 throw new IllegalArgumentException("Aucun jour ouvrable dans la période spécifiée");
//             }
            
//             // 5. Récupérer le nombre d'employés actifs
//             Long nbEmployes = employeService.countTotalEmployes();
//             if (nbEmployes == null) {
//                 throw new IllegalStateException("Le nombre d'employés est nul");
//             }
            
//             if (nbEmployes == 0) {
//                 throw new IllegalStateException("Aucun employé actif dans le système");
//             }
            
//             // 6. Calculer le taux de présence théorique (nombre de pointages attendus)
//             // Chaque employé devrait pointer chaque jour ouvrable
//             long pointagesAttendus = nbEmployes * joursOuvrables;
//             System.out.println("pointageAttendu : " + pointagesAttendus);
//             if (pointagesAttendus == 0) {
//                 throw new ArithmeticException("Le nombre de pointages attendus est nul");
//             }
            
//             // 7. Calculer le taux de présence réel
//             double tauxPresence = ((double) nbPointages / pointagesAttendus) * 100.0;
//             System.out.println("taux présence : " + tauxPresence);
            
//             // 8. Limiter entre 0 et 100% et arrondir
//             double tauxArrondi = Math.round(tauxPresence * 10.0) / 10.0;
//             System.out.println("taux arrondi : " + tauxArrondi);
            
//             // 9. Vérifier la validité du résultat
//             if (tauxArrondi < 0 || tauxArrondi > 100) {
//                 throw new ArithmeticException(String.format(
//                     "Taux de présence invalide: %.1f%% (doit être entre 0 et 100)", tauxArrondi));
//             }
            
//             return tauxArrondi;
            
//         } catch (Exception e) {
//             // Loguer l'erreur proprement
//             System.err.println("Erreur lors du calcul du taux de présence: " + e.getMessage());
//             // Propager l'exception plutôt que de retourner une valeur par défaut
//             throw new RuntimeException("Impossible de calculer le taux de présence: " + e.getMessage(), e);
//         }
//     }

//     /**
//      * Calcule le nombre de jours ouvrables entre deux dates (lundi-vendredi)
//      * @throws IllegalArgumentException si les dates sont invalides
//      */
//     private long calculerJoursOuvrables(LocalDate dateDebut, LocalDate dateFin) {
//         if (dateDebut == null) {
//             throw new IllegalArgumentException("La date de début est nulle");
//         }
        
//         if (dateFin == null) {
//             throw new IllegalArgumentException("La date de fin est nulle");
//         }
        
//         if (dateDebut.isAfter(dateFin)) {
//             throw new IllegalArgumentException(
//                 String.format("La date de début (%s) est après la date de fin (%s)", 
//                             dateDebut, dateFin));
//         }
        
//         long joursOuvrables = 0;
//         LocalDate date = dateDebut;
        
//         while (!date.isAfter(dateFin)) {
//             // Lundi = 1, Mardi = 2, ..., Vendredi = 5
//             if (date.getDayOfWeek().getValue() <= 5) {
//                 joursOuvrables++;
//             }
//             date = date.plusDays(1);
//         }
        
//         return joursOuvrables;
//     }

//     private double calculerTauxRetard(LocalDate dateDebut, LocalDate dateFin) {
//         try {
//             StatistiquesPointageDTO stats = vuePointageEmployeService.getStatistiques(dateDebut, dateFin);
//             return stats.getTauxRetardMoyen();
//             // if (stats != null) {
//             //     // Si la statistique contient un taux de retard
//             //     if (stats.getTauxRetard() != null) {
//             //         return stats.getTauxRetard();
//             //     }
                
//             //     // Sinon, calculer à partir des retards et heures travaillées
//             //     Long nbEmployes = employeService.countTotalEmployes();
//             //     if (nbEmployes != null && nbEmployes > 0 && stats.getHeuresTravaillees() > 0) {
//             //         double heuresAttendues = nbEmployes * 160.0;
//             //         return (stats.getRetards() / heuresAttendues) * 100.0;
//             //     }
//             // }
//         } catch (Exception e) {
//             System.out.println("Erreur calcul taux retard: " + e.getMessage());
//         }
//         return 2.5; // Valeur par défaut
//     }

//     private double calculerMoyenneHeuresTravaillees(LocalDate dateDebut, LocalDate dateFin) {
//         try {
//             double moyenneHeuresTravaillees = 0;
//             int nb_employe = 0;
//             // StatistiquesPointageDTO stats = service.getStatistiques();
//             StatistiquesPointageDTO stats = vuePointageEmployeService.getStatistiques(dateDebut, dateFin);
//             System.out.println("moyenne heure : " + stats.getMoyenneHeuresParEmploye());
//             return stats.getMoyenneHeuresParEmploye();
//             // if (stats != null) {
//             //     System.out.println("vide nga ity : " + stats.getMoyenneHeuresParEmploye());
//             //     List<PointageEmployeDTO> employeDTOs = vuePointageEmployeService.getAllPointages();
//             //     for (PointageEmployeDTO employeDTO : employeDTOs) {
//             //         System.out.println(("employeDTO : " + employeDTO.getTotalHeureTravaillee()));
//             //         moyenneHeuresTravaillees = moyenneHeuresTravaillees + employeDTO.getTotalHeureTravaillee();
//             //         nb_employe = nb_employe++; 
//             //     }
//             //     return moyenneHeuresTravaillees / nb_employe; 
//             //     // return stats.getTotalHeuresTravaillees();
//             // }
//         } catch (Exception e) {
//             System.out.println("Erreur calcul moyenne heures: " + e.getMessage());
//         }
//         return 160.0; // Valeur par défaut
//     }

//     private double calculerMoyenneHeuresSupplementaires(LocalDate dateDebut, LocalDate dateFin) {
//         try {
//             StatistiquesPointageDTO stats = vuePointageEmployeService.getStatistiques(dateDebut, dateFin);
//             System.out.println("calcul moyen HS : " + stats.getMoyenneHeuresParEmploye());
//             if (stats != null) {
//                 // Si la statistique contient une moyenne
//                 return stats.getTauxHeuresSupMoyen();
//             }
//         } catch (Exception e) {
//             System.out.println("Erreur calcul moyenne heures sup: " + e.getMessage());
//         }
//         return 5.0; // Valeur par défaut
//     }

//     private double calculerTotalRetards(LocalDate dateDebut, LocalDate dateFin) {
//         try {
//             StatistiquesPointageDTO stats = vuePointageEmployeService.getStatistiques(dateDebut, dateFin);
//             if (stats != null) {
//                 return stats.getTauxRetardMoyen();
//             }
//         } catch (Exception e) {
//             System.out.println("Erreur calcul total retards: " + e.getMessage());
//         }
//         return 25.0; // Valeur par défaut
//     }
    
//     private double calculerMasseSalariale(LocalDate dateDebut, LocalDate dateFin) {
//         try {
//             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//             LocalDate dateDebutPeriode = LocalDate.parse("01/01/2026", formatter);
//             LocalDate dateFinPeriode = LocalDate.parse("31/01/2026", formatter);    
            
//             List<VuePaieComplete> paies = vuePaieCompleteService.getByPeriode(dateDebutPeriode, dateFinPeriode);
//             double total = 0;
            
//             if (paies != null) {
//                 for (VuePaieComplete paie : paies) {
//                     if (paie.getSalaireBrut() != null) {
//                         total += paie.getSalaireBrut().doubleValue();
//                     }
//                 }
//             }
//             return total;
//         } catch (Exception e) {
//             System.out.println("Erreur calcul masse salariale: " + e.getMessage());
//         }
//         return 50000.0; // Valeur par défaut
//     }

//     /**
//      * Récupère toutes les données pour le tableau de bord RH (période par défaut : mois précédent)
//      */
//     public DashboardRHDTO getDashboardRH() {
//         // 1. Déterminer la période active
//         // LocalDate dateDebutPeriode = LocalDate.now().minusMonths(1).withDayOfMonth(1);
//         // LocalDate dateFinPeriode = LocalDate.now().minusMonths(1).withDayOfMonth(
//         //     LocalDate.now().minusMonths(1).lengthOfMonth()
//         // );
        
//         // Format français (dd/MM/yyyy)
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//         LocalDate dateDebutPeriode = LocalDate.parse("01/12/2025", formatter);
//         LocalDate dateFinPeriode = LocalDate.parse("31/12/2025", formatter);

//         // Pour afficher
//         // String dateFormattee = dateDebutPeriode.format(formatter); // "01/12/2025"

//         System.out.println("dateDebut : " + dateDebutPeriode);
//         System.out.println("dateFinPeriode : " + dateFinPeriode);
        
//         try {
//             // Essayer de récupérer la période de paie active
//             var periodesActives = periodePaieService.getActivePeriodePaies();
//             if (periodesActives != null && !periodesActives.isEmpty()) {
//                 dateDebutPeriode = periodesActives.get(0).getDateDebut();
//                 dateFinPeriode = periodesActives.get(0).getDateFin();
//             } 
//         } catch (Exception e) {
//             // Utiliser les dates par défaut si erreur
//             System.out.println("Utilisation des dates par défaut pour le dashboard: " + e.getMessage());
//         }
        
//         // 2. Créer l'objet dashboard
//         DashboardRHDTO dashboard = new DashboardRHDTO(dateDebutPeriode, dateFinPeriode);
//         // dashboard.set
        
//         // 3. Calculer les KPI principaux
//         calculerKPIs(dashboard, dateDebutPeriode, dateFinPeriode);
        
//         // 4. Calculer les statistiques détaillées
//         calculerStatsEffectif(dashboard, dateDebutPeriode, dateFinPeriode);
//         calculerStatsPresence(dashboard, dateDebutPeriode, dateFinPeriode);
//         calculerStatsAbsencesConges(dashboard, dateDebutPeriode, dateFinPeriode);
//         calculerStatsPointage(dashboard, dateDebutPeriode, dateFinPeriode);
        
//         // 5. Calculer les tendances
//         calculerTendances(dashboard);
        
//         // 6. Récupérer les tops et alertes
//         calculerTopEmployes(dashboard, dateDebutPeriode, dateFinPeriode);
//         calculerAlertes(dashboard, dateDebutPeriode, dateFinPeriode);
//         calculerDemandesCongesEnAttente(dashboard);
        
//         // 7. Calculer les répartitions
//         calculerRepartitions(dashboard, dateDebutPeriode, dateFinPeriode);
        
//         return dashboard;
//     }

//     public DashboardRHDTO getDashboardRHByPeriode(LocalDate dateDebut, LocalDate dateFin) {
//         // Validation
//         if (dateDebut == null || dateFin == null) {
//             throw new IllegalArgumentException("Les dates sont obligatoires");
//         }
        
//         if (dateDebut.isAfter(dateFin)) {
//             throw new IllegalArgumentException("Date début doit être avant date fin");
//         }
        
//         // Créer l'objet dashboard
//         DashboardRHDTO dashboard = new DashboardRHDTO(dateDebut, dateFin);
        
//         // Appeler toutes les méthodes de calcul avec les dates fournies
//         calculerKPIs(dashboard, dateDebut, dateFin);
//         calculerStatsEffectif(dashboard, dateDebut, dateFin);
//         calculerStatsPresence(dashboard, dateDebut, dateFin);
//         calculerStatsAbsencesConges(dashboard, dateDebut, dateFin);
//         calculerStatsPointage(dashboard, dateDebut, dateFin);
//         calculerTendances(dashboard);
//         calculerTopEmployes(dashboard, dateDebut, dateFin);
//         calculerAlertes(dashboard, dateDebut, dateFin);
//         calculerDemandesCongesEnAttente(dashboard);
//         calculerRepartitions(dashboard, dateDebut, dateFin);
        
//         return dashboard;
//     }
// }

package com.rh.manage.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Dto.AlerteDTO;
import com.rh.manage.Dto.DashboardRHDTO;
import com.rh.manage.Dto.DemandeCongeEnCoursDTO;
import com.rh.manage.Dto.KPIDTO;
import com.rh.manage.Dto.PointageEmployeAgregatDTO;
import com.rh.manage.Dto.PointageEmployeDTO;
import com.rh.manage.Dto.StatsAbsencesCongesDTO;
import com.rh.manage.Dto.StatsEffectifDTO;
import com.rh.manage.Dto.StatsPointageDTO;
import com.rh.manage.Dto.StatsPresenceDTO;
import com.rh.manage.Dto.TendanceMensuelleDTO;
import com.rh.manage.Dto.TopEmployeDTO;
import com.rh.manage.Dto.StatistiquesAbsenceDTO;
import com.rh.manage.Dto.StatistiquesPointageDTO;
import com.rh.manage.Dto.StatistiquesDepartementDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Pointage;
import com.rh.manage.Model.VueDemandeConge;
import com.rh.manage.Model.VuePaieComplete;

@Service
@Transactional
public class DashboardRHService {
    
    @Autowired 
    private EmployeService employeService;
    
    @Autowired
    private DepartementService departementService;

    @Autowired
    private DemandeCongeService demandeCongeService;

    @Autowired
    private VueDemandeCongeService vueDemandeCongeService;

    @Autowired
    private VuePaieCompleteService vuePaieCompleteService;

    @Autowired
    private InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    private AbsenceCongeService absenceCongeService;

    @Autowired
    private PeriodePaieService periodePaieService;

    @Autowired
    private VuePointageEmployeV2Service vuePointageEmployeV2Service;

    @Autowired
    private PointageService pointageService;
    
    /**
     * Calcule tous les KPI principaux
     */
    private void calculerKPIs(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // 1. Effectif total
        Long nbTotalEmployeActif = employeService.countTotalEmployes();
        System.out.println("kpi : nb employé : " + nbTotalEmployeActif);
        KPIDTO kpiEffectif = new KPIDTO();
        kpiEffectif.setTitre("Effectif Actif");
        kpiEffectif.setValeur(nbTotalEmployeActif != null ? nbTotalEmployeActif : 0);
        kpiEffectif.setUnite("employés");
        kpiEffectif.setCouleur("success");
        kpiEffectif.setIcone("team");
        kpiEffectif.setDescription("Nombre total d'employés actifs dans l'entreprise");
        dashboard.setEffectifTotal(kpiEffectif);
        System.out.println("effectif : " + dashboard.getEffectifTotal());
        
        // 2. Taux de présence (à calculer à partir des pointages)
        double tauxPresence = calculerTauxPresence(dateDebut, dateFin);
        KPIDTO kpiPresence = new KPIDTO();
        kpiPresence.setTitre("Taux de Présence");
        kpiPresence.setValeur(Math.round(tauxPresence * 10.0) / 10.0);
        kpiPresence.setUnite("%");
        kpiPresence.setCouleur(tauxPresence >= 95 ? "success" : tauxPresence >= 90 ? "warning" : "danger");
        kpiPresence.setIcone("check-circle");
        kpiPresence.setDescription("Pourcentage moyen de présence des employés");
        dashboard.setTauxPresence(kpiPresence);
        
        // 3. Taux d'absentéisme
        StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales();
        double tauxAbsenteeisme = statAbsence != null ? statAbsence.getTauxAbsence() : 0;
        KPIDTO kpiAbsenteeisme = new KPIDTO();
        kpiAbsenteeisme.setTitre("Taux d'Absentéisme");
        kpiAbsenteeisme.setValeur(Math.round(tauxAbsenteeisme * 10.0) / 10.0);
        kpiAbsenteeisme.setUnite("%");
        kpiAbsenteeisme.setCouleur(tauxAbsenteeisme <= 3 ? "success" : tauxAbsenteeisme <= 8 ? "warning" : "danger");
        kpiAbsenteeisme.setIcone("warning");
        kpiAbsenteeisme.setDescription("Pourcentage de jours d'absence non justifiés");
        dashboard.setTauxAbsenteeisme(kpiAbsenteeisme);
        System.out.println("taux absentéisme : " + kpiAbsenteeisme);
        
        // 4. Congés pris
        int totalConges = statAbsence != null ? statAbsence.getTotalConges().intValue() : 0;
        KPIDTO kpiConges = new KPIDTO();
        kpiConges.setTitre("Congés Pris");
        kpiConges.setValeur(totalConges);
        kpiConges.setUnite("jours");
        kpiConges.setCouleur("info");
        kpiConges.setIcone("coffee");
        kpiConges.setDescription("Nombre total de jours de congés pris cette période");
        dashboard.setCongesPris(kpiConges);
        
        // 5. Heures travaillées
        double moyHeuresTravaillees = calculerMoyenneHeuresTravaillees(dateDebut, dateFin);
        System.out.println("moyenneTravaillees : " + moyHeuresTravaillees);
        KPIDTO kpiHeures = new KPIDTO();
        kpiHeures.setTitre("Heures Travaillées");
        kpiHeures.setValeur(Math.round(moyHeuresTravaillees * 10.0) / 10.0);
        kpiHeures.setUnite("h/mois");
        kpiHeures.setCouleur("info");
        kpiHeures.setIcone("clock-circle");
        kpiHeures.setDescription("Moyenne d'heures travaillées par employé par mois");
        dashboard.setHeuresTravaillees(kpiHeures);
        System.out.println("fa tsy tokony eto ho eto ve zao e ? ");
        
        // 6. Masse salariale
        double masseSalariale = calculerMasseSalariale(dateDebut, dateFin);
        System.out.println("eto amlé masse salariale : " + masseSalariale);
        KPIDTO kpiMasseSalariale = new KPIDTO();
        kpiMasseSalariale.setTitre("Masse Salariale");
        kpiMasseSalariale.setValeur(Math.round(masseSalariale / 1000.0 * 10.0) / 10.0); // En milliers
        kpiMasseSalariale.setUnite("k€/mois");
        kpiMasseSalariale.setCouleur(masseSalariale > 0 ? "success" : "warning");
        kpiMasseSalariale.setIcone("dollar");
        kpiMasseSalariale.setDescription("Masse salariale mensuelle totale");
        dashboard.setMasseSalariale(kpiMasseSalariale);
        
        // 7. Heures supplémentaires
        double moyHeuresSupplementaires = calculerMoyenneHeuresSupplementaires(dateDebut, dateFin);
        KPIDTO kpiHeuresSup = new KPIDTO();
        kpiHeuresSup.setTitre("Heures Supplémentaires");
        kpiHeuresSup.setValeur(Math.round(moyHeuresSupplementaires * 10.0) / 10.0);
        kpiHeuresSup.setUnite("h/mois");
        kpiHeuresSup.setCouleur(moyHeuresSupplementaires > 10 ? "warning" : "info");
        kpiHeuresSup.setIcone("plus-circle");
        kpiHeuresSup.setDescription("Moyenne d'heures supplémentaires par employé");
        dashboard.setHeuresSupplementaires(kpiHeuresSup);
        
        // 8. Retards cumulés
        double totalRetards = calculerTotalRetards(dateDebut, dateFin);
        KPIDTO kpiRetards = new KPIDTO();
        kpiRetards.setTitre("Retards Cumulés");
        kpiRetards.setValeur(Math.round(totalRetards * 10.0) / 10.0);
        kpiRetards.setUnite("h");
        kpiRetards.setCouleur(totalRetards > 50 ? "danger" : totalRetards > 20 ? "warning" : "info");
        kpiRetards.setIcone("clock");
        kpiRetards.setDescription("Total des heures de retard cette période");
        dashboard.setRetardsCumules(kpiRetards);
    }
    
    /**
     * Calcule les statistiques d'effectif
     */
    private void calculerStatsEffectif(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        StatsEffectifDTO stats = new StatsEffectifDTO();
        
        Long totalActifs = employeService.countTotalEmployes();
        Long totalInactifs = employeService.countTotalEmployeInactif();
        
        stats.setTotalEmployes(totalActifs != null ? totalActifs.intValue() : 0);
        stats.setEmployesActifs(totalActifs != null ? totalActifs.intValue() : 0);
        
        // Répartition par département
        List<Departement> departementsActifs = departementService.getDepartementActif();
        Map<String, Integer> parDepartement = new HashMap<>();
        
        for (Departement departement : departementsActifs) {
            Optional<List<InfosProfessionnelles>> infosPro = infosProfessionnellesService.findByDepartement(departement.getId());
            int nbParDepartement = infosPro.isPresent() ? infosPro.get().size() : 0;
            parDepartement.put(departement.getNom(), nbParDepartement);
        }
        
        stats.setParDepartement(parDepartement);
        
        // Répartition par poste
        Map<String, Integer> parPoste = new HashMap<>();
        try {
            List<InfosProfessionnelles> allInfos = infosProfessionnellesService.getAll();
            if (allInfos != null) {
                for (InfosProfessionnelles info : allInfos) {
                    if (info.getPoste() != null && info.getPoste().getNom() != null) {
                        String poste = info.getPoste().getNom();
                        parPoste.put(poste, parPoste.getOrDefault(poste, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des postes: " + e.getMessage());
        }
        
        stats.setParPoste(parPoste);
        
        // Nouveaux employés (embauchés dans les 30 derniers jours)
        int nouveauxEmployes = 0; // À implémenter selon votre modèle
        stats.setNouveauxEmployesMois(nouveauxEmployes);
        
        dashboard.setStatsEffectif(stats);
        dashboard.setRepartitionParDepartement(parDepartement);
    }
    
    /**
     * Calcule les statistiques de présence
     */
    private void calculerStatsPresence(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        StatsPresenceDTO stats = new StatsPresenceDTO();
        
        // Taux de présence global
        double tauxPresenceGlobal = calculerTauxPresence(dateDebut, dateFin);
        stats.setTauxPresenceGlobal(Math.round(tauxPresenceGlobal * 10.0) / 10.0);
        
        // Taux de retard global
        double tauxRetardGlobal = calculerTauxRetard(dateDebut, dateFin);
        stats.setTauxRetardGlobal(Math.round(tauxRetardGlobal * 10.0) / 10.0);
        
        // Heures travaillées moyennes
        double moyHeures = calculerMoyenneHeuresTravaillees(dateDebut, dateFin);
        stats.setHeuresTravailleesMoyennes(Math.round(moyHeures * 10.0) / 10.0);
        
        // Jours travail moyen (calculé à partir de la période)
        long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);
        stats.setJoursTravailMoyen((int) joursOuvrables);
        
        // Taux par département
        Map<String, Double> tauxPresenceParDept = new HashMap<>();
        Map<String, Double> tauxRetardParDept = new HashMap<>();
        
        try {
            List<StatistiquesDepartementDTO> statsDept = vuePointageEmployeV2Service.getStatistiquesDepartement(dateDebut, dateFin);
            for (StatistiquesDepartementDTO dept : statsDept) {
                tauxPresenceParDept.put(dept.getDepartementNom(), 
                    calculerTauxPresencePourDepartement(dept, joursOuvrables));
                tauxRetardParDept.put(dept.getDepartementNom(), 
                    calculerTauxRetardPourDepartement(dept));
            }
        } catch (Exception e) {
            System.out.println("Erreur calcul stats par département: " + e.getMessage());
        }
        
        stats.setTauxPresenceParDepartement(tauxPresenceParDept);
        stats.setTauxRetardParDepartement(tauxRetardParDept);
        
        dashboard.setStatsPresence(stats);
    }
    
    /**
     * Calcule les statistiques d'absences et congés
     */
    private void calculerStatsAbsencesConges(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        StatsAbsencesCongesDTO stats = new StatsAbsencesCongesDTO();
        
        StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales();
        
        if (statAbsence != null) {
            stats.setTotalAbsencesMois(statAbsence.getTotalAbsences().intValue());
            stats.setTotalCongesPrisMois(statAbsence.getTotalConges().intValue());
            stats.setTauxAbsenteeisme(statAbsence.getTauxAbsence());
            stats.setTauxUtilisationConges(statAbsence.getTauxConge());
        }
        
        // Demandes en attente
        List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
        stats.setTotalCongesEnAttente(demandesEnAttente != null ? demandesEnAttente.size() : 0);
        
        // Répartition par département
        Map<String, Integer> absencesParDept = new HashMap<>();
        Map<String, Integer> congesParDept = new HashMap<>();
        
        try {
            List<Departement> departements = departementService.getDepartementActif();
            for (Departement dept : departements) {
                // Ces valeurs devraient venir de votre service d'absence
                absencesParDept.put(dept.getNom(), 0);
                congesParDept.put(dept.getNom(), 0);
            }
        } catch (Exception e) {
            System.out.println("Erreur calcul répartition absences: " + e.getMessage());
        }
        
        stats.setAbsencesParDepartement(absencesParDept);
        stats.setCongesParDepartement(congesParDept);
        
        dashboard.setStatsAbsencesConges(stats);
    }
    
    /**
     * Calcule les statistiques de pointage avec la nouvelle vue V2
     */
    private void calculerStatsPointage(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        StatsPointageDTO stats = new StatsPointageDTO();
        
        try {
            StatistiquesPointageDTO statsPointage = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            
            if (statsPointage != null) {
                stats.setTotalHeuresTravaillees(statsPointage.getTotalHeuresTravaillees());
                stats.setTotalHeuresSupplementaires(statsPointage.getTotalHeuresSupplementaires());
                stats.setTotalRetards(statsPointage.getTotalRetard());
                
                double tempsEffectif = statsPointage.getTotalHeuresTravaillees() - statsPointage.getTotalHeuresSupplementaires();
                stats.setTempsEffectifMoyen(statsPointage.getNombreEmployes() > 0 ? 
                    tempsEffectif / statsPointage.getNombreEmployes() : 0);
                
                // Répartition par département avec les vraies données
                Map<String, Double> heuresParDept = new HashMap<>();
                Map<String, Double> heuresSupParDept = new HashMap<>();
                
                List<StatistiquesDepartementDTO> statsDept = vuePointageEmployeV2Service.getStatistiquesDepartement(dateDebut, dateFin);
                for (StatistiquesDepartementDTO dept : statsDept) {
                    heuresParDept.put(dept.getDepartementNom(), dept.getTotalHeuresTravailleesEnHeures());
                    heuresSupParDept.put(dept.getDepartementNom(), dept.getTotalHeuresSupEnHeures());
                }
                
                stats.setHeuresParDepartement(heuresParDept);
                stats.setHeuresSupParDepartement(heuresSupParDept);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des stats de pointage: " + e.getMessage());
        }
        
        dashboard.setStatsPointage(stats);
    }
    
    /**
     * Calcule les tendances mensuelles avec les vraies données
     */
    private void calculerTendances(DashboardRHDTO dashboard) {
        List<TendanceMensuelleDTO> tendances = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);
        
        // Générer les 6 derniers mois
        for (int i = 5; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.now().minusMonths(i);
            LocalDate debutMois = yearMonth.atDay(1);
            LocalDate finMois = yearMonth.atEndOfMonth();
            
            TendanceMensuelleDTO tendance = new TendanceMensuelleDTO();
            tendance.setMois(yearMonth.format(formatter));
            
            try {
                // Effectif
                Long effectif = employeService.countTotalEmployes();
                tendance.setEffectif(effectif != null ? effectif.intValue() : 0);
                
                // Taux de présence
                double tauxPresence = calculerTauxPresence(debutMois, finMois);
                tendance.setTauxPresence(tauxPresence);
                
                // Heures travaillées (moyenne)
                double heuresTravaillees = calculerMoyenneHeuresTravaillees(debutMois, finMois);
                tendance.setHeuresTravaillees(heuresTravaillees);
                
                // Congés pris (à adapter avec vos données réelles)
                tendance.setCongesPris(5.0 + (Math.random() * 10)); // Valeur exemple à remplacer
                
                // Variations
                if (i > 0) {
                    YearMonth moisPrecedent = yearMonth.minusMonths(1);
                    LocalDate debutPrecedent = moisPrecedent.atDay(1);
                    LocalDate finPrecedent = moisPrecedent.atEndOfMonth();
                    
                    double tauxPresencePrecedent = calculerTauxPresence(debutPrecedent, finPrecedent);
                    tendance.setVariationPresence(tauxPresence - tauxPresencePrecedent);
                }
                
            } catch (Exception e) {
                System.out.println("Erreur calcul tendance pour " + yearMonth + ": " + e.getMessage());
                // Valeurs par défaut en cas d'erreur
                tendance.setEffectif(0);
                tendance.setTauxPresence(0.0);
                tendance.setHeuresTravaillees(0.0);
                tendance.setCongesPris(0.0);
                tendance.setVariationPresence(0.0);
            }
            
            tendances.add(tendance);
        }
        
        dashboard.setTendances(tendances);
    }
    
    /**
     * Calcule les tops employés avec la nouvelle vue V2
     */
    private void calculerTopEmployes(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Top absentéisme (à implémenter avec vos données d'absence)
        List<TopEmployeDTO> topAbsenteeisme = new ArrayList<>();
        dashboard.setTopAbsenteeisme(topAbsenteeisme);
        
        // Top performers (heures travaillées) avec les vraies données
        List<TopEmployeDTO> topPerformers = new ArrayList<>();
        
        try {
            List<PointageEmployeAgregatDTO> topTravailleurs = 
                vuePointageEmployeV2Service.getTopTravailleurs(dateDebut, dateFin, 5);
            
            int rang = 1;
            for (PointageEmployeAgregatDTO stat : topTravailleurs) {
                TopEmployeDTO top = new TopEmployeDTO();
                top.setMatricule(stat.getMatricule());
                top.setNomComplet(stat.getNomComplet());
                
                try {
                    InfosProfessionnelles infoPro = infosProfessionnellesService.findInfosProActifByMatricule(stat.getMatricule());
                    if (infoPro != null && infoPro.getDepartement() != null) {
                        top.setDepartement(infoPro.getDepartement().getNom());
                    }
                } catch (Exception e) {
                    top.setDepartement("Non défini");
                }
                
                top.setValeur(stat.getTotalHeureTravailleeEnHeures());
                top.setIndicateur("heures_travaillees");
                top.setRang(rang++);
                topPerformers.add(top);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des top performers: " + e.getMessage());
        }
        
        dashboard.setTopPerformers(topPerformers);
        
        // Top retardataires
        List<TopEmployeDTO> topRetardataires = new ArrayList<>();
        try {
            List<PointageEmployeAgregatDTO> topRetards = 
                vuePointageEmployeV2Service.getTopRetardataires(dateDebut, dateFin, 5);
            
            int rang = 1;
            for (PointageEmployeAgregatDTO stat : topRetards) {
                TopEmployeDTO top = new TopEmployeDTO();
                top.setMatricule(stat.getMatricule());
                top.setNomComplet(stat.getNomComplet());
                
                try {
                    InfosProfessionnelles infoPro = infosProfessionnellesService.findInfosProActifByMatricule(stat.getMatricule());
                    if (infoPro != null && infoPro.getDepartement() != null) {
                        top.setDepartement(infoPro.getDepartement().getNom());
                    }
                } catch (Exception e) {
                    top.setDepartement("Non défini");
                }
                
                top.setValeur(stat.getTotalRetardEnHeures());
                top.setIndicateur("heures_retard");
                top.setRang(rang++);
                topRetardataires.add(top);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des top retardataires: " + e.getMessage());
        }
        
        dashboard.setTopRetardataires(topRetardataires);
    }
    
    /**
     * Calcule les alertes
     */
    private void calculerAlertes(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        List<AlerteDTO> alertes = new ArrayList<>();
        
        // Vérifier le taux d'absentéisme
        try {
            StatistiquesAbsenceDTO statsAbsence = absenceCongeService.getStatistiquesGlobales();
            if (statsAbsence != null && statsAbsence.getTauxAbsence() > 10) {
                AlerteDTO alerte = new AlerteDTO(
                    "absenteeisme",
                    "Taux d'absentéisme élevé",
                    String.format("Le taux d'absentéisme est de %.1f%%, supérieur au seuil de 10%%", 
                        statsAbsence.getTauxAbsence()),
                    "eleve"
                );
                alerte.setActionRecommandee("Analyser les causes et mettre en place des actions correctives");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur vérification absentéisme: " + e.getMessage());
        }
        
        // Vérifier les demandes de congés en attente
        try {
            List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
            if (demandesEnAttente != null && demandesEnAttente.size() > 5) {
                AlerteDTO alerte = new AlerteDTO(
                    "conge",
                    "Demandes de congés en attente",
                    String.format("%d demandes de congés sont en attente de validation", 
                        demandesEnAttente.size()),
                    "moyen"
                );
                alerte.setActionRecommandee("Traiter les demandes de congés en attente");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur vérification congés: " + e.getMessage());
        }
        
        // Vérifier les retards excessifs
        try {
            double totalRetards = calculerTotalRetards(dateDebut, dateFin);
            if (totalRetards > 50) {
                AlerteDTO alerte = new AlerteDTO(
                    "retard",
                    "Retards cumulés élevés",
                    String.format("Le total des retards est de %.1f heures cette période", totalRetards),
                    "moyen"
                );
                alerte.setActionRecommandee("Sensibiliser les équipes sur le respect des horaires");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur vérification retards: " + e.getMessage());
        }
        
        dashboard.setAlertes(alertes);
    }
    
    /**
     * Récupère les demandes de congés en cours
     */
    private void calculerDemandesCongesEnAttente(DashboardRHDTO dashboard) {
        List<DemandeCongeEnCoursDTO> demandes = new ArrayList<>();
        
        try {
            List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
            if (demandesEnAttente != null) {
                for (VueDemandeConge demande : demandesEnAttente) {
                    DemandeCongeEnCoursDTO dto = new DemandeCongeEnCoursDTO();
                    dto.setEmployeNom(demande.getNomCompletEmploye());
                    dto.setMatricule(demande.getMatricule());
                    dto.setDepartement(demande.getNomDepartement());
                    dto.setDateDebut(demande.getDateDebut());
                    dto.setDateFin(demande.getDateFin());
                    dto.setDureeJours(demande.getNbJours());
                    dto.setStatut(demande.getDecisionManagerLibelle());
                    dto.setDateDemande(demande.getDateDemande());
                    demandes.add(dto);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des demandes de congés: " + e.getMessage());
        }
        
        dashboard.setDemandesCongesEnAttente(demandes);
    }
    
    /**
     * Calcule les répartitions et masses salariales
     */
    private void calculerRepartitions(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // Masse salariale par département
        Map<String, Double> masseSalarialeParDept = new HashMap<>();
        
        try {
            List<VuePaieComplete> paies = vuePaieCompleteService.getByPeriode(dateDebut, dateFin);
            double salaireBrutTotal = 0;
            
            if (paies != null) {
                for (VuePaieComplete paie : paies) {
                    if (paie.getSalaireBrut() != null && paie.getDepartement() != null) {
                        double salaire = paie.getSalaireBrut().doubleValue();
                        salaireBrutTotal += salaire;
                        
                        masseSalarialeParDept.put(paie.getDepartement(), 
                            masseSalarialeParDept.getOrDefault(paie.getDepartement(), 0.0) + salaire);
                    }
                }
            }
            
            dashboard.setMasseSalarialeParDepartement(masseSalarialeParDept);
            
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul de la masse salariale: " + e.getMessage());
        }
    }
    
// ==================== MÉTHODES DE CALCUL AUXILIAIRES ====================
    
    private double calculerTauxPresence(LocalDate dateDebut, LocalDate dateFin) {
        try {
            // Récupérer les statistiques de pointage avec la V2
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("stats présence : " + (stats != null ? stats.getNombreEmployes() : "null"));
            
            if (stats == null) {
                throw new IllegalStateException("Les statistiques de pointage sont nulles");
            }
            
            // Récupérer le nombre de pointages pour la période (version détaillée)
            List<com.rh.manage.Dto.PointageDetailDTO> pointagesDetails = vuePointageEmployeV2Service.getPointagesByPeriode(dateDebut, dateFin);
            System.out.println("pointages size : " + (pointagesDetails != null ? pointagesDetails.size() : 0));
            
            if (pointagesDetails == null) {
                throw new IllegalStateException("La liste des pointages est nulle");
            }
            
            int nbPointages = pointagesDetails.size();
            
            // Calculer le nombre de jours ouvrables dans la période
            long joursOuvrables = calculerJoursOuvrables(dateDebut, dateFin);
            if (joursOuvrables == 0) {
                throw new IllegalArgumentException("Aucun jour ouvrable dans la période spécifiée");
            }
            
            // Récupérer le nombre d'employés actifs
            Long nbEmployes = employeService.countTotalEmployes();
            if (nbEmployes == null) {
                throw new IllegalStateException("Le nombre d'employés est nul");
            }
            
            if (nbEmployes == 0) {
                throw new IllegalStateException("Aucun employé actif dans le système");
            }
            
            // Calculer le taux de présence
            long pointagesAttendus = nbEmployes * joursOuvrables;
            System.out.println("pointagesAttendus : " + pointagesAttendus);
            
            if (pointagesAttendus == 0) {
                throw new ArithmeticException("Le nombre de pointages attendus est nul");
            }
            
            double tauxPresence = ((double) nbPointages / pointagesAttendus) * 100.0;
            System.out.println("taux présence : " + tauxPresence);
            
            double tauxArrondi = Math.min(100.0, Math.max(0.0, Math.round(tauxPresence * 10.0) / 10.0));
            System.out.println("taux arrondi : " + tauxArrondi);
            
            return tauxArrondi;
            
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du taux de présence: " + e.getMessage());
            throw new RuntimeException("Impossible de calculer le taux de présence: " + e.getMessage(), e);
        }
    }
    
    private double calculerTauxRetard(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            return stats != null ? stats.getTauxRetardMoyen() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur calcul taux retard: " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerMoyenneHeuresTravaillees(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("moyenne heure : " + (stats != null ? stats.getMoyenneHeuresParEmploye() : "null"));
            return stats != null ? stats.getMoyenneHeuresParEmploye() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur calcul moyenne heures: " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerMoyenneHeuresSupplementaires(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("calcul moyen HS : " + (stats != null ? stats.getMoyenneHeuresSupParEmploye() : "null"));
            return stats != null ? stats.getMoyenneHeuresSupParEmploye() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur calcul moyenne heures sup: " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerTotalRetards(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            return stats != null ? stats.getTotalRetard() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur calcul total retards: " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerMasseSalariale(LocalDate dateDebut, LocalDate dateFin) {
        try {
            List<VuePaieComplete> paies = vuePaieCompleteService.getByPeriode(dateDebut, dateFin);
            double total = 0;
            
            if (paies != null) {
                for (VuePaieComplete paie : paies) {
                    if (paie.getSalaireBrut() != null) {
                        total += paie.getSalaireBrut().doubleValue();
                    }
                }
            }
            return total;
        } catch (Exception e) {
            System.out.println("Erreur calcul masse salariale: " + e.getMessage());
            return 0.0;
        }
    }
    
    private long calculerJoursOuvrables(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires");
        }
        
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException(
                String.format("La date de début (%s) est après la date de fin (%s)", 
                            dateDebut, dateFin));
        }
        
        long joursOuvrables = 0;
        LocalDate date = dateDebut;
        
        while (!date.isAfter(dateFin)) {
            // Lundi = 1, Mardi = 2, ..., Vendredi = 5
            if (date.getDayOfWeek().getValue() <= 5) {
                joursOuvrables++;
            }
            date = date.plusDays(1);
        }
        
        return joursOuvrables;
    }
    
    private double calculerTauxPresencePourDepartement(StatistiquesDepartementDTO dept, long joursOuvrables) {
        try {
            if (dept.getNombreEmployes() == null || dept.getNombreEmployes() == 0 || joursOuvrables == 0) {
                return 0.0;
            }
            // Formule simplifiée - à adapter selon vos besoins
            double heuresAttendues = dept.getNombreEmployes() * joursOuvrables * 8.0; // 8h par jour
            double heuresReelles = dept.getTotalHeuresTravailleesEnHeures();
            return heuresAttendues > 0 ? Math.min(100.0, (heuresReelles / heuresAttendues) * 100.0) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double calculerTauxRetardPourDepartement(StatistiquesDepartementDTO dept) {
        try {
            if (dept.getTotalHeuresTravaillees() == null || dept.getTotalHeuresTravaillees() == 0) {
                return 0.0;
            }
            double retard = dept.getTotalRetardEnHeures();
            double travail = dept.getTotalHeuresTravailleesEnHeures();
            return travail > 0 ? (retard / travail) * 100.0 : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Récupère toutes les données pour le tableau de bord RH (période par défaut : mois précédent)
     */
    public DashboardRHDTO getDashboardRH() {
        // Période par défaut : mois précédent
        LocalDate dateDebutPeriode = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate dateFinPeriode = LocalDate.now().minusMonths(1).withDayOfMonth(
            LocalDate.now().minusMonths(1).lengthOfMonth()
        );
        
        System.out.println("dateDebut par défaut : " + dateDebutPeriode);
        System.out.println("dateFinPar défaut : " + dateFinPeriode);
        
        try {
            // Essayer de récupérer la période de paie active
            var periodesActives = periodePaieService.getActivePeriodePaies();
            if (periodesActives != null && !periodesActives.isEmpty()) {
                dateDebutPeriode = periodesActives.get(0).getDateDebut();
                dateFinPeriode = periodesActives.get(0).getDateFin();
                System.out.println("Période active trouvée: " + dateDebutPeriode + " - " + dateFinPeriode);
            }
        } catch (Exception e) {
            System.out.println("Utilisation des dates par défaut pour le dashboard: " + e.getMessage());
        }
        
        return getDashboardRHByPeriode(dateDebutPeriode, dateFinPeriode);
    }
    
    public DashboardRHDTO getDashboardRHByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        // Validation
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires");
        }
        
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("Date début doit être avant date fin");
        }
        
        System.out.println("Génération du dashboard pour la période: " + dateDebut + " - " + dateFin);
        
        // Créer l'objet dashboard
        DashboardRHDTO dashboard = new DashboardRHDTO(dateDebut, dateFin);
        
        try {
            // Appeler toutes les méthodes de calcul avec les dates fournies
            calculerKPIs(dashboard, dateDebut, dateFin);
            calculerStatsEffectif(dashboard, dateDebut, dateFin);
            calculerStatsPresence(dashboard, dateDebut, dateFin);
            calculerStatsAbsencesConges(dashboard, dateDebut, dateFin);
            calculerStatsPointage(dashboard, dateDebut, dateFin);
            calculerTendances(dashboard);
            calculerTopEmployes(dashboard, dateDebut, dateFin);
            calculerAlertes(dashboard, dateDebut, dateFin);
            calculerDemandesCongesEnAttente(dashboard);
            calculerRepartitions(dashboard, dateDebut, dateFin);
            
            System.out.println("Dashboard généré avec succès");
        } catch (Exception e) {
            System.out.println("error +66666666666666666++++++++++++++++++++++++++++++++++++++++++++++++: " + e.getMessage());
            System.err.println("Erreur lors de la génération du dashboard: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dashboard;
    }
}