// package com.rh.manage.Service;

// import org.springframework.stereotype.Service;

// import com.rh.manage.Dto.PointageEmployeDTO;
// import com.rh.manage.Dto.StatistiquesPointageDTO;
// import com.rh.manage.Repository.VuePointageEmployeRepository;
// import com.rh.manage.View.VuePointageEmploye;

// import java.time.LocalDate;
// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// public class VuePointageEmployeService {
    
//     private final VuePointageEmployeRepository repository;
    
//     public VuePointageEmployeService(VuePointageEmployeRepository repository) {
//         this.repository = repository;
//     }
    
//     // Récupérer tous les pointages
//     public List<PointageEmployeDTO> getAllPointages() {
//         List<VuePointageEmploye> pointages = repository.findAll();
//         return pointages.stream()
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }
    
//     // Récupérer par ID
//     public Optional<PointageEmployeDTO> getById(String id) {
//         return repository.findById(id)
//             .map(this::convertirEnDTO);
//     }
    
//     // Récupérer par matricule
//     public Optional<PointageEmployeDTO> getByMatricule(String matricule) {
//         return repository.findByMatricule(matricule)
//             .map(this::convertirEnDTO);
//     }
    
//     // Rechercher par nom
//     public List<PointageEmployeDTO> searchByNom(String nom) {
//         return repository.findByNomCompletContainingIgnoreCase(nom)
//             .stream()
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }
    
//     // Top retardataires
//     public List<PointageEmployeDTO> getTopRetardataires(int limit) {
//         return repository.findAllByOrderByTotalRetardDesc()
//             .stream()
//             .limit(limit)
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }
    
//     // Top travailleurs (plus d'heures travaillées)
//     public List<PointageEmployeDTO> getTopTravailleurs(int limit) {
//         return repository.findAllByOrderByTotalHeureTravailleeDesc()
//             .stream()
//             .limit(limit)
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }
    
//     // Top heures supplémentaires
//     public List<PointageEmployeDTO> getTopHeuresSupplementaires(int limit) {
//         return repository.findAllByOrderByTotalHeureSupDesc()
//             .stream()
//             .limit(limit)
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }

//     List<VuePointageEmploye> findByDatePointageBetweenOrderByDatePointageDesc(LocalDate dateDebut, LocalDate dateFin){
//         return repository.findByDatePointageBetweenOrderByDatePointageDesc(dateDebut, dateFin);
//     }
    
//     // Calculer les statistiques globales
//     public StatistiquesPointageDTO getStatistiques(LocalDate dateDebut, LocalDate dateFin) {
//         StatistiquesPointageDTO stats = new StatistiquesPointageDTO();
        
//         // Récupérer toutes les données
//         List<VuePointageEmploye> tous = repository.findByDatePointageBetweenOrderByDatePointageDesc(dateDebut, dateFin);
        
//         if (tous.isEmpty()) {
//             return stats;
//         }
        
//         // Calculs
//         long nombreEmployes = tous.size();
//         double totalHeures = tous.stream()
//             .mapToDouble(v -> v.getTotalHeureTravailleeEnHeures())
//             .sum();
//         double totalRetard = tous.stream()
//             .mapToDouble(v -> v.getTotalRetardEnHeures())
//             .sum();
//         double totalHeuresSup = tous.stream()
//             .mapToDouble(v -> v.getTotalHeureSupEnHeures())
//             .sum();
        
//         stats.setNombreEmployes(nombreEmployes);
//         stats.setTotalHeuresTravaillees(Math.round(totalHeures * 100.0) / 100.0);
//         stats.setTotalRetard(Math.round(totalRetard * 100.0) / 100.0);
//         stats.setTotalHeuresSupplementaires(Math.round(totalHeuresSup * 100.0) / 100.0);
//         stats.setMoyenneHeuresParEmploye(Math.round((totalHeures / nombreEmployes) * 100.0) / 100.0);
//         stats.setMoyenneRetardParEmploye(Math.round((totalRetard / nombreEmployes) * 100.0) / 100.0);
//         stats.setMoyenneHeuresSupParEmploye(Math.round((totalHeuresSup / nombreEmployes) * 100.0) / 100.0);
        
//         // Top 5 retardataires
//         stats.setTopRetardataires(getTopRetardataires(5));
        
//         // Top 5 travailleurs
//         stats.setTopTravailleurs(getTopTravailleurs(5));
        
//         // Taux moyens
//         double tauxRetardMoyen = tous.stream()
//             .mapToDouble(VuePointageEmploye::getPourcentageRetard)
//             .average()
//             .orElse(0.0);
//         double tauxHeuresSupMoyen = tous.stream()
//             .mapToDouble(VuePointageEmploye::getPourcentageHeureSup)
//             .average()
//             .orElse(0.0);
        
