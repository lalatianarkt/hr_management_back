package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.TypePaiement;
import com.rh.manage.Repository.TypePaiementRepository;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TypePaiementService {
    
    @Autowired
    private TypePaiementRepository typePaiementRepository;
    
    public TypePaiement createTypePaiement(TypePaiement typePaiement) {
        if (typePaiementRepository.existsByCode(typePaiement.getCode())) {
            throw new DuplicateKeyException("Un type de paiement avec ce code existe déjà");
        }
        
        if (typePaiement.getId() == null || typePaiement.getId().isEmpty()) {
            typePaiement.setId(UUID.randomUUID().toString());
        }
        
        return typePaiementRepository.save(typePaiement);
    }
    
    public TypePaiement updateTypePaiement(String id, TypePaiement typePaiementDetails) {
        TypePaiement typePaiement = typePaiementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé avec l'id : " + id));
        
        typePaiement.setCode(typePaiementDetails.getCode());
        typePaiement.setLibelle(typePaiementDetails.getLibelle());
        
        return typePaiementRepository.save(typePaiement);
    }
    
    @Transactional(readOnly = true)
    public TypePaiement getTypePaiementById(String id) {
        return typePaiementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé avec l'id : " + id));
    }
    
    @Transactional(readOnly = true)
    public List<TypePaiement> getAllTypePaiements() {
        return typePaiementRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public TypePaiement getTypePaiementByCode(String code) {
        return typePaiementRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé avec le code : " + code));
    }
    
    public void deleteTypePaiement(String id) {
        TypePaiement typePaiement = typePaiementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé avec l'id : " + id));
        typePaiementRepository.delete(typePaiement);
    }
    
    @Transactional(readOnly = true)
    public List<TypePaiement> searchTypePaiements(String keyword) {
        return typePaiementRepository.searchByKeyword(keyword);
    }
}
