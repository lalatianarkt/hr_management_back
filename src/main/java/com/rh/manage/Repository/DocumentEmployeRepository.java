package com.rh.manage.Repository;

import com.rh.manage.Model.DocumentEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentEmployeRepository extends JpaRepository<DocumentEmploye, String> {
    
    // Trouver tous les documents d'un employé, triés par date d'upload décroissante
    List<DocumentEmploye> findByEmployeIdAndStatutOrderByDateUploadDesc(String employeId, int statut);
    
    // Trouver les documents par employé et type
    List<DocumentEmploye> findByEmployeIdAndTypeDocumentIdOrderByDateUploadDesc(String employeId, String typeDocumentId);
    
    // Vérifier si un document existe pour un employé avec un nom spécifique
    boolean existsByEmployeIdAndNomFichier(String employeId, String nomFichier);
    
    // Compter les documents d'un employé
    long countByEmployeId(String employeId);
}