//         stats.setTauxRetardMoyen(Math.round(tauxRetardMoyen * 100.0) / 100.0);
//         stats.setTauxHeuresSupMoyen(Math.round(tauxHeuresSupMoyen * 100.0) / 100.0);
        
//         return stats;
//     }

//     // Filtrer par critères multiples
//     public List<PointageEmployeDTO> filterPointages(String matricule, String nom, 
//                                                    Integer minHeures, Integer maxHeures,
//                                                    Integer minRetard, Integer maxRetard,
//                                                    Integer minHeuresSup, Integer maxHeuresSup) {
        
//         List<VuePointageEmploye> resultats = repository.findAll();
        
//         return resultats.stream()
//             .filter(v -> matricule == null || v.getMatricule().toLowerCase().contains(matricule.toLowerCase()))
//             .filter(v -> nom == null || v.getNomComplet().toLowerCase().contains(nom.toLowerCase()))
//             .filter(v -> minHeures == null || (v.getTotalHeureTravaillee() != null && v.getTotalHeureTravaillee() >= minHeures))
//             .filter(v -> maxHeures == null || (v.getTotalHeureTravaillee() != null && v.getTotalHeureTravaillee() <= maxHeures))
//             .filter(v -> minRetard == null || (v.getTotalRetard() != null && v.getTotalRetard() >= minRetard))
//             .filter(v -> maxRetard == null || (v.getTotalRetard() != null && v.getTotalRetard() <= maxRetard))
//             .filter(v -> minHeuresSup == null || (v.getTotalHeureSup() != null && v.getTotalHeureSup() >= minHeuresSup))
//             .filter(v -> maxHeuresSup == null || (v.getTotalHeureSup() != null && v.getTotalHeureSup() <= maxHeuresSup))
//             .map(this::convertirEnDTO)
//             .collect(Collectors.toList());
//     }

//     // Convertir Entity en DTO
//     private PointageEmployeDTO convertirEnDTO(VuePointageEmploye entity) {
//         PointageEmployeDTO dto = new PointageEmployeDTO();
//         dto.setIdEmploye(entity.getIdEmploye());
//         dto.setMatricule(entity.getMatricule());
//         dto.setNomComplet(entity.getNomComplet());
//         dto.setTotalHeureTravaillee(entity.getTotalHeureTravaillee());
//         dto.setTotalRetard(entity.getTotalRetard());
//         dto.setTotalHeureSup(entity.getTotalHeureSup());
        
//         // Les autres champs sont calculés automatiquement par les getters
//         return dto;
//     }
    
//     // Générer un rapport synthétique
//     // public Map<String, Object> generateRapport() {
//     //     Map<String, Object> rapport = new HashMap<>();
        
//     //     StatistiquesPointageDTO stats = getStatistiques();
//     //     rapport.put("statistiques", stats);
        
//     //     // Alertes
//     //     List<PointageEmployeDTO> retardSevere = repository.findEmployesAvecRetardSuperieurA(300) // 5 heures
//     //         .stream()
//     //         .map(this::convertirEnDTO)
//     //         .collect(Collectors.toList());
//     //     rapport.put("retardSevere", retardSevere);
        
//     //     List<PointageEmployeDTO> heuresSupImportantes = repository.findEmployesAvecHeuresSupSuperieuresA(1200) // 20 heures
//     //         .stream()
//     //         .map(this::convertirEnDTO)
//     //         .collect(Collectors.toList());
//     //     rapport.put("heuresSupImportantes", heuresSupImportantes);
        
//     //     // Distribution par plage d'heures
//     //     Map<String, Long> distributionHeures = new HashMap<>();
//     //     List<VuePointageEmploye> tous = repository.findAll();
        
//     //     distributionHeures.put("0-100h", tous.stream()
//     //         .filter(v -> v.getTotalHeureTravailleeEnHeures() < 100)
//     //         .count());
//     //     distributionHeures.put("100-150h", tous.stream()
//     //         .filter(v -> v.getTotalHeureTravailleeEnHeures() >= 100 && v.getTotalHeureTravailleeEnHeures() < 150)
//     //         .count());
//     //     distributionHeures.put("150-200h", tous.stream()
//     //         .filter(v -> v.getTotalHeureTravailleeEnHeures() >= 150 && v.getTotalHeureTravailleeEnHeures() < 200)
//     //         .count());
//     //     distributionHeures.put("200h+", tous.stream()
//     //         .filter(v -> v.getTotalHeureTravailleeEnHeures() >= 200)
//     //         .count());
        
//     //     rapport.put("distributionHeures", distributionHeures);
        
//     //     // Timestamp
//     //     rapport.put("timestamp", new Date());
//     //     rapport.put("generatedBy", "Pointage Service");
        
//     //     return rapport;
//     // }
    
// }