package com.rh.manage.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.AlerteDTO;
import com.rh.manage.Dto.DashboardRHDTO;
import com.rh.manage.Dto.DemandeCongeEnCoursDTO;
import com.rh.manage.Dto.EvolutionRetardsDTO;
import com.rh.manage.Dto.KPIDTO;
import com.rh.manage.Dto.PointageDetailDTO;
import com.rh.manage.Dto.PointageEmployeAgregatDTO;
import com.rh.manage.Dto.StatistiquesAbsenceDTO;
import com.rh.manage.Dto.StatistiquesDepartementDTO;
import com.rh.manage.Dto.StatistiquesPointageDTO;
import com.rh.manage.Dto.StatsAbsencesCongesDTO;
import com.rh.manage.Dto.StatsEffectifDTO;
import com.rh.manage.Dto.StatsPointageDTO;
import com.rh.manage.Dto.StatsPresenceDTO;
import com.rh.manage.Dto.TendanceMensuelleDTO;
import com.rh.manage.Dto.TopEmployeDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.VueDemandeConge;
import com.rh.manage.Model.VuePaieComplete;

import io.jsonwebtoken.Claims;

@Service
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
    private CalendrierTravailService calendrierTravailService;

    @Autowired
    private VuePointageEmployeV2Service vuePointageEmployeV2Service;

    @Autowired
    private PointageService pointageService;

    @Autowired
    private ManagerService managerService;

    /**
     * Calcule tous les KPI principaux
     */
    private void calculerKPIs(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        // 1. Effectif total
        Long nbTotalEmployeActif = employeService.countTotalEmployes();
        System.out.println("KPI : nombre d'employés : " + nbTotalEmployeActif);
        KPIDTO kpiEffectif = new KPIDTO();
        kpiEffectif.setTitre("Effectif actif");
        kpiEffectif.setValeur(nbTotalEmployeActif != null ? nbTotalEmployeActif : 0);
        kpiEffectif.setUnite("employés");
        kpiEffectif.setCouleur("success");
        kpiEffectif.setIcone("team");
        kpiEffectif.setDescription("Nombre total d'employés actifs dans l'entreprise");
        dashboard.setEffectifTotal(kpiEffectif);
        System.out.println("Effectif : " + dashboard.getEffectifTotal());
        
        // 2. Taux de présence (à calculer à partir des pointages)
        double tauxPresence = calculerTauxPresence(dateDebut, dateFin);
        KPIDTO kpiPresence = new KPIDTO();
        kpiPresence.setTitre("Taux de présence");
        kpiPresence.setValeur(Math.round(tauxPresence * 10.0) / 10.0);
        kpiPresence.setUnite("%");
        kpiPresence.setCouleur(tauxPresence >= 95 ? "success" : tauxPresence >= 90 ? "warning" : "danger");
        kpiPresence.setIcone("check-circle");
        kpiPresence.setDescription("Pourcentage moyen de présence des employés");
        dashboard.setTauxPresence(kpiPresence);
        
        // 3. Taux d'absentéisme
        StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales(dateDebut, dateFin);
        double tauxAbsenteeisme = statAbsence != null ? statAbsence.getTauxAbsence() : 0;
        KPIDTO kpiAbsenteeisme = new KPIDTO();
        kpiAbsenteeisme.setTitre("Taux d'absentéisme");
        kpiAbsenteeisme.setValeur(Math.round(tauxAbsenteeisme * 10.0) / 10.0);
        kpiAbsenteeisme.setUnite("%");
        kpiAbsenteeisme.setCouleur(tauxAbsenteeisme <= 3 ? "success" : tauxAbsenteeisme <= 8 ? "warning" : "danger");
        kpiAbsenteeisme.setIcone("warning");
        kpiAbsenteeisme.setDescription("Pourcentage de jours d'absence non justifiés");
        dashboard.setTauxAbsenteeisme(kpiAbsenteeisme);
        System.out.println("Taux d'absentéisme : " + kpiAbsenteeisme);
        
        // 4. Congés pris
        int totalConges = statAbsence != null ? statAbsence.getTotalConges().intValue() : 0;
        KPIDTO kpiConges = new KPIDTO();
        kpiConges.setTitre("Congés pris");
        kpiConges.setValeur(totalConges);
        kpiConges.setUnite("jours");
        kpiConges.setCouleur("info");
        kpiConges.setIcone("coffee");
        kpiConges.setDescription("Nombre total de jours de congés pris cette période");
        dashboard.setCongesPris(kpiConges);
        
        // 5. Heures travaillées
        double moyHeuresTravaillees = calculerMoyenneHeuresTravaillees(dateDebut, dateFin);
        System.out.println("Moyenne d'heures travaillées : " + moyHeuresTravaillees);
        KPIDTO kpiHeures = new KPIDTO();
        kpiHeures.setTitre("Heures travaillées");
        kpiHeures.setValeur(Math.round(moyHeuresTravaillees * 10.0) / 10.0);
        kpiHeures.setUnite("h/mois");
        kpiHeures.setCouleur("info");
        kpiHeures.setIcone("clock-circle");
        kpiHeures.setDescription("Moyenne d'heures travaillées par employé par mois");
        dashboard.setHeuresTravaillees(kpiHeures);
        
        // 6. Masse salariale
        double masseSalariale = calculerMasseSalariale(dateDebut, dateFin);
        System.out.println("Masse salariale : " + masseSalariale);
        KPIDTO kpiMasseSalariale = new KPIDTO();
        kpiMasseSalariale.setTitre("Masse salariale");
        double valeurMillions = Math.round(masseSalariale / 1_000_000.0 * 10.0) / 10.0;
        kpiMasseSalariale.setValeur(valeurMillions);
        kpiMasseSalariale.setUnite("M Ar/mois");
        kpiMasseSalariale.setCouleur(masseSalariale > 0 ? "success" : "warning");
        kpiMasseSalariale.setIcone("dollar");
        kpiMasseSalariale.setDescription("Masse salariale mensuelle totale");
        dashboard.setMasseSalariale(kpiMasseSalariale);
        
        // 7. Heures supplémentaires (total sur la période)
        double totalHeuresSupplementaires = calculerTotalHeuresSupplementaires(dateDebut, dateFin);
        KPIDTO kpiHeuresSup = new KPIDTO();
        kpiHeuresSup.setTitre("Heures supplémentaires");
        kpiHeuresSup.setValeur(Math.round(totalHeuresSupplementaires * 10.0) / 10.0);
        kpiHeuresSup.setUnite("h");
        kpiHeuresSup.setCouleur(totalHeuresSupplementaires > 10 ? "warning" : "info");
        kpiHeuresSup.setIcone("plus-circle");
        kpiHeuresSup.setDescription("Total des heures supplémentaires sur la période");
        dashboard.setHeuresSupplementaires(kpiHeuresSup);
        
        // 8. Retards cumulés (nombre d'occurrences sur la période)
        long nombreRetards = calculerNombreRetards(dateDebut, dateFin);
        KPIDTO kpiRetards = new KPIDTO();
        kpiRetards.setTitre("Retards cumulés");
        kpiRetards.setValeur(nombreRetards);
        kpiRetards.setUnite("retards");
        kpiRetards.setCouleur(nombreRetards > 50 ? "danger" : nombreRetards > 20 ? "warning" : "info");
        kpiRetards.setIcone("clock");
        kpiRetards.setDescription("Nombre total de retards sur la période");
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
            List<InfosProfessionnelles> allInfos = infosProfessionnellesService.getAllInfoProActif();
            if (allInfos != null) {
                for (InfosProfessionnelles info : allInfos) {
                    if (info.getPoste() != null && info.getPoste().getNom() != null) {
                        String poste = info.getPoste().getNom();
                        parPoste.put(poste, parPoste.getOrDefault(poste, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des postes : " + e.getMessage());
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
        
        // Jours travaillés moyens (calculé à partir de la période)
        long joursOuvrables = calendrierTravailService.calculerJoursOuvrables(dateDebut, dateFin);
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
            System.out.println("Erreur calcul des statistiques par département : " + e.getMessage());
        }
        
        stats.setTauxPresenceParDepartement(tauxPresenceParDept);
        stats.setTauxRetardParDepartement(tauxRetardParDept);
        
        dashboard.setStatsPresence(stats);
    }
    
    /**
     * Calcule les statistiques d'absences et de congés
     */
    private void calculerStatsAbsencesConges(DashboardRHDTO dashboard, LocalDate dateDebut, LocalDate dateFin) {
        StatsAbsencesCongesDTO stats = new StatsAbsencesCongesDTO();
        
        StatistiquesAbsenceDTO statAbsence = absenceCongeService.getStatistiquesGlobales(dateDebut, dateFin);
        
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
            System.out.println("Erreur calcul de la répartition des absences : " + e.getMessage());
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
                Map<String, Double> retardsParDept = new HashMap<>();
                
                List<StatistiquesDepartementDTO> statsDept = vuePointageEmployeV2Service.getStatistiquesDepartement(dateDebut, dateFin);
                for (StatistiquesDepartementDTO dept : statsDept) {
                    heuresParDept.put(dept.getDepartementNom(), dept.getTotalHeuresTravailleesEnHeures());
                    heuresSupParDept.put(dept.getDepartementNom(), dept.getTotalHeuresSupEnHeures());
                    retardsParDept.put(dept.getDepartementNom(), dept.getTotalRetardEnHeures());
                }
                
                stats.setHeuresParDepartement(heuresParDept);
                stats.setHeuresSupParDepartement(heuresSupParDept);
                stats.setRetardsParDepartement(retardsParDept);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des statistiques de pointage : " + e.getMessage());
        }
        
        dashboard.setStatsPointage(stats);
    }
    
    /**
     * Calcule les tendances mensuelles avec les vraies données
     */
    private void calculerTendances(DashboardRHDTO dashboard) {
        LocalDate dateFin = LocalDate.now();
        LocalDate dateDebut = dateFin.minusMonths(5).withDayOfMonth(1);
        dashboard.setTendances(getTendancesParPeriode(dateDebut, dateFin));
    }

    public List<TendanceMensuelleDTO> getTendancesParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("La date de début et la date de fin sont obligatoires.");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }

        List<TendanceMensuelleDTO> tendances = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);

        YearMonth start = YearMonth.from(dateDebut);
        YearMonth end = YearMonth.from(dateFin);

        for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
            LocalDate debutMois = ym.atDay(1);
            LocalDate finMois = ym.atEndOfMonth();

            LocalDate debutPeriode = debutMois.isBefore(dateDebut) ? dateDebut : debutMois;
            LocalDate finPeriode = finMois.isAfter(dateFin) ? dateFin : finMois;

            TendanceMensuelleDTO tendance = new TendanceMensuelleDTO();
            tendance.setMois(ym.format(formatter));

            try {
                Long effectif = employeService.countTotalEmployes();
                tendance.setEffectif(effectif != null ? effectif.intValue() : 0);

                double tauxPresence = calculerTauxPresence(debutPeriode, finPeriode);
                tendance.setTauxPresence(tauxPresence);

                double heuresTravaillees = calculerMoyenneHeuresTravaillees(debutPeriode, finPeriode);
                tendance.setHeuresTravaillees(heuresTravaillees);

                StatistiquesAbsenceDTO statsAbsence = absenceCongeService.getStatistiquesGlobales(debutPeriode, finPeriode);
                double congesPris = statsAbsence != null && statsAbsence.getTotalConges() != null
                        ? statsAbsence.getTotalConges().doubleValue()
                        : 0.0;
                tendance.setCongesPris(congesPris);

                if (!ym.equals(start)) {
                    YearMonth moisPrecedent = ym.minusMonths(1);
                    LocalDate debutPrecedent = moisPrecedent.atDay(1);
                    LocalDate finPrecedent = moisPrecedent.atEndOfMonth();

                    LocalDate debutPrec = debutPrecedent.isBefore(dateDebut) ? dateDebut : debutPrecedent;
                    LocalDate finPrec = finPrecedent.isAfter(dateFin) ? dateFin : finPrecedent;

                    double tauxPresencePrecedent = calculerTauxPresence(debutPrec, finPrec);
                    tendance.setVariationPresence(tauxPresence - tauxPresencePrecedent);
                }
            } catch (Exception e) {
                System.out.println("Erreur calcul de la tendance pour " + ym + " : " + e.getMessage());
                tendance.setEffectif(0);
                tendance.setTauxPresence(0.0);
                tendance.setHeuresTravaillees(0.0);
                tendance.setCongesPris(0.0);
                tendance.setVariationPresence(0.0);
            }

            tendances.add(tendance);
        }

        return tendances;
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
                System.out.println("Matricule : " + top.getMatricule());
                
                try {
                    InfosProfessionnelles infoPro = infosProfessionnellesService.findInfosProActifByMatricule(stat.getMatricule());
                    if (infoPro != null && infoPro.getDepartement() != null) {
                        top.setDepartement(infoPro.getDepartement().getNom());
                        top.setPoste(infoPro.getPoste().getNom());
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
            System.out.println("Erreur lors du calcul des meilleurs employés : " + e.getMessage());
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
                        top.setPoste(infoPro.getPoste().getNom());
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
            System.out.println("Erreur lors du calcul des plus grands retardataires : " + e.getMessage());
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
            StatistiquesAbsenceDTO statsAbsence = absenceCongeService.getStatistiquesGlobales(dateDebut, dateFin);
            if (statsAbsence != null && statsAbsence.getTauxAbsence() > 10) {
                AlerteDTO alerte = new AlerteDTO(
                    "absenteeisme",
                    "Taux d'absentéisme élevé",
                    String.format("Le taux d'absentéisme est de %.1f%%, supérieur au seuil de 10%%", 
                        statsAbsence.getTauxAbsence()),
                    "élevé"
                );
                alerte.setActionRecommandee("Analyser les causes et mettre en place des actions correctives.");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification de l'absentéisme : " + e.getMessage());
        }
        
        // Vérifier les demandes de congés en attente
        try {
            List<VueDemandeConge> demandesEnAttente = vueDemandeCongeService.getDemandesEnAttente();
            if (demandesEnAttente != null && demandesEnAttente.size() > 5) {
                AlerteDTO alerte = new AlerteDTO(
                    "conge",
                    "Demandes de congés en attente",
                    String.format("%d demandes de congés sont en attente de validation.", 
                        demandesEnAttente.size()),
                    "moyen"
                );
                alerte.setActionRecommandee("Traiter les demandes de congés en attente.");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des congés : " + e.getMessage());
        }
        
        // Vérifier les retards excessifs
        try {
            long nombreRetards = calculerNombreRetards(dateDebut, dateFin);
            if (nombreRetards > 50) {
                AlerteDTO alerte = new AlerteDTO(
                    "retard",
                    "Retards cumulés élevés",
                    String.format("Le nombre total de retards est de %d sur cette période.", nombreRetards),
                    "moyen"
                );
                alerte.setActionRecommandee("Sensibiliser les équipes au respect des horaires.");
                alertes.add(alerte);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des retards : " + e.getMessage());
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
            System.out.println("Erreur lors de la récupération des demandes de congés : " + e.getMessage());
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
            List<Departement> departements = departementService.getDepartementActif();
            for (Departement dept : departements) {
                masseSalarialeParDept.put(dept.getNom(), 0.0);
            }

            List<VuePaieComplete> paies = vuePaieCompleteService.getByPeriode(dateDebut, dateFin);
            double salaireBrutTotal = 0;
            
            if (paies != null && !paies.isEmpty()) {
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
            System.out.println("Erreur lors du calcul de la masse salariale : " + e.getMessage());
        }
    }
    
// ==================== MÉTHODES DE CALCUL AUXILIAIRES ====================
    
    private double calculerTauxPresence(LocalDate dateDebut, LocalDate dateFin) {
        try {
            // Récupérer les statistiques de pointage avec la V2
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("Statistiques de présence : " + (stats != null ? stats.getNombreEmployes() : "null"));
            if (stats == null) {
                return 0.0;
            }
            
            // Récupérer le nombre de pointages pour la période (version détaillée)
            List<com.rh.manage.Dto.PointageDetailDTO> pointagesDetails = vuePointageEmployeV2Service.getPointagesByPeriode(dateDebut, dateFin);
            System.out.println("Nombre de pointages : " + (pointagesDetails != null ? pointagesDetails.size() : 0));
            
            if (pointagesDetails == null) {
                return 0.0;
            }
            
            int nbPointages = pointagesDetails.size();
            if (nbPointages == 0) {
                return 0.0;
            }
            
            // Calculer le nombre de jours ouvrables dans la période (exclut week-end et jours fériés)
            long joursOuvrables = calendrierTravailService.calculerJoursOuvrablesAvecFeries(dateDebut, dateFin);
            if (joursOuvrables == 0) {
                return 0.0;
            }
            
            // Récupérer le nombre d'employés actifs
            Long nbEmployes = employeService.countTotalEmployes();
            if (nbEmployes == null) {
                return 0.0;
            }
            
            if (nbEmployes == 0) {
                return 0.0;
            }
            
            // Calculer le taux de présence
            long pointagesAttendus = nbEmployes * joursOuvrables;
            System.out.println("Pointages attendus : " + pointagesAttendus);
            
            if (pointagesAttendus == 0) {
                return 0.0;
            }
            
            double tauxPresence = ((double) nbPointages / pointagesAttendus) * 100.0;
            System.out.println("Taux de présence : " + tauxPresence);
            
            double tauxArrondi = Math.min(100.0, Math.max(0.0, Math.round(tauxPresence * 10.0) / 10.0));
            System.out.println("Taux arrondi : " + tauxArrondi);
            
            return tauxArrondi;
            
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du taux de présence : " + e.getMessage());
            throw new RuntimeException("Impossible de calculer le taux de présence : " + e.getMessage(), e);
        }
    }

    // Nouvelle méthode qui prend en compte les jours fériés
    private double calculerTauxRetard(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            return stats != null ? stats.getTauxRetardMoyen() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul du taux de retard : " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerMoyenneHeuresTravaillees(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("Moyenne d'heures : " + (stats != null ? stats.getMoyenneHeuresParEmploye() : "null"));
            return stats != null ? stats.getMoyenneHeuresParEmploye() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul de la moyenne d'heures : " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerTotalHeuresSupplementaires(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            System.out.println("Calcul du total des heures supplémentaires : " + (stats != null ? stats.getTotalHeuresSupplementaires() : "null"));
            return stats != null ? stats.getTotalHeuresSupplementaires() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul du total des heures supplémentaires : " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerTotalRetards(LocalDate dateDebut, LocalDate dateFin) {
        try {
            StatistiquesPointageDTO stats = vuePointageEmployeV2Service.getStatistiques(dateDebut, dateFin);
            return stats != null ? stats.getTotalRetard() : 0.0;
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul du total des retards : " + e.getMessage());
            return 0.0;
        }
    }

    private long calculerNombreRetards(LocalDate dateDebut, LocalDate dateFin) {
        try {
            List<PointageDetailDTO> pointages = vuePointageEmployeV2Service.getPointagesByPeriode(dateDebut, dateFin);
            if (pointages == null || pointages.isEmpty()) {
                return 0L;
            }

            return pointages.stream()
                    .filter(pointage -> pointage.getDureeRetard() != null && pointage.getDureeRetard() > 0)
                    .count();
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul du nombre de retards : " + e.getMessage());
            return 0L;
        }
    }

    public double calculerSalaireBrut(LocalDate dateDebut, LocalDate dateFin){
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
            System.out.println("Erreur lors du calcul de la masse salariale : " + e.getMessage());
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
                        total += paie.getTotalCharges().doubleValue();
                    }
                }
            }
            return total;
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul de la masse salariale : " + e.getMessage());
            return 0.0;
        }
    }
    
    private double calculerTauxPresencePourDepartement(StatistiquesDepartementDTO dept, long joursOuvrables) {
        try {
            if (dept.getNombreEmployes() == null || dept.getNombreEmployes() == 0 || joursOuvrables == 0) {
                return 0.0;
            }
            // Formule simplifiée - à adapter selon vos besoins
            double heuresAttendues = dept.getNombreEmployes() * joursOuvrables * 8.0; // 8 heures par jour
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
        
        System.out.println("Date de début par défaut : " + dateDebutPeriode);
        System.out.println("Date de fin par défaut : " + dateFinPeriode);
        
        try {
            // Essayer de récupérer la période de paie active
            var periodesActives = periodePaieService.getActivePeriodePaies();
            if (periodesActives != null && !periodesActives.isEmpty()) {
                dateDebutPeriode = periodesActives.get(0).getDateDebut();
                dateFinPeriode = periodesActives.get(0).getDateFin();
                System.out.println("Période active trouvée : " + dateDebutPeriode + " - " + dateFinPeriode);
            }
        } catch (Exception e) {
            System.out.println("Utilisation des dates par défaut pour le tableau de bord : " + e.getMessage());
        }
        
        return getDashboardRHByPeriode(dateDebutPeriode, dateFinPeriode);
    }
    
    public DashboardRHDTO getDashboardRHByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        // Validation
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }
        
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }
        
        System.out.println("Génération du tableau de bord pour la période : " + dateDebut + " - " + dateFin);
        
        // Créer l'objet dashboard
        DashboardRHDTO dashboard = new DashboardRHDTO(dateDebut, dateFin);
        
        try {
            // Appeler toutes les méthodes de calcul avec les dates fournies
            calculerKPIs(dashboard, dateDebut, dateFin);
            calculerStatsEffectif(dashboard, dateDebut, dateFin);
            calculerStatsPresence(dashboard, dateDebut, dateFin);
            calculerEvolutionRetards3DerniersMois(dashboard, dateFin);
            calculerStatsAbsencesConges(dashboard, dateDebut, dateFin);
            calculerStatsPointage(dashboard, dateDebut, dateFin);
            calculerTendances(dashboard);
            calculerTopEmployes(dashboard, dateDebut, dateFin);
            calculerAlertes(dashboard, dateDebut, dateFin);
            calculerDemandesCongesEnAttente(dashboard);
            calculerRepartitions(dashboard, dateDebut, dateFin);
            
            System.out.println("Tableau de bord généré avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur +66666666666666666 : " + e.getMessage());
            System.err.println("Erreur lors de la génération du tableau de bord : " + e.getMessage());
            e.printStackTrace();
        }
        
        return dashboard;
    }

    private void calculerEvolutionRetards3DerniersMois(DashboardRHDTO dashboard, LocalDate dateFinReference) {
        LocalDate finRef = dateFinReference != null ? dateFinReference : LocalDate.now();
        YearMonth moisFin = YearMonth.from(finRef);
        YearMonth moisDebut = moisFin.minusMonths(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);
        List<EvolutionRetardsDTO> evolution = new ArrayList<>();

        for (YearMonth ym = moisDebut; !ym.isAfter(moisFin); ym = ym.plusMonths(1)) {
            LocalDate debutMois = ym.atDay(1);
            LocalDate finMois = ym.atEndOfMonth();

            long nbRetards = calculerNombreRetards(debutMois, finMois);
            double tauxRetard = calculerTauxRetard(debutMois, finMois);

            evolution.add(new EvolutionRetardsDTO(
                    ym.format(formatter),
                    nbRetards,
                    Math.round(tauxRetard * 10.0) / 10.0
            ));
        }

        dashboard.setEvolutionRetards3DerniersMois(evolution);
        dashboard.setVariationRetards3DerniersMoisPourcentage(calculerVariationRetards3DerniersMoisPourcentage(evolution));
    }

    private Double calculerVariationRetards3DerniersMoisPourcentage(List<EvolutionRetardsDTO> evolution) {
        if (evolution == null || evolution.size() < 2) {
            return 0.0;
        }

        long nbDebut = evolution.get(0).getNombreRetards();
        long nbFin = evolution.get(evolution.size() - 1).getNombreRetards();

        if (nbDebut <= 0) {
            return 0.0;
        }

        double variation = ((nbFin - nbDebut) * 100.0) / nbDebut;
        return Math.round(variation * 10.0) / 10.0;
    }

    /**
     * Expose le calcul du taux de présence pour une période afin d'être utilisé par le contrôleur.
     */
    public double getTauxPresence(LocalDate dateDebut, LocalDate dateFin) {
        return calculerTauxPresence(dateDebut, dateFin);
    }

    /**
     * Expose le calcul de la masse salariale pour une période afin d'être utilisé par le contrôleur.
     */
    public double getMasseSalariale(LocalDate dateDebut, LocalDate dateFin) {
        return calculerMasseSalariale(dateDebut, dateFin);
    }

    public DashboardRHDTO getDashboardManagerParPeriodeParManager(LocalDate dateDebut, LocalDate dateFin, Claims claims){
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }
        String idEmploye = claims.get("idEmploye").toString();
        Manager manager = managerService.getManagerByEmploye(idEmploye);
        String idDepartement = manager.getDepartement().getId();
        return getDashboardManagerPeriode(dateDebut, dateFin, idDepartement);
    } 

    /**
     * Tableau de bord du manager sur une période donnée et pour un seul département
     */
    public DashboardRHDTO getDashboardManagerPeriode(LocalDate dateDebut, LocalDate dateFin, String idDepartement) {
        if (dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates sont obligatoires.");
        }
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }
        if (idDepartement == null || idDepartement.trim().isEmpty()) {
            throw new IllegalArgumentException("Le département est obligatoire.");
        }

        DashboardRHDTO dashboard = new DashboardRHDTO(dateDebut, dateFin);

        StatistiquesDepartementDTO statsDept = null;
        try {
            List<StatistiquesDepartementDTO> stats = vuePointageEmployeV2Service.getStatistiquesDepartement(dateDebut, dateFin);
            statsDept = stats.stream()
                    .filter(s -> idDepartement.equals(s.getIdDepartement()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des statistiques du département : " + e.getMessage());
        }

        if (statsDept == null) {
            statsDept = new StatistiquesDepartementDTO(idDepartement, "Département",
                    0L, 0L, 0L, 0L);
        }

        long nbEmployesDept = employeService.countEmployesByDepartement(idDepartement);
        long joursOuvrables = calendrierTravailService.calculerJoursOuvrablesAvecFeries(dateDebut, dateFin);
        double tauxPresenceDept = calculerTauxPresencePourDepartement(statsDept, joursOuvrables);

        StatistiquesAbsenceDTO statsAbsenceDept = absenceCongeService
                .getStatistiquesParPeriodeEtDepartement(dateDebut, dateFin, idDepartement);

        KPIDTO kpiEffectif = new KPIDTO();
        kpiEffectif.setTitre("Effectif (Département)");
        kpiEffectif.setValeur(nbEmployesDept);
        kpiEffectif.setUnite("employés");
        kpiEffectif.setCouleur("success");
        kpiEffectif.setIcone("team");
        kpiEffectif.setDescription("Nombre d'employés actifs du département.");
        dashboard.setEffectifTotal(kpiEffectif);

        KPIDTO kpiPresence = new KPIDTO();
        kpiPresence.setTitre("Taux de présence");
        kpiPresence.setValeur(Math.round(tauxPresenceDept * 10.0) / 10.0);
        kpiPresence.setUnite("%");
        kpiPresence.setCouleur(tauxPresenceDept >= 95 ? "success" : tauxPresenceDept >= 90 ? "warning" : "danger");
        kpiPresence.setIcone("check-circle");
        kpiPresence.setDescription("Taux de présence du département.");
        dashboard.setTauxPresence(kpiPresence);

        KPIDTO kpiAbs = new KPIDTO();
        kpiAbs.setTitre("Taux d'absentéisme");
        kpiAbs.setValeur(Math.round(statsAbsenceDept.getTauxAbsence() * 10.0) / 10.0);
        kpiAbs.setUnite("%");
        kpiAbs.setCouleur(statsAbsenceDept.getTauxAbsence() <= 3 ? "success" : statsAbsenceDept.getTauxAbsence() <= 8 ? "warning" : "danger");
        kpiAbs.setIcone("warning");
        kpiAbs.setDescription("Absences du département.");
        dashboard.setTauxAbsenteeisme(kpiAbs);

        KPIDTO kpiConges = new KPIDTO();
        kpiConges.setTitre("Congés pris");
        kpiConges.setValeur(statsAbsenceDept.getTotalConges() != null ? statsAbsenceDept.getTotalConges() : 0);
        kpiConges.setUnite("jours");
        kpiConges.setCouleur("info");
        kpiConges.setIcone("coffee");
        kpiConges.setDescription("Congés pris dans le département.");
        dashboard.setCongesPris(kpiConges);

        KPIDTO kpiHeures = new KPIDTO();
        kpiHeures.setTitre("Heures travaillées");
        kpiHeures.setValeur(Math.round(statsDept.getTotalHeuresTravailleesEnHeures() * 10.0) / 10.0);
        kpiHeures.setUnite("h");
        kpiHeures.setCouleur("info");
        kpiHeures.setIcone("clock-circle");
        kpiHeures.setDescription("Heures travaillées dans le département.");
        dashboard.setHeuresTravaillees(kpiHeures);

        KPIDTO kpiHeuresSup = new KPIDTO();
        kpiHeuresSup.setTitre("Heures supplémentaires");
        kpiHeuresSup.setValeur(Math.round(statsDept.getTotalHeuresSupEnHeures() * 10.0) / 10.0);
        kpiHeuresSup.setUnite("h");
        kpiHeuresSup.setCouleur(statsDept.getTotalHeuresSupEnHeures() > 10 ? "warning" : "info");
        kpiHeuresSup.setIcone("plus-circle");
        kpiHeuresSup.setDescription("Heures supplémentaires dans le département.");
        dashboard.setHeuresSupplementaires(kpiHeuresSup);

        KPIDTO kpiRetards = new KPIDTO();
        kpiRetards.setTitre("Retards cumulés");
        kpiRetards.setValeur(Math.round(statsDept.getTotalRetardEnHeures() * 10.0) / 10.0);
        kpiRetards.setUnite("h");
        kpiRetards.setCouleur(statsDept.getTotalRetardEnHeures() > 50 ? "danger" : statsDept.getTotalRetardEnHeures() > 20 ? "warning" : "info");
        kpiRetards.setIcone("clock");
        kpiRetards.setDescription("Retards cumulés dans le département.");
        dashboard.setRetardsCumules(kpiRetards);

        // Évaluation : évolution vs période précédente (taux de présence)
        long nbJours = ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
        LocalDate finPrec = dateDebut.minusDays(1);
        LocalDate debutPrec = finPrec.minusDays(nbJours - 1);

        double tauxPresencePrec = 0.0;
        try {
            List<StatistiquesDepartementDTO> statsPrec = vuePointageEmployeV2Service.getStatistiquesDepartement(debutPrec, finPrec);
            StatistiquesDepartementDTO deptPrec = statsPrec.stream()
                    .filter(s -> idDepartement.equals(s.getIdDepartement()))
                    .findFirst()
                    .orElse(null);
            if (deptPrec != null) {
                long joursPrec = calendrierTravailService.calculerJoursOuvrables(debutPrec, finPrec);
                tauxPresencePrec = calculerTauxPresencePourDepartement(deptPrec, joursPrec);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul du taux de présence précédent : " + e.getMessage());
        }

        double evaluation = tauxPresenceDept - tauxPresencePrec;
        KPIDTO kpiEvaluation = new KPIDTO();
        kpiEvaluation.setTitre("Évaluation");
        kpiEvaluation.setValeur(Math.round(evaluation * 10.0) / 10.0);
        kpiEvaluation.setUnite("pts");
        kpiEvaluation.setCouleur(evaluation >= 0 ? "success" : "danger");
        kpiEvaluation.setIcone("rise");
        kpiEvaluation.setDescription("Évolution du taux de présence par rapport à la période précédente.");
        dashboard.setEvaluation(kpiEvaluation);

        // Courbes : heures travaillées et retards par mois (département)
        try {
            List<PointageDetailDTO> pointages = vuePointageEmployeV2Service
                    .getPointagesByDepartementAndPeriode(idDepartement, dateDebut, dateFin);

            Map<YearMonth, double[]> aggregats = new HashMap<>();
            if (pointages != null) {
                for (PointageDetailDTO p : pointages) {
                    if (p.getDatePointage() == null) {
                        continue;
                    }
                    YearMonth ym = YearMonth.from(p.getDatePointage());
                    double[] values = aggregats.computeIfAbsent(ym, k -> new double[] {0.0, 0.0});
                    values[0] += p.getDureeHeureTravailleeEnHeures();
                    values[1] += p.getDureeRetardEnHeures();
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);
            List<TendanceMensuelleDTO> tendances = new ArrayList<>();
            YearMonth start = YearMonth.from(dateDebut);
            YearMonth end = YearMonth.from(dateFin);

            for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
                double[] values = aggregats.getOrDefault(ym, new double[] {0.0, 0.0});
                TendanceMensuelleDTO t = new TendanceMensuelleDTO();
                t.setMois(ym.format(formatter));
                t.setHeuresTravaillees(Math.round(values[0] * 10.0) / 10.0);
                t.setRetards(Math.round(values[1] * 10.0) / 10.0);
                tendances.add(t);
            }

            dashboard.setTendances(tendances);
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul des courbes heures/retards : " + e.getMessage());
            dashboard.setTendances(new ArrayList<>());
        }

        return dashboard;
    }
}
