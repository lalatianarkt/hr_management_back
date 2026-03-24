package com.rh.manage.Service;

import com.rh.manage.Model.CompetenceEmploye;
import com.rh.manage.Model.Competence;
import com.rh.manage.Model.Employe;
import com.rh.manage.Repository.CompetenceEmployeRepository;
import com.rh.manage.Repository.CompetenceRepository;
import com.rh.manage.Repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompetenceEmployeService {
    
    @Autowired
    private CompetenceEmployeRepository competenceEmployeRepository;
    
    @Autowired
    private CompetenceRepository competenceRepository;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    // === CRUD BASIQUE ===
    
    public CompetenceEmploye create(CompetenceEmploye competenceEmploye) {
        // Vérifier que l'employé existe
        Employe employe = employeRepository.findById(competenceEmploye.getEmploye().getId())
            .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'ID: " + competenceEmploye.getEmploye().getId()));
        
        // Vérifier que la compétence existe
        Competence competence = competenceRepository.findById(competenceEmploye.getCompetence().getId())
            .orElseThrow(() -> new RuntimeException("Compétence non trouvée avec l'ID: " + competenceEmploye.getCompetence().getId()));
        
        // Vérifier si l'association existe déjà
        if (competenceEmployeRepository.existsByEmployeIdAndCompetenceId(employe.getId(), competence.getId())) {
            throw new RuntimeException("Cet employé possède déjà cette compétence");
        }
        
        // Valider le niveau
        if (!competenceEmploye.isNiveauValide()) {
            throw new RuntimeException("Le niveau doit être compris entre 1 et 5");
        }
        
        // Définir les relations
        competenceEmploye.setEmploye(employe);
        competenceEmploye.setCompetence(competence);
        
        // Définir les dates
        if (competenceEmploye.getCreatedAt() == null) {
            competenceEmploye.setCreatedAt(LocalDateTime.now());
        }
        
        return competenceEmployeRepository.save(competenceEmploye);
    } 
    
    public List<CompetenceEmploye> getAll() {
        return competenceEmployeRepository.findAll();
    }
    
    public Optional<CompetenceEmploye> getById(String id) {
        return competenceEmployeRepository.findById(id);
    }
    
    public CompetenceEmploye update(String id, CompetenceEmploye competenceEmployeDetails) {
        CompetenceEmploye competenceEmploye = competenceEmployeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Association compétence-employé non trouvée avec l'ID: " + id));
        
        // Mettre à jour les champs modifiables
        if (competenceEmployeDetails.getNiveau() != null) {
            if (competenceEmployeDetails.getNiveau() < 1 || competenceEmployeDetails.getNiveau() > 5) {
                throw new RuntimeException("Le niveau doit être compris entre 1 et 5");
            }
            competenceEmploye.setNiveau(competenceEmployeDetails.getNiveau());
        }
        
        if (competenceEmployeDetails.getDateAcquisition() != null) {
            competenceEmploye.setDateAcquisition(competenceEmployeDetails.getDateAcquisition());
        }
        
        competenceEmploye.setModifiedAt(LocalDateTime.now());
        
        return competenceEmployeRepository.save(competenceEmploye);
    }
    
    public void deleteById(String id) {
        if (!competenceEmployeRepository.existsById(id)) {
            throw new RuntimeException("Association compétence-employé non trouvée avec l'ID: " + id);
        }
        competenceEmployeRepository.deleteById(id);
    }
    
    // === MÉTHODES MÉTIER ===
    
    public List<CompetenceEmploye> getByEmploye(String employeId) {
        return competenceEmployeRepository.findByEmployeId(employeId);
    }
    
    public List<CompetenceEmploye> getByCompetence(String competenceId) {
        return competenceEmployeRepository.findByCompetenceId(competenceId);
    }
    
    public Optional<CompetenceEmploye> getByEmployeAndCompetence(String employeId, String competenceId) {
        return competenceEmployeRepository.findByEmployeIdAndCompetenceId(employeId, competenceId);
    }
    
    public boolean existsByEmployeAndCompetence(String employeId, String competenceId) {
        return competenceEmployeRepository.existsByEmployeIdAndCompetenceId(employeId, competenceId);
    }
    
    public List<CompetenceEmploye> getByEmployeWithNiveauMin(String employeId, Integer niveauMin) {
        return competenceEmployeRepository.findByEmployeIdAndNiveauMin(employeId, niveauMin);
    }
    
    public Double getAverageNiveauByEmploye(String employeId) {
        return competenceEmployeRepository.findAverageNiveauByEmployeId(employeId);
    }
    
    public Double getAverageNiveauByCompetence(String competenceId) {
        return competenceEmployeRepository.findAverageNiveauByCompetenceId(competenceId);
    }
    
    public long countByEmploye(String employeId) {
        return competenceEmployeRepository.countByEmployeId(employeId);
    }
    
    public long countByCompetence(String competenceId) {
        return competenceEmployeRepository.countByCompetenceId(competenceId);
    }
    
    public void deleteByEmploye(String employeId) {
        competenceEmployeRepository.deleteByEmployeId(employeId);
    }
    
    public void deleteByCompetence(String competenceId) {
        competenceEmployeRepository.deleteByCompetenceId(competenceId);
    }
    
    // Ajouter une compétence à un employé (méthode utilitaire)
    public CompetenceEmploye addCompetenceToEmploye(String employeId, String competenceId, Integer niveau, LocalDate dateAcquisition) {
        CompetenceEmploye competenceEmploye = new CompetenceEmploye();
        
        Employe employe = new Employe();
        employe.setId(employeId);
        competenceEmploye.setEmploye(employe);
        
        Competence competence = new Competence();
        competence.setId(competenceId);
        competenceEmploye.setCompetence(competence);
        
        competenceEmploye.setNiveau(niveau);
        competenceEmploye.setDateAcquisition(dateAcquisition);
        
        return create(competenceEmploye);
    }
}