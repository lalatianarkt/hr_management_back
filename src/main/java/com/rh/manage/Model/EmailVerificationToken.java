// package com.rh.manage.Model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// public class EmailVerificationToken {

//     @Id
//     private String token; // UUID généré

//     @OneToOne
//     @JoinColumn(name = "user_id")
//     private User user;

//     private LocalDateTime expiryDate;

//     private boolean verified = false;

//     // getters et setters
// }

