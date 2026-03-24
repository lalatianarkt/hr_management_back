// package com.rh.manage.Model;

// import java.time.LocalDate;
// import java.util.Date;

// import com.fasterxml.jackson.annotation.JsonFormat;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "manager_employe")

// public class ManagerEmploye {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "date_debut", nullable = false)
//     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//     private Date dateDebut;

//     @Column(name = "date_fin")
//     private Date dateFin;

//     // @ManyToOne(fetch = FetchType.LAZY) 
//     @ManyToOne
//     @JoinColumn(name = "id_manager", nullable = false)
//     private Manager manager;

//     // @ManyToOne(fetch = FetchType.LAZY)
//     @ManyToOne
//     @JoinColumn(name = "id_employe", nullable = false)
//     private Employe employe;

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Date getDateDebut() {
//         return dateDebut;
//     }

//     public void setDateDebut(Date dateDebut) {
//         this.dateDebut = dateDebut;
//     }

//     public Date getDateFin() {
//         return dateFin;
//     }

//     public void setDateFin(Date dateFin) {
//         this.dateFin = dateFin;
//     }

//     public Manager getManager() {
//         return manager;
//     }

//     public void setManager(Manager manager) {
//         this.manager = manager;
//     }

//     public Employe getEmploye() {
//         return employe;
//     }

//     public void setEmploye(Employe employe) {
//         this.employe = employe;
//     }
// }

