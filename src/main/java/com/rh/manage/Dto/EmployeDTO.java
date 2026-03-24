package com.rh.manage.Dto;

import java.util.List;

import com.rh.manage.Model.EmergencyContact;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosAdministratives;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.ModePaiement;
import com.rh.manage.Model.Nationalite;
import com.rh.manage.Model.Region;
import com.rh.manage.Model.Sexe;

public class EmployeDTO {
    private Employe employe;
    private EmergencyContact emergencyContact;
    private List<ModePaiement> modePaiements;
    private InfosAdministratives infosAdministratives;
    private List<InfosProfessionnelles> infosProfessionnelles;
    private Nationalite nationalite;
    private Sexe sexe;
    private Region region;

    public Sexe getSexe() {
        return sexe;
    }
    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }
    public Nationalite getNationalite() {
        return nationalite;
    }
    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }
    public InfosAdministratives getInfosAdministratives() {
        return infosAdministratives;
    }
    public void setInfosAdministratives(InfosAdministratives infosAdministratives) {
        this.infosAdministratives = infosAdministratives;
    }
    
    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }
    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    public List<InfosProfessionnelles> getInfosProfessionnelles() {
        return infosProfessionnelles;
    }
    public void setInfosProfessionnelles(List<InfosProfessionnelles> infosProfessionnelles) {
        this.infosProfessionnelles = infosProfessionnelles;
    }
    public Employe getEmploye() {
        return employe;
    }
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    public Region getRegion() {
        return region;
    }
    public void setRegion(Region region) {
        this.region = region;
    }
    public List<ModePaiement> getModePaiements() {
        return modePaiements;
    }
    public void setModePaiements(List<ModePaiement> modePaiements) {
        this.modePaiements = modePaiements;
    }
}
