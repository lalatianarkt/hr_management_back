// package com.rh.manage.Dto;

// import java.util.List;

// public class StatistiquesPointageDTO {
//     private Long nombreEmployes;
//     private Double totalHeuresTravaillees;
//     private Double totalRetard;
//     private Double totalHeuresSupplementaires;
//     private Double moyenneHeuresParEmploye;
//     private Double moyenneRetardParEmploye;
//     private Double moyenneHeuresSupParEmploye;
//     private List<PointageEmployeDTO> topRetardataires;
//     private List<PointageEmployeDTO> topTravailleurs;
//     private Double tauxRetardMoyen;
//     private Double tauxHeuresSupMoyen;
    
//     // Getters et Setters
//     public Long getNombreEmployes() { return nombreEmployes; }
//     public void setNombreEmployes(Long nombreEmployes) { this.nombreEmployes = nombreEmployes; }
    
//     public Double getTotalHeuresTravaillees() { return totalHeuresTravaillees; }
//     public void setTotalHeuresTravaillees(Double totalHeuresTravaillees) { this.totalHeuresTravaillees = totalHeuresTravaillees; }
    
//     public Double getTotalRetard() { return totalRetard; }
//     public void setTotalRetard(Double totalRetard) { this.totalRetard = totalRetard; }
    
//     public Double getTotalHeuresSupplementaires() { return totalHeuresSupplementaires; }
//     public void setTotalHeuresSupplementaires(Double totalHeuresSupplementaires) { this.totalHeuresSupplementaires = totalHeuresSupplementaires; }
    
//     public Double getMoyenneHeuresParEmploye() { return moyenneHeuresParEmploye; }
//     public void setMoyenneHeuresParEmploye(Double moyenneHeuresParEmploye) { this.moyenneHeuresParEmploye = moyenneHeuresParEmploye; }
    
//     public Double getMoyenneRetardParEmploye() { return moyenneRetardParEmploye; }
//     public void setMoyenneRetardParEmploye(Double moyenneRetardParEmploye) { this.moyenneRetardParEmploye = moyenneRetardParEmploye; }
    
//     public Double getMoyenneHeuresSupParEmploye() { return moyenneHeuresSupParEmploye; }
//     public void setMoyenneHeuresSupParEmploye(Double moyenneHeuresSupParEmploye) { this.moyenneHeuresSupParEmploye = moyenneHeuresSupParEmploye; }
    
//     public List<PointageEmployeDTO> getTopRetardataires() { return topRetardataires; }
//     public void setTopRetardataires(List<PointageEmployeDTO> topRetardataires) { this.topRetardataires = topRetardataires; }
    
//     public List<PointageEmployeDTO> getTopTravailleurs() { return topTravailleurs; }
//     public void setTopTravailleurs(List<PointageEmployeDTO> topTravailleurs) { this.topTravailleurs = topTravailleurs; }
    
//     public Double getTauxRetardMoyen() { return tauxRetardMoyen; }
//     public void setTauxRetardMoyen(Double tauxRetardMoyen) { this.tauxRetardMoyen = tauxRetardMoyen; }
    
//     public Double getTauxHeuresSupMoyen() { return tauxHeuresSupMoyen; }
//     public void setTauxHeuresSupMoyen(Double tauxHeuresSupMoyen) { this.tauxHeuresSupMoyen = tauxHeuresSupMoyen; }
// }

package com.rh.manage.Dto;

import java.util.List;

public class StatistiquesPointageDTO {
    private long nombreEmployes;
    private double totalHeuresTravaillees;
    private double totalRetard;
    private double totalHeuresSupplementaires;
    private double moyenneHeuresParEmploye;
    private double moyenneRetardParEmploye;
    private double moyenneHeuresSupParEmploye;
    private double tauxRetardMoyen;
    private double tauxHeuresSupMoyen;
    private List<PointageEmployeAgregatDTO> topRetardataires;
    private List<PointageEmployeAgregatDTO> topTravailleurs;

    // Constructeurs
    public StatistiquesPointageDTO() {
    }

    // Getters et Setters
    public long getNombreEmployes() {
        return nombreEmployes;
    }

    public void setNombreEmployes(long nombreEmployes) {
        this.nombreEmployes = nombreEmployes;
    }

    public double getTotalHeuresTravaillees() {
        return totalHeuresTravaillees;
    }

    public void setTotalHeuresTravaillees(double totalHeuresTravaillees) {
        this.totalHeuresTravaillees = totalHeuresTravaillees;
    }

    public double getTotalRetard() {
        return totalRetard;
    }

    public void setTotalRetard(double totalRetard) {
        this.totalRetard = totalRetard;
    }

    public double getTotalHeuresSupplementaires() {
        return totalHeuresSupplementaires;
    }

    public void setTotalHeuresSupplementaires(double totalHeuresSupplementaires) {
        this.totalHeuresSupplementaires = totalHeuresSupplementaires;
    }

    public double getMoyenneHeuresParEmploye() {
        return moyenneHeuresParEmploye;
    }

    public void setMoyenneHeuresParEmploye(double moyenneHeuresParEmploye) {
        this.moyenneHeuresParEmploye = moyenneHeuresParEmploye;
    }

    public double getMoyenneRetardParEmploye() {
        return moyenneRetardParEmploye;
    }

    public void setMoyenneRetardParEmploye(double moyenneRetardParEmploye) {
        this.moyenneRetardParEmploye = moyenneRetardParEmploye;
    }

    public double getMoyenneHeuresSupParEmploye() {
        return moyenneHeuresSupParEmploye;
    }

    public void setMoyenneHeuresSupParEmploye(double moyenneHeuresSupParEmploye) {
        this.moyenneHeuresSupParEmploye = moyenneHeuresSupParEmploye;
    }

    public double getTauxRetardMoyen() {
        return tauxRetardMoyen;
    }

    public void setTauxRetardMoyen(double tauxRetardMoyen) {
        this.tauxRetardMoyen = tauxRetardMoyen;
    }

    public double getTauxHeuresSupMoyen() {
        return tauxHeuresSupMoyen;
    }

    public void setTauxHeuresSupMoyen(double tauxHeuresSupMoyen) {
        this.tauxHeuresSupMoyen = tauxHeuresSupMoyen;
    }

    public List<PointageEmployeAgregatDTO> getTopRetardataires() {
        return topRetardataires;
    }

    public void setTopRetardataires(List<PointageEmployeAgregatDTO> topRetardataires) {
        this.topRetardataires = topRetardataires;
    }

    public List<PointageEmployeAgregatDTO> getTopTravailleurs() {
        return topTravailleurs;
    }

    public void setTopTravailleurs(List<PointageEmployeAgregatDTO> topTravailleurs) {
        this.topTravailleurs = topTravailleurs;
    }
}