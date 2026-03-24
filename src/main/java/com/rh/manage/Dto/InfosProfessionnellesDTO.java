package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.Poste;
import com.rh.manage.Model.TypeContrat;

public class InfosProfessionnellesDTO {
    private LocalDate dateEmbauche;
    private Poste poste;
    private Departement departement;
    private TypeContrat typeContrat;
    private Manager manager;
    
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }
    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }
    public Poste getPoste() {
        return poste;
    }
    public void setPoste(Poste poste) {
        this.poste = poste;
    }
    public Departement getDepartement() {
        return departement;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    public TypeContrat getTypeContrat() {
        return typeContrat;
    }
    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }
    public Manager getManager() {
        return manager;
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    
    
}
