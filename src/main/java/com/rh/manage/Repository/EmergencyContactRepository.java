package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rh.manage.Model.EmergencyContact;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, String> {

    // Vérifie si un contact existe déjà par son nom
    boolean existsByContact(String contact);

    // Vérifie si un contact existe par email
    boolean existsByEmail(String email);
}
