package com.rh.manage.Model;

import java.io.Serializable;
import java.util.Objects;

public class VuePaieFilleId implements Serializable {
    private String idPaie;
    private String code;
    
    // Constructeur par défaut
    public VuePaieFilleId() {}
    
    // Constructeur avec paramètres
    public VuePaieFilleId(String idPaie, String code) {
        this.idPaie = idPaie;
        this.code = code;
    }
    
    // Getters et Setters
    public String getIdPaie() { return idPaie; }
    public void setIdPaie(String idPaie) { this.idPaie = idPaie; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    // equals() et hashCode() requis
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VuePaieFilleId that = (VuePaieFilleId) o;
        return Objects.equals(idPaie, that.idPaie) && Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idPaie, code);
    }
}
