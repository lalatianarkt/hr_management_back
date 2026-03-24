package com.rh.manage.Dto;

import java.util.List;

import com.rh.manage.Model.Departement;

public class StatRHGlobalDTO {
    int total_employe;
    List<Departement> les_departement;
    List<Integer> nb_employe_par_departement;
    double nb_demande_conge;
    double taux_conge;
    double nb_absence;
    double taux_absence;
    double masse_salariale;

    public int getTotal_employe() {
        return total_employe;
    }
    public void setTotal_employe(int total_employe) {
        this.total_employe = total_employe;
    }
    public List<Departement> getLes_departement() {
        return les_departement;
    }
    public void setLes_departement(List<Departement> les_departement) {
        this.les_departement = les_departement;
    }
    
    public double getNb_demande_conge() {
        return nb_demande_conge;
    }
    public void setNb_demande_conge(double nb_demande_conge) {
        this.nb_demande_conge = nb_demande_conge;
    }
    public double getTaux_conge() {
        return taux_conge;
    }
    public void setTaux_conge(double taux_conge) {
        this.taux_conge = taux_conge;
    }
    public double getNb_absence() {
        return nb_absence;
    }
    public void setNb_absence(double nb_absence) {
        this.nb_absence = nb_absence;
    }
    public double getTaux_absence() {
        return taux_absence;
    }
    public void setTaux_absence(double taux_absence) {
        this.taux_absence = taux_absence;
    }
    public double getMasse_salariale() {
        return masse_salariale;
    }
    public void setMasse_salariale(double masse_salariale) {
        this.masse_salariale = masse_salariale;
    }
    public List<Integer> getNb_employe_par_departement() {
        return nb_employe_par_departement;
    }
    public void setNb_employe_par_departement(List<Integer> nb_employe_par_departement) {
        this.nb_employe_par_departement = nb_employe_par_departement;
    }
    List<Double> les_heures_travaillees;

    
       
}
