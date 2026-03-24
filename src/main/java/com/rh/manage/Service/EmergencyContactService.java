package com.rh.manage.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Model.EmergencyContact;
import com.rh.manage.Repository.EmergencyContactRepository;

@Service
public class EmergencyContactService {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    // ✅ Récupérer tous les contacts d'urgence
    public List<EmergencyContact> getAll() {
        return emergencyContactRepository.findAll();
    }

    // ✅ Récupérer un contact par ID
    public Optional<EmergencyContact> getById(String id) {
        return emergencyContactRepository.findById(id);
    }

    // ✅ Ajouter un nouveau contact
    public EmergencyContact create(EmergencyContact contact) {
        // if (emergencyContactRepository.existsByContact(contact.getContact())) {
        //     throw new IllegalArgumentException("Le contact existe déjà !");
        // }
        // if (contact.getEmail() != null && emergencyContactRepository.existsByEmail(contact.getEmail())) {
        //     throw new IllegalArgumentException("L'email est déjà utilisé !");
        // }
        return emergencyContactRepository.save(contact);
    }

    // ✅ Mettre à jour un contact existant
    public EmergencyContact update(EmergencyContact contact) {
        if (contact.getId() == null || !emergencyContactRepository.existsById(contact.getId())) {
            throw new IllegalArgumentException("L'ID est invalide ou inexistant");
        }
        return emergencyContactRepository.save(contact);
    }

    // ✅ Supprimer un contact par ID
    public void deleteById(String id) {
        if (!emergencyContactRepository.existsById(id)) {
            throw new IllegalArgumentException("L'ID est inexistant");
        }
        emergencyContactRepository.deleteById(id);
    }

    public EmergencyContact update_contact(String id, EmergencyContact contact) {
        try {
            // Vérifier si le contact existe
            EmergencyContact emergency_to_update = getById(id)
                .orElseThrow(() -> new RuntimeException("Contact d'urgence non trouvé avec l'ID: " + id));
            
            // Mettre à jour les champs
            emergency_to_update.setAdresse(contact.getAdresse());
            emergency_to_update.setContact(contact.getContact());
            emergency_to_update.setEmail(contact.getEmail());
            emergency_to_update.setNom(contact.getNom());
            
            // Sauvegarder et retourner
            EmergencyContact urgence = update(emergency_to_update);
            return urgence;
            
        } catch (RuntimeException e) {
            // Relancer les exceptions métier (contact non trouvé)
            System.err.println("Erreur métier modification contact " + id + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Logger les erreurs techniques
            System.err.println("Erreur technique lors de la modification du contact " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur technique lors de la modification du contact d'urgence", e);
        }
    }
}
