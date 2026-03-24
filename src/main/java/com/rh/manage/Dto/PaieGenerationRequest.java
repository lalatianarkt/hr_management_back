package com.rh.manage.Dto;

import com.rh.manage.Model.Paie;
import com.rh.manage.Model.PeriodePaie;

import java.util.List;

public class PaieGenerationRequest {
    
    private PeriodePaie periode;
    private List<Paie> les_paies;
    
    // Constructeurs
    public PaieGenerationRequest() {}
    
    public PaieGenerationRequest(PeriodePaie periode, List<Paie> les_paies) {
        this.periode = periode;
        this.les_paies = les_paies;
    }
    
    // Getters et Setters
    public PeriodePaie getPeriode() {
        return periode;
    }
    
    public void setPeriode(PeriodePaie periode) {
        this.periode = periode;
    }
    
    public List<Paie> getLes_paies() {
        return les_paies;
    }
    
    public void setLes_paies(List<Paie> les_paies) {
        this.les_paies = les_paies;
    }
}
