package com.rh.manage.Dto;

public class StatAllInfoDTO {
    private int nb_manager;
    private int nb_departement;
    private int nb_contrat_cdi;
    private int nb_contrat_cdd;
    private int nb_employe;
    private int nb_poste;
    private int nb_employes_en_conges;
    private int nb_competences_employes;
    private double nb_competences_added_dernier_mois; 
    private double nb_moyenne_competence_par_employe;

    // private int nb_absences;
    

    public int getNb_manager() {
        return nb_manager;
    }
    public int getNb_contrat_cdi() {
        return nb_contrat_cdi;
    }
    public void setNb_contrat_cdi(int nb_contrat_cdi) {
        this.nb_contrat_cdi = nb_contrat_cdi;
    }
    public int getNb_contrat_cdd() {
        return nb_contrat_cdd;
    }
    public void setNb_contrat_cdd(int nb_contrat_cdd) {
        this.nb_contrat_cdd = nb_contrat_cdd;
    }
    public int getNb_employes_en_conges() {
        return nb_employes_en_conges;
    }
    public void setNb_employes_en_conges(int nb_employes_en_conges) {
        this.nb_employes_en_conges = nb_employes_en_conges;
    }
    public int getNb_competences_employes() {
        return nb_competences_employes;
    }
    public void setNb_competences_employes(int nb_competences_employes) {
        this.nb_competences_employes = nb_competences_employes;
    }
    public double getNb_competences_added_dernier_mois() {
        return nb_competences_added_dernier_mois;
    }
    public void setNb_competences_added_dernier_mois(double nb_competences_added_dernier_mois) {
        this.nb_competences_added_dernier_mois = nb_competences_added_dernier_mois;
    }
    public double getNb_moyenne_competence_par_employe() {
        return nb_moyenne_competence_par_employe;
    }
    public void setNb_moyenne_competence_par_employe(double nb_moyenne_competence_par_employe) {
        this.nb_moyenne_competence_par_employe = nb_moyenne_competence_par_employe;
    }
    public void setNb_manager(int nb_manager) {
        this.nb_manager = nb_manager;
    }
    public int getNb_departement() {
        return nb_departement;
    }
    public void setNb_departement(int nb_departement) {
        this.nb_departement = nb_departement;
    }
   
    public int getNb_employe() {
        return nb_employe;
    }
    public void setNb_employe(int nb_employe) {
        this.nb_employe = nb_employe;
    }
    public int getNb_poste() {
        return nb_poste;
    }
    public void setNb_poste(int nb_poste) {
        this.nb_poste = nb_poste;
    }
}
