package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Dto.AttestationCongeData;
import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.InformationSociete;

@Service
public class AttestationCongeDataService {
    @Autowired
    InformationSocieteService informationSocieteService;

    public AttestationCongeData createCongeValidation(DemandeConge demande){
        AttestationCongeData attestation = new AttestationCongeData();
        attestation.setCommentaireManager(demande.getCommentaire());
        attestation.setDateDebut(demande.getDateDebut());
        attestation.setDateFin(demande.getDateFin());
        attestation.setEmailEmploye(demande.getEmploye().getEmail());
        attestation.setNbJours(demande.getNbJours());
        attestation.setNomEmploye(demande.getEmploye().getNom());
        attestation.setPrenomEmploye(demande.getEmploye().getPrenom());
        attestation.setStatut(demande.getStatut());
        if(attestation.getTypeConge() != null){
            attestation.setTypeConge(demande.getTypeConge().getIntitule()); 
        } else {
            attestation.setTypeConge(demande.getAutreMotif());
        }
        InformationSociete info_societe = informationSocieteService.getFirstSociete();
        attestation.setNomSociete(info_societe.getNomCompany());
        attestation.setLogo(info_societe.getLogo());

        return attestation;
    }
}
