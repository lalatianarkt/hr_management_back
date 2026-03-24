package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Recherche par email
    Optional<User> findByEmail(String email);
    
    // Recherche par employé
    Optional<User> findByEmployeId(String employeId);
    
    // Vérification d'existence par email
    boolean existsByEmail(String email);
    
    // Vérification d'existence par employé
    boolean existsByEmployeId(String employeId);
    
    // Recherche des utilisateurs créés après une certaine date
    List<User> findByCreatedAtAfter(LocalDateTime date);

        // Vérifie si un User est déjà lié à un employé donné (par id employe)
    boolean existsByEmploye_Id(String employeId);

}
