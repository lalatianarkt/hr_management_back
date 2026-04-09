// package com.rh.manage.Dto;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;

// public class DashboardRHDTO {
//     // ==================== MÉTADONNÉES ====================
//     private LocalDate dateGeneration = LocalDate.now();
//     private String periode; // "Mois en cours", "Mois précédent", etc.
//     private LocalDate dateDebutPeriode;
//     private LocalDate dateFinPeriode;
    
//     // ==================== KPI PRINCIPAUX (CARDS) ====================
//     private KPIDTO effectifTotal;
//     private KPIDTO tauxPresence;
//     private KPIDTO tauxAbsenteeisme;
//     private KPIDTO congesPris;
//     private KPIDTO heuresTravaillees;
//     private KPIDTO masseSalariale;
//     private KPIDTO heuresSupplementaires;
//     private KPIDTO retardsCumules;
    
//     // ==================== STATISTIQUES DÉTAILLÉES ====================
//     private StatsEffectifDTO statsEffectif;
//     private StatsPresenceDTO statsPresence;
//     private StatsAbsencesCongesDTO statsAbsencesConges;
//     private StatsPointageDTO statsPointage;
    
//     // ==================== TENDANCES MENSIELLES ====================
//     private List<TendanceMensuelleDTO> tendances;
    
//     // ==================== TOP/ALERTES ====================
//     private List<TopEmployeDTO> topAbsenteeisme;
//     private List<TopEmployeDTO> topPerformers;
//     private List<AlerteDTO> alertes;
//     private List<DemandeCongeEnCoursDTO> demandesCongesEnAttente;
    
//     // ==================== RÉPARTITION ====================
//     private Map<String, Integer> repartitionParDepartement;
//     private Map<String, Double> masseSalarialeParDepartement;

//     // vaovao
//     private List<TopEmployeDTO> topRetardataires; // NOUVEAU

//     public DashboardRHDTO(LocalDate dateDebutPeriode2, LocalDate dateFinPeriode2) {
//         //TODO Auto-generated constructor stub
//     }
//     public DashboardRHDTO() {
//         //TODO Auto-generated constructor stub
//     }

