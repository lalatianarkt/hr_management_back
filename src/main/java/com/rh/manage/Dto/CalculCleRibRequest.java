package com.rh.manage.Dto;

public class CalculCleRibRequest {
    private String codeBanque; 
    private String codeGuichet;  
    private String numeroCompte;

    public String getCodeBanque() {
        return codeBanque;
    }
    public void setCodeBanque(String codeBanque) {
        this.codeBanque = codeBanque;
    }
    public String getCodeGuichet() {
        return codeGuichet;
    }
    public void setCodeGuichet(String codeGuichet) {
        this.codeGuichet = codeGuichet;
    }
    public String getNumeroCompte() {
        return numeroCompte;
    }
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }
}
