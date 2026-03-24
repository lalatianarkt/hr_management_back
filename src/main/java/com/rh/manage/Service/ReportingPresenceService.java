package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Dto.ReportingPresenceDTO;
import com.rh.manage.Dto.ReportingPresenceFilterDTO;
import com.rh.manage.Dto.ReportingStatsDTO;
import com.rh.manage.Model.ReportingPresence;
import com.rh.manage.Repository.ReportingPresenceRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportingPresenceService {
    
    private final ReportingPresenceRepository reportingPresenceRepository;
    
    // Injection par constructeur (recommandée par Spring)
    @Autowired
    public ReportingPresenceService(ReportingPresenceRepository reportingPresenceRepository) {
        this.reportingPresenceRepository = reportingPresenceRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> getAllReportingPresence() {
        List<ReportingPresence> entities = reportingPresenceRepository.findAll();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> getReportingWithFilters(ReportingPresenceFilterDTO filter) {
        List<ReportingPresence> result = reportingPresenceRepository.findWithFilters(
                filter.getDepartementId(),
                filter.getNomComplet(),
                filter.getMinConges(),
                filter.getMaxConges()
        );
        
        // Appliquer le tri
        List<ReportingPresenceDTO> dtos = result.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        dtos.sort((a, b) -> applySort(a, b, filter.getTriPar(), filter.getOrdreTri()));
        
        return dtos;
    }

    // NOUVELLE MÉTHODE : Générer CSV filtré
    @Transactional(readOnly = true)
    public byte[] generateCsvExport(ReportingPresenceFilterDTO filter) {
        // Récupérer les données filtrées
        List<ReportingPresenceDTO> data = getReportingWithFilters(filter);
        
        // Générer le contenu CSV
        String csvContent = convertToCsv(data);
        
        // Convertir en bytes
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }
    
    // NOUVELLE MÉTHODE : Générer CSV complet
    @Transactional(readOnly = true)
    public byte[] generateCsvExportAll() {
        // Récupérer toutes les données
        List<ReportingPresenceDTO> data = getAllReportingPresence();
        
        // Générer le contenu CSV
        String csvContent = convertToCsv(data);
        
        // Convertir en bytes
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }
    
    // NOUVELLE MÉTHODE : Générer CSV par département seulement
    @Transactional(readOnly = true)
    public byte[] generateCsvExportByDepartement(String departementId) {
        List<ReportingPresenceDTO> data = getByDepartement(departementId);
        
        String csvContent = convertToCsv(data);
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }
    
    // Méthode privée pour convertir les données en CSV
    private String convertToCsv(List<ReportingPresenceDTO> data) {
        if (data == null || data.isEmpty()) {
            return "Matricule;Nom Complet;Département;Heures Travaillées (h);Retard (h);Heures Sup (h);Temps Effectif (h);Congés Payés (jours)\n";
        }
        
        StringBuilder csv = new StringBuilder();
        
        // En-tête simplifiée (colonnes affichées dans le tableau)
        csv.append("Matricule;Nom Complet;Département;Heures Travaillées (h);Retard (h);Heures Sup (h);Temps Effectif (h);Congés Payés (jours)\n");
        
        // Données
        for (ReportingPresenceDTO dto : data) {
            // Matricule
            csv.append(escapeCsv(dto.getMatricule())).append(";");
            
            // Nom complet
            csv.append(escapeCsv(dto.getNomComplet())).append(";");
            
            // Département
            csv.append(escapeCsv(dto.getDepartementNom())).append(";");
            
            // Heures travaillées
            csv.append(dto.getTotalHeureTravailleeEnHeures() != null ? 
                    String.format("%.2f", dto.getTotalHeureTravailleeEnHeures()) : "0.00").append(";");
            
            // Retard
            csv.append(dto.getTotalRetardEnHeures() != null ? 
                    String.format("%.2f", dto.getTotalRetardEnHeures()) : "0.00").append(";");
            
            // Heures supplémentaires
            csv.append(dto.getTotalHeureSupEnHeures() != null ? 
                    String.format("%.2f", dto.getTotalHeureSupEnHeures()) : "0.00").append(";");
            
            // Temps effectif
            csv.append(dto.getTempsEffectifEnHeures() != null ? 
                    String.format("%.2f", dto.getTempsEffectifEnHeures()) : "0.00").append(";");
            
            // Congés payés
            csv.append(dto.getTotalJoursCongesTermines() != null ? 
                    dto.getTotalJoursCongesTermines().toString() : "0");
            
            csv.append("\n");
        }
        
        return csv.toString();
    }
    
    // Méthode pour échapper les caractères spéciaux CSV
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // Remplacer les points-virgules par des virgules
        value = value.replace(";", ",");
        
        // Si la valeur contient des guillemets, les doubler
        if (value.contains("\"")) {
            value = value.replace("\"", "\"\"");
        }
        
        // Si la valeur contient des retours à la ligne ou des guillemets, l'entourer de guillemets
        if (value.contains("\n") || value.contains("\"") || value.contains(",")) {
            value = "\"" + value + "\"";
        }
        
        return value;
    }
    
    // NOUVELLE MÉTHODE : Nom de fichier intelligent
    public String generateExportFilename(ReportingPresenceFilterDTO filter) {
        StringBuilder filename = new StringBuilder("reporting_presence");
        
        // Ajouter les filtres au nom de fichier
        if (filter != null) {
            if (filter.getDepartementId() != null && !filter.getDepartementId().isEmpty()) {
                String deptSlug = filter.getDepartementId()
                    .toLowerCase()
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^a-z0-9_]", "");
                filename.append("_").append(deptSlug);
            }
            
            if (filter.getNomComplet() != null && !filter.getNomComplet().isEmpty()) {
                filename.append("_recherche");
            }
        } else {
            filename.append("_complet");
        }
        
        // Ajouter la date
        filename.append("_").append(LocalDate.now().toString());
        filename.append(".csv");
        
        return filename.toString();
    }
    
    @Transactional(readOnly = true)
    public ReportingPresenceDTO getByMatricule(String matricule) {
        ReportingPresence entity = reportingPresenceRepository.findByMatricule(matricule);
        if (entity == null) {
            return null;
        }
        return convertToDTO(entity);
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> getByDepartement(String departementId) {
        List<ReportingPresence> entities = reportingPresenceRepository.findByIdDepartement(departementId);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> getTopPerformers(Integer limit) {
        int actualLimit = limit != null && limit > 0 ? limit : 10;
        List<ReportingPresence> entities = reportingPresenceRepository.findTopByHeuresTravaillees();
        return entities.stream()
                .limit(actualLimit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ReportingStatsDTO getGlobalStats() {
        Long totalEmployes = reportingPresenceRepository.countTotalEmployes();
        Long totalHeures = reportingPresenceRepository.sumTotalHeuresTravaillees();
        Long totalJoursConges = reportingPresenceRepository.sumTotalJoursConges();
        
        // Initialiser à 0 si null
        totalEmployes = totalEmployes != null ? totalEmployes : 0L;
        totalHeures = totalHeures != null ? totalHeures : 0L;
        totalJoursConges = totalJoursConges != null ? totalJoursConges : 0L;
        
        Double moyenneHeures = totalEmployes > 0 ? 
                totalHeures.doubleValue() / (totalEmployes * 60.0) : 0.0;
        Double moyenneConges = totalEmployes > 0 ? 
                totalJoursConges.doubleValue() / totalEmployes : 0.0;
        
        // Calcul du taux d'absentéisme
        Double tauxAbsenteeisme = totalEmployes > 0 ? 
                (totalJoursConges.doubleValue() / (totalEmployes * 220.0)) * 100 : 0.0;
        
        ReportingStatsDTO stats = new ReportingStatsDTO();
        stats.setTotalEmployes(totalEmployes);
        stats.setTotalHeuresTravaillees(totalHeures);
        stats.setTotalJoursConges(totalJoursConges);
        stats.setMoyenneHeuresParEmploye(moyenneHeures);
        stats.setMoyenneCongesParEmploye(moyenneConges);
        stats.setTauxAbsenteeisme(tauxAbsenteeisme);
        stats.setPeriode("Données cumulées");
        
        return stats;
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> getStatsByDepartement() {
        return reportingPresenceRepository.getStatsParDepartement();
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> searchByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return getAllReportingPresence();
        }
        
        List<ReportingPresence> entities = reportingPresenceRepository.findByNomCompletContainingIgnoreCase(nom);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ReportingPresenceDTO> getEmployesAvecCongesSuperieursA(Integer joursMinimum) {
        if (joursMinimum == null || joursMinimum <= 0) {
            joursMinimum = 1;
        }
        
        List<ReportingPresence> entities = reportingPresenceRepository
                .findByNombreCongesTerminesGreaterThan(joursMinimum);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Méthode pour obtenir les statistiques formatées pour affichage
    public String getGlobalStatsFormatted() {
        ReportingStatsDTO stats = getGlobalStats();
        
        return String.format(
            "Statistiques Globales:\n" +
            "Total Employés: %d\n" +
            "Total Heures Travaillées: %d min (%.2f h)\n" +
            "Total Jours Congés: %d\n" +
            "Moyenne Heures/Employé: %.2f h\n" +
            "Moyenne Congés/Employé: %.2f jours\n" +
            "Taux d'Absentéisme: %.2f%%",
            stats.getTotalEmployes(),
            stats.getTotalHeuresTravaillees(),
            stats.getTotalHeuresTravaillees() / 60.0,
            stats.getTotalJoursConges(),
            stats.getMoyenneHeuresParEmploye(),
            stats.getMoyenneCongesParEmploye(),
            stats.getTauxAbsenteeisme()
        );
    }
    
    // Méthodes de conversion
    private ReportingPresenceDTO convertToDTO(ReportingPresence entity) {
        if (entity == null) {
            return null;
        }
        
        ReportingPresenceDTO dto = new ReportingPresenceDTO();
        
        // Données de base
        dto.setIdEmploye(entity.getIdEmploye());
        dto.setMatricule(entity.getMatricule());
        dto.setNomComplet(entity.getNomComplet());
        dto.setTotalHeureTravaillee(entity.getTotalHeureTravaillee());
        dto.setTotalRetard(entity.getTotalRetard());
        dto.setTotalHeureSup(entity.getTotalHeureSup());
        dto.setIdDepartement(entity.getIdDepartement());
        dto.setDepartementNom(entity.getDepartementNom());
        dto.setNombreCongesTermines(entity.getNombreCongesTermines());
        dto.setTotalJoursCongesTermines(entity.getTotalJoursCongesTermines());
        dto.setDateDernierCongeTermine(entity.getDateDernierCongeTermine());
        dto.setMoisDernierConge(entity.getMoisDernierConge());
        
        // Champs calculés
        dto.setTotalHeureTravailleeEnHeures(entity.getTotalHeureTravailleeEnHeures());
        dto.setTotalRetardEnHeures(entity.getTotalRetardEnHeures());
        dto.setTotalHeureSupEnHeures(entity.getTotalHeureSupEnHeures());
        dto.setTempsEffectifEnHeures(entity.getTempsEffectifEnHeures());
        dto.setPourcentageRetard(entity.getPourcentageRetard());
        dto.setPourcentageHeureSup(entity.getPourcentageHeureSup());
        dto.setTauxPresence(entity.getTauxPresence());
        dto.setMoyenneJoursParConge(entity.getMoyenneJoursParConge());
        dto.setFrequenceConges(entity.getFrequenceConges());
        
        return dto;
    }
    
    private int applySort(ReportingPresenceDTO a, ReportingPresenceDTO b, String triPar, String ordreTri) {
        if (triPar == null || triPar.trim().isEmpty() || "nom".equalsIgnoreCase(triPar.trim())) {
            return sortByNom(a, b, ordreTri);
        }
        
        String triParLower = triPar.toLowerCase().trim();
        int comparison = 0;
        
        switch (triParLower) {
            case "conges":
                comparison = sortByConges(a, b);
                break;
            case "heures":
                comparison = sortByHeures(a, b);
                break;
            case "retard":
                comparison = sortByRetard(a, b);
                break;
            case "departement":
                comparison = sortByDepartement(a, b);
                break;
            case "matricule":
                comparison = sortByMatricule(a, b);
                break;
            default:
                comparison = sortByNom(a, b, "asc");
        }
        
        // Inverser si ordre descendant
        if ("desc".equalsIgnoreCase(ordreTri)) {
            return -comparison;
        }
        
        return comparison;
    }
    
    private int sortByNom(ReportingPresenceDTO a, ReportingPresenceDTO b, String ordreTri) {
        String nomA = a.getNomComplet() != null ? a.getNomComplet() : "";
        String nomB = b.getNomComplet() != null ? b.getNomComplet() : "";
        return nomA.compareToIgnoreCase(nomB);
    }
    
    private int sortByConges(ReportingPresenceDTO a, ReportingPresenceDTO b) {
        Integer congesA = a.getNombreCongesTermines() != null ? a.getNombreCongesTermines() : 0;
        Integer congesB = b.getNombreCongesTermines() != null ? b.getNombreCongesTermines() : 0;
        return Integer.compare(congesA, congesB);
    }
    
    private int sortByHeures(ReportingPresenceDTO a, ReportingPresenceDTO b) {
        Double heuresA = a.getTotalHeureTravailleeEnHeures() != null ? a.getTotalHeureTravailleeEnHeures() : 0.0;
        Double heuresB = b.getTotalHeureTravailleeEnHeures() != null ? b.getTotalHeureTravailleeEnHeures() : 0.0;
        return Double.compare(heuresA, heuresB);
    }
    
    private int sortByRetard(ReportingPresenceDTO a, ReportingPresenceDTO b) {
        Double retardA = a.getPourcentageRetard() != null ? a.getPourcentageRetard() : 0.0;
        Double retardB = b.getPourcentageRetard() != null ? b.getPourcentageRetard() : 0.0;
        return Double.compare(retardA, retardB);
    }
    
    private int sortByDepartement(ReportingPresenceDTO a, ReportingPresenceDTO b) {
        String deptA = a.getDepartementNom() != null ? a.getDepartementNom() : "";
        String deptB = b.getDepartementNom() != null ? b.getDepartementNom() : "";
        return deptA.compareToIgnoreCase(deptB);
    }
    
    private int sortByMatricule(ReportingPresenceDTO a, ReportingPresenceDTO b) {
        String matA = a.getMatricule() != null ? a.getMatricule() : "";
        String matB = b.getMatricule() != null ? b.getMatricule() : "";
        return matA.compareToIgnoreCase(matB);
    }
}