//     public LocalDate getDateGeneration() {
//         return dateGeneration;
//     }
//     public void setDateGeneration(LocalDate dateGeneration) {
//         this.dateGeneration = dateGeneration;
//     }
//     public String getPeriode() {
//         return periode;
//     }
//     public void setPeriode(String periode) {
//         this.periode = periode;
//     }
//     public LocalDate getDateDebutPeriode() {
//         return dateDebutPeriode;
//     }
//     public void setDateDebutPeriode(LocalDate dateDebutPeriode) {
//         this.dateDebutPeriode = dateDebutPeriode;
//     }
//     public LocalDate getDateFinPeriode() {
//         return dateFinPeriode;
//     }
//     public void setDateFinPeriode(LocalDate dateFinPeriode) {
//         this.dateFinPeriode = dateFinPeriode;
//     }
//     public KPIDTO getEffectifTotal() {
//         return effectifTotal;
//     }
//     public void setEffectifTotal(KPIDTO effectifTotal) {
//         this.effectifTotal = effectifTotal;
//     }
//     public KPIDTO getTauxPresence() {
//         return tauxPresence;
//     }
//     public void setTauxPresence(KPIDTO tauxPresence) {
//         this.tauxPresence = tauxPresence;
//     }
//     public KPIDTO getTauxAbsenteeisme() {
//         return tauxAbsenteeisme;
//     }
//     public void setTauxAbsenteeisme(KPIDTO tauxAbsenteeisme) {
//         this.tauxAbsenteeisme = tauxAbsenteeisme;
//     }
//     public KPIDTO getCongesPris() {
//         return congesPris;
//     }
//     public void setCongesPris(KPIDTO congesPris) {
//         this.congesPris = congesPris;
//     }
//     public KPIDTO getHeuresTravaillees() {
//         return heuresTravaillees;
//     }
//     public void setHeuresTravaillees(KPIDTO heuresTravaillees) {
//         this.heuresTravaillees = heuresTravaillees;
//     }
//     public KPIDTO getMasseSalariale() {
//         return masseSalariale;
//     }
//     public void setMasseSalariale(KPIDTO masseSalariale) {
//         this.masseSalariale = masseSalariale;
//     }
//     public KPIDTO getHeuresSupplementaires() {
//         return heuresSupplementaires;
//     }
//     public void setHeuresSupplementaires(KPIDTO heuresSupplementaires) {
//         this.heuresSupplementaires = heuresSupplementaires;
//     }
//     public KPIDTO getRetardsCumules() {
//         return retardsCumules;
//     }
//     public void setRetardsCumules(KPIDTO retardsCumules) {
//         this.retardsCumules = retardsCumules;
//     }
//     public StatsEffectifDTO getStatsEffectif() {
//         return statsEffectif;
//     }
//     public void setStatsEffectif(StatsEffectifDTO statsEffectif) {
//         this.statsEffectif = statsEffectif;
//     }
//     public StatsPresenceDTO getStatsPresence() {
//         return statsPresence;
//     }
//     public void setStatsPresence(StatsPresenceDTO statsPresence) {
//         this.statsPresence = statsPresence;
//     }
//     public StatsAbsencesCongesDTO getStatsAbsencesConges() {
//         return statsAbsencesConges;
//     }
//     public void setStatsAbsencesConges(StatsAbsencesCongesDTO statsAbsencesConges) {
//         this.statsAbsencesConges = statsAbsencesConges;
//     }
//     public StatsPointageDTO getStatsPointage() {
//         return statsPointage;
//     }
//     public void setStatsPointage(StatsPointageDTO statsPointage) {
//         this.statsPointage = statsPointage;
//     }
//     public List<TendanceMensuelleDTO> getTendances() {
//         return tendances;
//     }
//     public void setTendances(List<TendanceMensuelleDTO> tendances) {
//         this.tendances = tendances;
//     }
//     public List<TopEmployeDTO> getTopAbsenteeisme() {
//         return topAbsenteeisme;
//     }
//     public void setTopAbsenteeisme(List<TopEmployeDTO> topAbsenteeisme) {
//         this.topAbsenteeisme = topAbsenteeisme;
//     }
//     public List<TopEmployeDTO> getTopPerformers() {
//         return topPerformers;
//     }
//     public void setTopPerformers(List<TopEmployeDTO> topPerformers) {
//         this.topPerformers = topPerformers;
//     }
//     public List<AlerteDTO> getAlertes() {
//         return alertes;
//     }
//     public void setAlertes(List<AlerteDTO> alertes) {
//         this.alertes = alertes;
//     }
//     public List<DemandeCongeEnCoursDTO> getDemandesCongesEnAttente() {
//         return demandesCongesEnAttente;
//     }
//     public void setDemandesCongesEnAttente(List<DemandeCongeEnCoursDTO> demandesCongesEnAttente) {
//         this.demandesCongesEnAttente = demandesCongesEnAttente;
//     }
//     public Map<String, Integer> getRepartitionParDepartement() {
//         return repartitionParDepartement;
//     }
//     public void setRepartitionParDepartement(Map<String, Integer> repartitionParDepartement) {
//         this.repartitionParDepartement = repartitionParDepartement;
//     }
//     public Map<String, Double> getMasseSalarialeParDepartement() {
//         return masseSalarialeParDepartement;
//     }
//     public void setMasseSalarialeParDepartement(Map<String, Double> masseSalarialeParDepartement) {
//         this.masseSalarialeParDepartement = masseSalarialeParDepartement;
//     }
//     public List<TopEmployeDTO> getTopRetardataires() {
//         return topRetardataires;
//     }
//     public void setTopRetardataires(List<TopEmployeDTO> topRetardataires) {
//         this.topRetardataires = topRetardataires;
//     }

    
// }

// // ==================== CLASSES DE SUPPORT ====================

