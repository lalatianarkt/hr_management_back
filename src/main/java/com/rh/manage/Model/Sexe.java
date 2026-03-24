package com.rh.manage.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "sexe")
public class Sexe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sexe", length = 50, nullable = false)
    private String sexe;

    @Column(name = "code", length = 1, nullable = false, unique = true)
    private String code;

    // Constructeurs
    public Sexe() {}

    public Sexe(String sexe, String code) {
        this.sexe = sexe;
        this.code = code;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getsexe() { return sexe; }
    public void setsexe(String sexe) { this.sexe = sexe; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return sexe + " (" + code + ")";
    }
}
