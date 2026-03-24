package com.rh.manage.Dto;

public class StatistiquesDepartementDTO {
    private String idDepartement;
    private String departementNom;
    private Long nombreEmployes;
    private Integer totalHeuresTravaillees;
    private Integer totalRetard;
    private Integer totalHeuresSup;
    private Double moyenneHeuresParEmploye;
    private Double moyenneRetardParEmploye;
    private Double moyenneHeuresSupParEmploye;

    public StatistiquesDepartementDTO() {
    }

    public StatistiquesDepartementDTO(String idDepartement, String departementNom,
                                      Long nombreEmployes, Long totalHeuresTravaillees,
                                      Long totalRetard, Long totalHeuresSup) {
        this.idDepartement = idDepartement;
        this.departementNom = departementNom;
        this.nombreEmployes = nombreEmployes;
        this.totalHeuresTravaillees = totalHeuresTravaillees != null ? totalHeuresTravaillees.intValue() : 0;
        this.totalRetard = totalRetard != null ? totalRetard.intValue() : 0;
        this.totalHeuresSup = totalHeuresSup != null ? totalHeuresSup.intValue() : 0;
        
        if (nombreEmployes != null && nombreEmployes > 0) {
            this.moyenneHeuresParEmploye = this.totalHeuresTravaillees / (double) nombreEmployes;
            this.moyenneRetardParEmploye = this.totalRetard / (double) nombreEmployes;
            this.moyenneHeuresSupParEmploye = this.totalHeuresSup / (double) nombreEmployes;
        } else {
            this.moyenneHeuresParEmploye = 0.0;
            this.moyenneRetardParEmploye = 0.0;
            this.moyenneHeuresSupParEmploye = 0.0;
        }
    }

    // Getters et Setters
    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getDepartementNom() {
        return departementNom;
    }

    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }

    public Long getNombreEmployes() {
        return nombreEmployes;
    }

    public void setNombreEmployes(Long nombreEmployes) {
        this.nombreEmployes = nombreEmployes;
    }

    public Integer getTotalHeuresTravaillees() {
        return totalHeuresTravaillees;
    }

    public void setTotalHeuresTravaillees(Integer totalHeuresTravaillees) {
        this.totalHeuresTravaillees = totalHeuresTravaillees;
    }

    public Integer getTotalRetard() {
        return totalRetard;
    }

    public void setTotalRetard(Integer totalRetard) {
        this.totalRetard = totalRetard;
    }

    public Integer getTotalHeuresSup() {
        return totalHeuresSup;
    }

    public void setTotalHeuresSup(Integer totalHeuresSup) {
        this.totalHeuresSup = totalHeuresSup;
    }

    public Double getMoyenneHeuresParEmploye() {
        return moyenneHeuresParEmploye;
    }

    public void setMoyenneHeuresParEmploye(Double moyenneHeuresParEmploye) {
        this.moyenneHeuresParEmploye = moyenneHeuresParEmploye;
    }

    public Double getMoyenneRetardParEmploye() {
        return moyenneRetardParEmploye;
    }

    public void setMoyenneRetardParEmploye(Double moyenneRetardParEmploye) {
        this.moyenneRetardParEmploye = moyenneRetardParEmploye;
    }

    public Double getMoyenneHeuresSupParEmploye() {
        return moyenneHeuresSupParEmploye;
    }

    public void setMoyenneHeuresSupParEmploye(Double moyenneHeuresSupParEmploye) {
        this.moyenneHeuresSupParEmploye = moyenneHeuresSupParEmploye;
    }

    // Méthodes utilitaires pour conversion en heures
    public Double getTotalHeuresTravailleesEnHeures() {
        return totalHeuresTravaillees != null ? totalHeuresTravaillees / 60.0 : 0.0;
    }

    public Double getTotalRetardEnHeures() {
        return totalRetard != null ? totalRetard / 60.0 : 0.0;
    }

    public Double getTotalHeuresSupEnHeures() {
        return totalHeuresSup != null ? totalHeuresSup / 60.0 : 0.0;
    }
}
