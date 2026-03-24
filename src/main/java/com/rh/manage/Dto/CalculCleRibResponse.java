package com.rh.manage.Dto;

public class CalculCleRibResponse {
    private String cleRib;
    private String ribComplet;
    private boolean valide;

    public String getCleRib() {
        return cleRib;
    }

    public void setCleRib(String cleRib) {
        this.cleRib = cleRib;
    }

    public String getRibComplet() {
        return ribComplet;
    }

    public void setRibComplet(String ribComplet) {
        this.ribComplet = ribComplet;
    }

    public boolean isValide() {
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public CalculCleRibResponse(String cleRib) {
        this.cleRib = cleRib;
        this.valide = true;
    }
    
    public CalculCleRibResponse(String codeBanque, String codeGuichet, 
                                 String numeroCompte, String cleRib) {
        this.cleRib = cleRib;
        this.ribComplet = codeBanque + " " + codeGuichet + " " + 
                          numeroCompte + " " + cleRib;
        this.valide = true;
    }
}
