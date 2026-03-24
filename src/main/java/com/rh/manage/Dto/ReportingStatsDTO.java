package com.rh.manage.Dto;

public class ReportingStatsDTO {
    private Long totalEmployes;
    private Long totalHeuresTravaillees;
    private Long totalJoursConges;
    private Double moyenneHeuresParEmploye;
    private Double moyenneCongesParEmploye;
    private Double tauxAbsenteeisme;
    private String periode;
    
    // Constructeurs
    public ReportingStatsDTO() {}
    
    public ReportingStatsDTO(Long totalEmployes, Long totalHeuresTravaillees, 
                            Long totalJoursConges, Double moyenneHeuresParEmploye,
                            Double moyenneCongesParEmploye, Double tauxAbsenteeisme,
                            String periode) {
        this.totalEmployes = totalEmployes;
        this.totalHeuresTravaillees = totalHeuresTravaillees;
        this.totalJoursConges = totalJoursConges;
        this.moyenneHeuresParEmploye = moyenneHeuresParEmploye;
        this.moyenneCongesParEmploye = moyenneCongesParEmploye;
        this.tauxAbsenteeisme = tauxAbsenteeisme;
        this.periode = periode;
    }

    public Long getTotalEmployes() {
        return totalEmployes;
    }

    public void setTotalEmployes(Long totalEmployes) {
        this.totalEmployes = totalEmployes;
    }

    public Long getTotalHeuresTravaillees() {
        return totalHeuresTravaillees;
    }

    public void setTotalHeuresTravaillees(Long totalHeuresTravaillees) {
        this.totalHeuresTravaillees = totalHeuresTravaillees;
    }

    public Long getTotalJoursConges() {
        return totalJoursConges;
    }

    public void setTotalJoursConges(Long totalJoursConges) {
        this.totalJoursConges = totalJoursConges;
    }

    public Double getMoyenneHeuresParEmploye() {
        return moyenneHeuresParEmploye;
    }

    public void setMoyenneHeuresParEmploye(Double moyenneHeuresParEmploye) {
        this.moyenneHeuresParEmploye = moyenneHeuresParEmploye;
    }

    public Double getMoyenneCongesParEmploye() {
        return moyenneCongesParEmploye;
    }

    public void setMoyenneCongesParEmploye(Double moyenneCongesParEmploye) {
        this.moyenneCongesParEmploye = moyenneCongesParEmploye;
    }

    public Double getTauxAbsenteeisme() {
        return tauxAbsenteeisme;
    }

    public void setTauxAbsenteeisme(Double tauxAbsenteeisme) {
        this.tauxAbsenteeisme = tauxAbsenteeisme;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    
}