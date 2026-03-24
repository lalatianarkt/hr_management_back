package com.rh.manage.View;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vue_paie_fille")
public class PaieFilleView {

    @Id
    @Column(name = "id_paie")
    private Long idPaie;

    @Column(name = "code")
    private String code;

    @Column(name = "plafond_mensuel", precision = 19, scale = 2)
    private BigDecimal plafondMensuel;

    @Column(name = "plafond_annuel", precision = 19, scale = 2)
    private BigDecimal plafondAnnuel;

    @Column(name = "montant", precision = 19, scale = 2)
    private BigDecimal montant;

    @Column(name = "taux", precision = 10, scale = 4)
    private BigDecimal taux;

    @Column(name = "base", precision = 19, scale = 2)
    private BigDecimal base;

    // Getters et Setters
    public Long getIdPaie() { return idPaie; }
    public void setIdPaie(Long idPaie) { this.idPaie = idPaie; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getPlafondMensuel() { return plafondMensuel; }
    public void setPlafondMensuel(BigDecimal plafondMensuel) { this.plafondMensuel = plafondMensuel; }

    public BigDecimal getPlafondAnnuel() { return plafondAnnuel; }
    public void setPlafondAnnuel(BigDecimal plafondAnnuel) { this.plafondAnnuel = plafondAnnuel; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public BigDecimal getTaux() { return taux; }
    public void setTaux(BigDecimal taux) { this.taux = taux; }

    public BigDecimal getBase() { return base; }
    public void setBase(BigDecimal base) { this.base = base; }
}