package com.rh.manage.Dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DashboardRHDTO {
    // ==================== MÉTADONNÉES ====================
    private LocalDate dateGeneration;
    private String periode;
    private LocalDate dateDebutPeriode;
    private LocalDate dateFinPeriode;
    
    // ==================== KPI PRINCIPAUX (CARDS) ====================
    private KPIDTO effectifTotal;
    private KPIDTO tauxPresence;
    private KPIDTO tauxAbsenteeisme;
    private KPIDTO congesPris;
    private KPIDTO heuresTravaillees;
    private KPIDTO masseSalariale;
    private KPIDTO heuresSupplementaires;
    private KPIDTO retardsCumules;
    private KPIDTO evaluation;
    
    // ==================== STATISTIQUES DÉTAILLÉES ====================
    private StatsEffectifDTO statsEffectif;
    private StatsPresenceDTO statsPresence;
    private StatsAbsencesCongesDTO statsAbsencesConges;
    private StatsPointageDTO statsPointage;
    
    // ==================== TENDANCES MENSUELLES ====================
    private List<TendanceMensuelleDTO> tendances;
    
    // ==================== TOP/ALERTES ====================
    private List<TopEmployeDTO> topAbsenteeisme;
    private List<TopEmployeDTO> topPerformers;
    private List<TopEmployeDTO> topRetardataires;  // NOUVEAU
    private List<AlerteDTO> alertes;
    private List<DemandeCongeEnCoursDTO> demandesCongesEnAttente;
    
    // ==================== RÉPARTITION ====================
    private Map<String, Integer> repartitionParDepartement;
    private Map<String, Double> masseSalarialeParDepartement;

    // ==================== CONSTRUCTEURS ====================
    
    /**
     * Constructeur principal avec période
     */
    public DashboardRHDTO(LocalDate dateDebutPeriode, LocalDate dateFinPeriode) {
        this.dateGeneration = LocalDate.now();
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
        this.periode = formaterPeriode(dateDebutPeriode, dateFinPeriode);
    }

    /**
     * Constructeur par défaut
     */
    public DashboardRHDTO() {
        this.dateGeneration = LocalDate.now();
        LocalDate now = LocalDate.now();
        this.dateDebutPeriode = now.withDayOfMonth(1);
        this.dateFinPeriode = now.withDayOfMonth(now.lengthOfMonth());
        this.periode = formaterPeriode(dateDebutPeriode, dateFinPeriode);
    }

    // ==================== MÉTHODES UTILITAIRES ====================
    
    private String formaterPeriode(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) {
            return "Période non définie";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Du " + debut.format(formatter) + " au " + fin.format(formatter);
    }

    public String getPeriodeFormatee() {
        return formaterPeriode(dateDebutPeriode, dateFinPeriode);
    }

    public String getPeriodeCourte() {
        if (dateDebutPeriode == null || dateFinPeriode == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        if (dateDebutPeriode.getMonth() == dateFinPeriode.getMonth() 
            && dateDebutPeriode.getYear() == dateFinPeriode.getYear()) {
            return dateDebutPeriode.format(formatter);
        }
        return dateDebutPeriode.format(formatter) + " - " + dateFinPeriode.format(formatter);
    }

    // ==================== GETTERS ET SETTERS ====================
    
    public LocalDate getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDate dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public LocalDate getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(LocalDate dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
        if (this.dateFinPeriode != null) {
            this.periode = formaterPeriode(dateDebutPeriode, this.dateFinPeriode);
        }
    }

    public LocalDate getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(LocalDate dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
        if (this.dateDebutPeriode != null) {
            this.periode = formaterPeriode(this.dateDebutPeriode, dateFinPeriode);
        }
    }

    public KPIDTO getEffectifTotal() {
        return effectifTotal;
    }

    public void setEffectifTotal(KPIDTO effectifTotal) {
        this.effectifTotal = effectifTotal;
    }

    public KPIDTO getTauxPresence() {
        return tauxPresence;
    }

    public void setTauxPresence(KPIDTO tauxPresence) {
        this.tauxPresence = tauxPresence;
    }

    public KPIDTO getTauxAbsenteeisme() {
        return tauxAbsenteeisme;
    }

    public void setTauxAbsenteeisme(KPIDTO tauxAbsenteeisme) {
        this.tauxAbsenteeisme = tauxAbsenteeisme;
    }

    public KPIDTO getCongesPris() {
        return congesPris;
    }

    public void setCongesPris(KPIDTO congesPris) {
        this.congesPris = congesPris;
    }

    public KPIDTO getHeuresTravaillees() {
        return heuresTravaillees;
    }

    public void setHeuresTravaillees(KPIDTO heuresTravaillees) {
        this.heuresTravaillees = heuresTravaillees;
    }

    public KPIDTO getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(KPIDTO masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public KPIDTO getHeuresSupplementaires() {
        return heuresSupplementaires;
    }

    public void setHeuresSupplementaires(KPIDTO heuresSupplementaires) {
        this.heuresSupplementaires = heuresSupplementaires;
    }

    public KPIDTO getRetardsCumules() {
        return retardsCumules;
    }

    public void setRetardsCumules(KPIDTO retardsCumules) {
        this.retardsCumules = retardsCumules;
    }

    public KPIDTO getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(KPIDTO evaluation) {
        this.evaluation = evaluation;
    }

    public StatsEffectifDTO getStatsEffectif() {
        return statsEffectif;
    }

    public void setStatsEffectif(StatsEffectifDTO statsEffectif) {
        this.statsEffectif = statsEffectif;
    }

    public StatsPresenceDTO getStatsPresence() {
        return statsPresence;
    }

    public void setStatsPresence(StatsPresenceDTO statsPresence) {
        this.statsPresence = statsPresence;
    }

    public StatsAbsencesCongesDTO getStatsAbsencesConges() {
        return statsAbsencesConges;
    }

    public void setStatsAbsencesConges(StatsAbsencesCongesDTO statsAbsencesConges) {
        this.statsAbsencesConges = statsAbsencesConges;
    }

    public StatsPointageDTO getStatsPointage() {
        return statsPointage;
    }

    public void setStatsPointage(StatsPointageDTO statsPointage) {
        this.statsPointage = statsPointage;
    }

    public List<TendanceMensuelleDTO> getTendances() {
        return tendances;
    }

    public void setTendances(List<TendanceMensuelleDTO> tendances) {
        this.tendances = tendances;
    }

    public List<TopEmployeDTO> getTopAbsenteeisme() {
        return topAbsenteeisme;
    }

    public void setTopAbsenteeisme(List<TopEmployeDTO> topAbsenteeisme) {
        this.topAbsenteeisme = topAbsenteeisme;
    }

    public List<TopEmployeDTO> getTopPerformers() {
        return topPerformers;
    }

    public void setTopPerformers(List<TopEmployeDTO> topPerformers) {
        this.topPerformers = topPerformers;
    }

    public List<TopEmployeDTO> getTopRetardataires() {
        return topRetardataires;
    }

    public void setTopRetardataires(List<TopEmployeDTO> topRetardataires) {
        this.topRetardataires = topRetardataires;
    }

    public List<AlerteDTO> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<AlerteDTO> alertes) {
        this.alertes = alertes;
    }

    public List<DemandeCongeEnCoursDTO> getDemandesCongesEnAttente() {
        return demandesCongesEnAttente;
    }

    public void setDemandesCongesEnAttente(List<DemandeCongeEnCoursDTO> demandesCongesEnAttente) {
        this.demandesCongesEnAttente = demandesCongesEnAttente;
    }

    public Map<String, Integer> getRepartitionParDepartement() {
        return repartitionParDepartement;
    }

    public void setRepartitionParDepartement(Map<String, Integer> repartitionParDepartement) {
        this.repartitionParDepartement = repartitionParDepartement;
    }

    public Map<String, Double> getMasseSalarialeParDepartement() {
        return masseSalarialeParDepartement;
    }

    public void setMasseSalarialeParDepartement(Map<String, Double> masseSalarialeParDepartement) {
        this.masseSalarialeParDepartement = masseSalarialeParDepartement;
    }

    // ==================== MÉTHODE POUR AFFICHAGE ====================
    
    @Override
    public String toString() {
        return "DashboardRHDTO{" +
                "periode='" + periode + '\'' +
                ", effectifTotal=" + (effectifTotal != null ? effectifTotal.getValeur() : "null") +
                ", tauxPresence=" + (tauxPresence != null ? tauxPresence.getValeur() : "null") +
                ", alertes=" + (alertes != null ? alertes.size() : 0) +
                '}';
    }
}















