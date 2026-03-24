package com.rh.manage.Model;

import java.io.Serializable;
import java.util.Objects;

public class VuePaieCompleteId implements Serializable {
    
    private String idEmploye;
    private String paieId;
    
    // Constructeur par défaut (OBLIGATOIRE pour JPA)
    public VuePaieCompleteId() {
    }
    
    // Constructeur avec paramètres
    public VuePaieCompleteId(String idEmploye, String paieId) {
        this.idEmploye = idEmploye;
        this.paieId = paieId;
    }
    
    // Getters et Setters (OBLIGATOIRES)
    public String getIdEmploye() {
        return idEmploye;
    }
    
    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }
    
    public String getPaieId() {
        return paieId;
    }
    
    public void setPaieId(String paieId) {
        this.paieId = paieId;
    }
    
    // equals() OBLIGATOIRE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        VuePaieCompleteId that = (VuePaieCompleteId) o;
        
        if (!Objects.equals(idEmploye, that.idEmploye)) return false;
        return Objects.equals(paieId, that.paieId);
    }
    
    // hashCode() OBLIGATOIRE
    @Override
    public int hashCode() {
        int result = idEmploye != null ? idEmploye.hashCode() : 0;
        result = 31 * result + (paieId != null ? paieId.hashCode() : 0);
        return result;
    }
    
    // toString() optionnel mais recommandé
    @Override
    public String toString() {
        return "VuePaieCompleteId{" +
                "idEmploye='" + idEmploye + '\'' +
                ", paieId='" + paieId + '\'' +
                '}';
    }
}
