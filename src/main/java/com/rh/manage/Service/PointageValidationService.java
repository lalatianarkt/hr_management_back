// PointageValidationService.java
package com.rh.manage.Service;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Pointage;
import com.rh.manage.Model.PointageFille;
import com.rh.manage.Model.PointageImport;
import com.rh.manage.Model.ReglementHoraireInterieur;
import com.rh.manage.Utils.ValidationResult;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PointageValidationService {

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    PointageFilleService pointageFilleService;

    @Autowired
    PointageService pointageService;

    @Autowired
    ReglementHoraireInterieurService reglementHoraireInterieurService;

    @Autowired
    DemandeCongeService demandeCongeService;
    
    // Constantes pour les messages d'erreur
    public static final String ERR_ID_REQUIRED = "L'ID est obligatoire";
    public static final String ERR_ID_POSITIVE = "L'ID doit être positif";
    public static final String ERR_NAME_REQUIRED = "Le nom est obligatoire";
    public static final String ERR_NAME_LENGTH = "Le nom doit contenir entre 2 et 100 caractères";
    public static final String ERR_DEPT_LENGTH = "Le département doit contenir entre 1 et 50 caractères";
    public static final String ERR_TIME_REQUIRED = "La date/heure de pointage est obligatoire";
    public static final String ERR_TIME_FUTURE = "La date/heure ne peut pas être dans le futur";
    public static final String ERR_TIME_PAST = "La date/heure doit être après 2000-01-01";
    public static final String ERR_TIME_FORMAT = "Format de date/heure invalide";
    public static final String ERR_TYPE_REQUIRED = "Le type de pointage est obligatoire";
    public static final String ERR_TYPE_INVALID = "Type de pointage invalide. Valeurs acceptées: Check-In, Check-Out, Entrée, Sortie";
    public static final String ERR_MACHINE_LENGTH = "Le nom de la machine doit contenir entre 1 et 50 caractères";
    public static final String ERR_ID_FORMAT = "Format d'ID invalide. Doit être un nombre entier";
    
    public boolean isValidExcelFile(String contentType, String fileName) {
        String[] validExtensions = {".xlsx", ".xls", ".csv"};
        String[] validContentTypes = {
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel",
            "text/csv",
            "application/vnd.oasis.opendocument.spreadsheet"
        };
        
        // Vérifier par extension
        if (fileName != null) {
            String lowerFileName = fileName.toLowerCase();
            for (String ext : validExtensions) {
                if (lowerFileName.endsWith(ext)) {
                    return true;
                }
            }
        }
        
        // Vérifier par content-type
        if (contentType != null) {
            for (String validType : validContentTypes) {
                if (contentType.contains(validType)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public int findHeaderRow(Sheet sheet) {
    // Chercher la ligne contenant les en-têtes spécifiques
    String[] targetHeaders = {"ID", "Name", "Dept.", "Attendance Time", "Attendance Type", "Machine Name"};
    
    for (int i = 0; i <= Math.min(10, sheet.getLastRowNum()); i++) { // Chercher dans les 10 premières lignes
        Row row = sheet.getRow(i);
        if (row == null) continue;
        
        // Vérifier si cette ligne contient les en-têtes cibles
        int matchCount = 0;
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            String cellValue = getCellValue(cell);
            
            for (String target : targetHeaders) {
                if (target.equalsIgnoreCase(cellValue.trim())) {
                    matchCount++;
                }
            }
        }
        
        // Si on trouve au moins 3 des en-têtes cibles, on considère que c'est la ligne d'en-tête
        if (matchCount >= 3) {
            return i;
        }
    }
    
    return -1; // Non trouvé
}

    public Map<String, Integer> mapColumnIndexes(List<String> headers, String[] expectedColumns) {
        Map<String, Integer> columnIndexes = new HashMap<>();
        
        for (String expectedColumn : expectedColumns) {
            columnIndexes.put(expectedColumn, -1); // Initialiser à -1 (non trouvé)
            
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                if (expectedColumn.equalsIgnoreCase(header.trim())) {
                    columnIndexes.put(expectedColumn, i);
                    break;
                }
            }
        }
        
        return columnIndexes;
    }

    public boolean isEmptyRow(Row row) {
        if (row == null) return true;
        
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String value = getCellValue(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public Date parseAttendanceTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        
        String[] dateFormats = {
            "yyyy-MM-dd HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss",
            "dd.MM.yyyy HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "dd/MM/yyyy HH:mm",
            "MM/dd/yyyy HH:mm"
        };
        
        for (String format : dateFormats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(timeStr);
            } catch (ParseException e) {
                // Continuer avec le format suivant
            }
        }
        
        // Essayer avec des formats plus simples
        try {
            // Essayer comme timestamp Excel
            double excelTimestamp = Double.parseDouble(timeStr);
            return DateUtil.getJavaDate(excelTimestamp);
        } catch (NumberFormatException e) {
            // Pas un nombre
        }
        
        return null;
    }

    public String formatDate(Date date, String format) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    // Méthode pour extraire un PointageImport d'une ligne Excel
    private PointageImport extractPointageFromRow(Row row, Map<String, Integer> columnIndexes, int rowNumber) {
        PointageImport pointage = new PointageImport();
        
        try {
            // Extraire l'ID
            if (columnIndexes.containsKey("ID")) {
                Cell idCell = row.getCell(columnIndexes.get("ID"));
                String idValue = getCellValue(idCell);
                if (idValue != null && !idValue.trim().isEmpty()) {
                    try {
                        // pointage.setId(Integer.parseInt(idValue.trim()));
                        pointage.setId(idValue);
                    } catch (NumberFormatException e) {
                        System.err.println("  Format ID invalide ligne " + rowNumber + ": " + idValue);
                        pointage.setId(null);
                        // pointage.setId(0);
                    }
                }
            }
            
            // Extraire le nom
            if (columnIndexes.containsKey("Name")) {
                Cell nameCell = row.getCell(columnIndexes.get("Name"));
                String nameValue = getCellValue(nameCell);
                if (nameValue != null && !nameValue.trim().isEmpty()) {
                    pointage.setName(nameValue.trim());
                }
            }
            
            // Extraire le département
            if (columnIndexes.containsKey("Dept.")) {
                Cell deptCell = row.getCell(columnIndexes.get("Dept."));
                String deptValue = getCellValue(deptCell);
                if (deptValue != null && !deptValue.trim().isEmpty()) {
                    pointage.setDept(deptValue.trim());
                }
            }
            
            // Extraire la date/heure
            if (columnIndexes.containsKey("Attendance Time")) {
                Cell timeCell = row.getCell(columnIndexes.get("Attendance Time"));
                String timeValue = getCellValue(timeCell);
                if (timeValue != null && !timeValue.trim().isEmpty()) {
                    try {
                        // LocalDateTime dateTime = parseDateTime(timeValue.trim());
                        pointage.setAttendance_time(timeValue);
                    } catch (Exception e) {
                        System.err.println("  Format date/heure invalide ligne " + rowNumber + ": " + timeValue);
                    }
                }
            }
            
            // Extraire le type de pointage
            if (columnIndexes.containsKey("Attendance Type")) {
                Cell typeCell = row.getCell(columnIndexes.get("Attendance Type"));
                String typeValue = getCellValue(typeCell);
                if (typeValue != null && !typeValue.trim().isEmpty()) {
                    pointage.setAttendance_type(typeValue.trim());
                }
            }
            
            // Extraire le nom de la machine
            if (columnIndexes.containsKey("Machine Name")) {
                Cell machineCell = row.getCell(columnIndexes.get("Machine Name"));
                String machineValue = getCellValue(machineCell);
                if (machineValue != null && !machineValue.trim().isEmpty()) {
                    pointage.setMachine_name(machineValue.trim());
                }
            }
            
            // Débug: afficher les valeurs extraites
            System.out.println("  ID: " + pointage.getId() + 
                            ", Nom: " + pointage.getName() + 
                            ", Heure: " + (pointage.getAttendance_time()));
            
            return pointage;
            
        } catch (Exception e) {
            System.err.println("Erreur extraction ligne " + rowNumber + ": " + e.getMessage());
            return null;
        }
    }
    
    @Transactional
    public ValidationResult importDataFromSheet(Sheet sheet) {
        System.out.println("=== IMPORT DES DONNÉES DEPUIS LA FEUILLE ===");
        
        // Lire le nom de la société (première ligne)
        Row companyRow = sheet.getRow(0);
        String companyName = "";
        if (companyRow != null) {
            Cell companyCell = companyRow.getCell(0);
            companyName = getCellValue(companyCell);
            System.out.println("Nom de la société: " + companyName);
        }
        
        int headerRowIndex = findHeaderRow(sheet);
        if (headerRowIndex == -1) {
            System.out.println("❌ En-tête non trouvé dans la feuille");
            ValidationResult result = new ValidationResult();
            result.addGlobalError("En-tête non trouvé dans la feuille Excel");
            return result;
        } 
        
        // Afficher les en-têtes de colonnes
        Row headerRow = sheet.getRow(headerRowIndex);
        System.out.println("\n=== COLONNES DÉTECTÉES ===");
        List<String> headers = new ArrayList<>();
        Map<String, Integer> columnIndexes = new HashMap<>();
        
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            String headerName = getCellValue(cell);
            headers.add(headerName);
            columnIndexes.put(headerName, i);
            System.out.println("Colonne " + (i + 1) + ": '" + headerName + "'");
        }
        
        System.out.println("\n=== TRAITEMENT DES DONNÉES ===");
        List<PointageImport> les_pointages = new ArrayList<>();
        
        // Parcourir les lignes de données (après l'en-tête)
        for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isEmptyRow(row)) {
                System.out.println("Ligne " + (i + 1) + ": VIDE (ignorée)");
                continue;
            }
            
            System.out.println("\n--- Ligne " + (i + 1) + " ---");
            
            try {
                // Extraire les données de la ligne
                PointageImport pointageImport = extractPointageFromRow(row, columnIndexes, i + 1);
                
                if (pointageImport != null) {
                    les_pointages.add(pointageImport);
                    System.out.println("  ✅ Données extraites: " + pointageImport.getName());
                } else {
                    System.out.println("  ❌ Échec extraction des données");
                }
                
            } catch (Exception e) {
                System.err.println("  ❌ Erreur lors de l'extraction ligne " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        // Valider tous les pointages extraits
        ValidationResult validationResult = validatePointages(les_pointages);
        
        // AJOUT: Valider les paires de pointages pour les données VALIDES uniquement
        if (!validationResult.getValidPointages().isEmpty()) {
            System.out.println("\n=== VALIDATION DES PAIRES DE POINTAGES ===");
            
            // Valider uniquement les pointages qui sont valides individuellement
            Map<String, Object> pairValidation = validatePointagePairs(validationResult.getValidPointages());
            
            if (!(Boolean) pairValidation.get("isValid")) {
                List<String> pairErrors = (List<String>) pairValidation.get("errors");
                
                // Ajouter chaque erreur de paire comme erreur globale
                for (String error : pairErrors) {
                    validationResult.addGlobalError(error);
                    System.err.println("⚠️ " + error);
                }
                
                // Option: Si vous voulez rejeter les pointages des utilisateurs avec paires incomplètes
                // vous pouvez les retirer des validPointages ici
                System.out.println("❌ " + pairErrors.size() + " utilisateurs ont des pointages incomplets");
            } else {
                System.out.println("✅ Tous les utilisateurs ont des pointages complets (paires)");
            }
            
            // Afficher les statistiques
            System.out.println("Total utilisateurs: " + pairValidation.get("totalUsers"));
            System.out.println("Utilisateurs valides: " + pairValidation.get("validUsers"));
            System.out.println("Utilisateurs avec erreurs: " + pairValidation.get("usersWithErrors"));
        }
        
        System.out.println("\n=== RÉSUMÉ IMPORT ===");
        System.out.println("Société: " + companyName);
        System.out.println("Feuille: " + sheet.getSheetName());
        System.out.println("Lignes traitées: " + les_pointages.size());
        System.out.println("Lignes valides après validation: " + validationResult.getValidCount());
        System.out.println("Lignes avec erreurs: " + validationResult.getErrorCount());

        // if (!validationResult.hasErrors()) {
        //     List<PointageImport> les_pointages_importees = validationResult.getValidPointages();
        //     for (PointageImport pointageImport : les_pointages) {
        //         Pointage pointageMere = new Pointage();
        //         pointageMere.setDatePointage(pointageImport.getAttendance_time().toDate());
        //         InfosProfessionnelles infoPro = infosProfessionnellesService.findInfosProActifByMatricule(pointageImport.getId()); 
        //         pointageMere.setEmploye(infoPro.getEmploye()); 
        //         pointageMere.setIsShiftJour(null);
        //         pointageMere.setIsFerie(null);
        //         pointageMere.setDureeRetardMinute(null);
        //         pointageMere.setDureeHeureSupplementaire(null);
        //         pointageMere.setDateHeureArrivee(null);
        //         pointageMere.setDateHeureDepart(null);
        //         pointageService.createPointage(pointageMere);

        //         PointageFille pointageFille = new PointageFille();
        //         pointageFille.setDateHeurePointage(pointageImport.getAttendance_time());
        //         pointageFille.setPointage(pointageMere);
        //         pointageFille.setSource(pointageImport.getMachine_name());

        //         if(paire){
        //             pointageFille.setTypeAction("OUT"); 
        //         } else{
        //             pointageFille.setTypeAction("IN"); 
        //         }
        //     }   
        // }

        if (!validationResult.hasErrors()) {
            // 1. RÉCUPÉRER UNE SEULE FOIS TOUS LES INFOSPRO
            Map<String, InfosProfessionnelles> infosProMap = chargerInfosProEnMemoire();
            
            // 2. ASSEMBLER LES DONNÉES PAR EMPLOYÉ
            Map<String, Map<LocalDate, List<PointageImport>>> donneesAssemblees = 
                assemblerDonneesParEmploye(validationResult.getValidPointages(), infosProMap);
            
            // 3. CRÉER ET INSÉRER LES POINTAGES MÈRES ET FILLES
            insererPointagesMeresEtFilles(donneesAssemblees, infosProMap);
        } 
        
        return validationResult;
    }

    // Manomboka eto //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Méthode 1: Charger tous les infosPro en mémoire (une seule requête)
    private Map<String, InfosProfessionnelles> chargerInfosProEnMemoire() {
        System.out.println("📋 Chargement des employés actifs en mémoire...");
        
        List<InfosProfessionnelles> tousInfosPro = infosProfessionnellesService.getAllInfoProActif();
        
        // Créer une Map: matricule -> InfosProfessionnelles
        Map<String, InfosProfessionnelles> infosProMap = new HashMap<>();
        
        for (InfosProfessionnelles infoPro : tousInfosPro) {
            String matricule = infoPro.getMatricule() != null ? 
                            infoPro.getMatricule().toString() : null;
            
            if (matricule != null && !matricule.trim().isEmpty()) {
                infosProMap.put(matricule.trim(), infoPro);
            }
        }
        
        System.out.println("✅ " + infosProMap.size() + " employés chargés en mémoire");
        return infosProMap;
    }

    // Méthode 2: Assembler les données par employé et par date
    private Map<String, Map<LocalDate, List<PointageImport>>> assemblerDonneesParEmploye(
            List<PointageImport> pointagesValides,
            Map<String, InfosProfessionnelles> infosProMap) {
        
        System.out.println("🧩 Assemblage des données par employé...");
        
        // Structure: Matricule -> Date -> List<PointageImport>
        Map<String, Map<LocalDate, List<PointageImport>>> donneesAssemblees = new HashMap<>();
        int compteurSansCorrespondance = 0;
        
        for (PointageImport pointage : pointagesValides) {
            String matricule = pointage.getId();
            
            // Vérifier si l'employé existe
            if (!infosProMap.containsKey(matricule)) {
                compteurSansCorrespondance++;
                System.err.println("⚠️ Matricule non trouvé dans infosPro: " + matricule);
                continue;
            }
            
            LocalDate date = convertirEnLocalDate(pointage.getAttendance_time());
            
            // Ajouter au regroupement
            donneesAssemblees
                .computeIfAbsent(matricule, k -> new HashMap<>())
                .computeIfAbsent(date, k -> new ArrayList<>())
                .add(pointage);
        }
        
        // Trier les pointages de chaque jour par heure
        donneesAssemblees.values().forEach(dateMap -> 
            dateMap.values().forEach(list -> 
                list.sort(Comparator.comparing(PointageImport::getAttendance_time))
            )
        );
        
        System.out.println("✅ Données assemblées: " + donneesAssemblees.size() + 
                        " employés, " + compteurSansCorrespondance + " sans correspondance");
        
        return donneesAssemblees;
    }

    // Méthode pour convertir String en LocalDateTime
    private LocalDateTime convertirEnLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Essayer le format "dd/MM/yyyy HH:mm"
            if (dateTimeStr.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return LocalDateTime.parse(dateTimeStr.trim(), formatter);
            }
            // Essayer le format ISO "yyyy-MM-dd HH:mm:ss"
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(dateTimeStr.trim(), formatter);
            }
            // Essayer le format ISO sans secondes "yyyy-MM-dd HH:mm"
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(dateTimeStr.trim(), formatter);
            }
            else {
                throw new IllegalArgumentException("Format de date non reconnu: " + dateTimeStr);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Impossible de parser la date: " + dateTimeStr, e);
        }
    }

    // Méthode pour convertir String en LocalDate
    private LocalDate convertirEnLocalDate(String dateTimeStr) {
        LocalDateTime dateTime = convertirEnLocalDateTime(dateTimeStr);
        return dateTime != null ? dateTime.toLocalDate() : null;
    }

    @Transactional
    // Méthode 3: Créer et insérer les pointages
    private void insererPointagesMeresEtFilles(Map<String, Map<LocalDate, List<PointageImport>>> donneesAssemblees, 
                                               Map<String, InfosProfessionnelles> infosProMap) {
        ReglementHoraireInterieur reglementActuel = reglementHoraireInterieurService.getReglementActif();
        //    ReglementHoraireInterieur getReglementActif = reglement 
        System.out.println("💾 Création des pointages mères et filles...");
        
        List<Pointage> toutesPointagesMeres = new ArrayList<>();
        List<PointageFille> toutesPointagesFilles = new ArrayList<>();
        
        // Pour chaque employé
        for (Map.Entry<String, Map<LocalDate, List<PointageImport>>> entryEmploye : 
            donneesAssemblees.entrySet()) {
            
            String matricule = entryEmploye.getKey();
            Map<LocalDate, List<PointageImport>> pointagesParDate = entryEmploye.getValue();
            
            // Récupérer l'employé (déjà en mémoire)
            InfosProfessionnelles infoPro = infosProMap.get(matricule);
            Employe employe = infoPro.getEmploye();
            
            // Pour chaque jour de cet employé
            for (Map.Entry<LocalDate, List<PointageImport>> entryDate : 
                pointagesParDate.entrySet()) {
                
                LocalDate date = entryDate.getKey();
                List<PointageImport> pointagesDuJour = entryDate.getValue();
                
                if (pointagesDuJour.isEmpty()) {
                    continue;
                }
                
                // Créer le pointage mère pour ce jour
                Pointage pointageMere = creerPointageMere(date, employe, pointagesDuJour, reglementActuel);
                toutesPointagesMeres.add(pointageMere);
                
                // Créer les pointages filles pour ce jour
                List<PointageFille> pointagesFilles = creerPointagesFilles(
                    pointageMere, pointagesDuJour);
                toutesPointagesFilles.addAll(pointagesFilles);
            }
        }
        
        // INSÉRER EN BATCH
        insererEnBatch(toutesPointagesMeres, toutesPointagesFilles);
    }

    // Méthode pour créer un pointage mère
    // private Pointage creerPointageMere(LocalDate date, Employe employe, 
    //                                 List<PointageImport> pointagesDuJour, 
    //                                 ReglementHoraireInterieur reglementActuel) {

    //     LocalTime heureEntree = reglementActuel.getHeureMatEntree();
    //     LocalTime heureSortie = reglementActuel.getHeureApremSortie();
    //     BigDecimal duree_pause = reglementActuel.getDureeNormalePauseMinutes();
    //     BigDecimal duree_heure_journaliere_attendue = reglementActuel.getHeureNormaleJournaliere();
    //     Pointage pointage = new Pointage();
        
    //     // ID sera généré automatiquement
    //     pointage.setDatePointage(date);
    //     pointage.setEmploye(employe);
        
    //     // Calculer les heures d'arrivée et départ
    //     LocalDateTime heureArrivee =  convertirEnLocalDateTime(pointagesDuJour.get(0).getAttendance_time());
    //     LocalDateTime heureDepart = convertirEnLocalDateTime(pointagesDuJour.get(pointagesDuJour.size() - 1)
    //                                             .getAttendance_time());
        
    //     pointage.setDateHeureArrivee(heureArrivee);
    //     pointage.setDateHeureDepart(heureDepart);
        
    //     // Calculer la durée travaillée (en minutes)
    //     long dureeMinutes = Duration.between(heureArrivee, heureDepart).toMinutes();
    //     pointage.setDureeHeureTravailleeMinute((double) dureeMinutes);
        
        
    //     // Calculer retard (exemple: si arrivée après 8h30)
    //     Double retard = calculerRetard(heureArrivee, employe);
    //     pointage.setDureeRetardMinute(retard);
        
    //     // Calculer heures supplémentaires (exemple: si durée > 7h30)
    //     Double heuresSupp = calculerHeuresSupplementaires(dureeMinutes, employe);
    //     pointage.setDureeHeureSupplementaire(heuresSupp);
        
    //     // Autres champs
    //     pointage.setIsShiftJour(calculerShiftJour(heureArrivee));
    //     pointage.setIsFerie(estJourFerie(date));
    //     pointage.setIsWeekEnd(estWeekEnd(date));
    //     pointage.setStatutPointage(Pointage.Statut.BROUILLON.getCode());
    //     pointage.setCommentaire("Importé depuis Excel - " + LocalDateTime.now());
        
    //     return pointage;
    // }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private Pointage creerPointageMere(LocalDate date, Employe employe, 
                                    List<PointageImport> pointagesDuJour, 
                                    ReglementHoraireInterieur reglementActuel) {
    
    Pointage pointage = new Pointage();
    if(demandeCongeService.findByDate(employe.getId(), LocalDate.now()).isPresent()){
        pointage.setEstEnConge(true);
    }
    
    // Informations de base
    pointage.setDatePointage(date);
    pointage.setEmploye(employe);
    
    try {
        // Convertir les heures de pointage
        LocalDateTime heureArrivee = convertirEnLocalDateTime(
            pointagesDuJour.get(0).getAttendance_time());
        LocalDateTime heureDepart = convertirEnLocalDateTime(
            pointagesDuJour.get(pointagesDuJour.size() - 1).getAttendance_time());
        
        pointage.setDateHeureArrivee(heureArrivee);
        pointage.setDateHeureDepart(heureDepart);
        
        // Extraire les heures du règlement
        LocalTime heureEntreeNormale = reglementActuel.getHeureMatEntree();
        LocalTime heureSortieNormale = reglementActuel.getHeureApremSortie();
        BigDecimal dureePauseMinutes = reglementActuel.getDureeNormalePauseMinutes();
        
        // 1. CALCUL DU RETARD
        Double retard = calculerRetardSelonReglement(
            heureArrivee, heureEntreeNormale);
        pointage.setDureeRetardMinute(retard);
        
        // 2. CALCUL DES HEURES TRAVAILLÉES BRUTES
        Double heuresTravailleesBrutes = calculerHeuresTravailleesSelonReglement(
            heureArrivee, heureDepart, heureEntreeNormale, retard);
        
        // 3. DÉDUCTION DE LA PAUSE
        Double heuresTravailleesNet = deduirePause(
            heuresTravailleesBrutes, dureePauseMinutes);
        pointage.setDureeHeureTravailleeMinute(heuresTravailleesNet);
        
        // 4. CALCUL DES HEURES SUPPLÉMENTAIRES
        Double heuresSupplementaires = calculerHeuresSupplementairesSelonReglement(
            heureDepart, heureSortieNormale);
        pointage.setDureeHeureSupplementaire(heuresSupplementaires);
        
        // 5. CALCUL DU SHIFT JOUR/NUIT
        Boolean isShiftJour = calculerShiftJour(heureArrivee);
        pointage.setIsShiftJour(isShiftJour);
        
        // Log des calculs
        System.out.printf("✅ Calculs pour %s le %s:%n" +
                         "  - Retard: %.0f min%n" +
                         "  - Heures travaillées (net): %.2f min%n" +
                         "  - Heures supplémentaires: %.2f min%n" +
                         "  - Shift: %s%n",
                employe.getNom(), date, 
                retard != null ? retard : 0.0,
                heuresTravailleesNet != null ? heuresTravailleesNet : 0.0,
                heuresSupplementaires != null ? heuresSupplementaires : 0.0,
                isShiftJour ? "JOUR" : "NUIT");
        
    } catch (Exception e) {
        System.err.println("❌ Erreur dans les calculs pour " + employe.getNom() + 
                         " le " + date + ": " + e.getMessage());
        e.printStackTrace();
        
        // Valeurs par défaut en cas d'erreur
        pointage.setDureeRetardMinute(0.0);
        pointage.setDureeHeureTravailleeMinute(0.0);
        pointage.setDureeHeureSupplementaire(0.0);
        pointage.setIsShiftJour(true);
    }
    
    // Autres champs
    pointage.setIsFerie(estJourFerie(date));
    pointage.setIsWeekEnd(estWeekEnd(date));
    pointage.setStatutPointage(Pointage.Statut.BROUILLON.getCode());
    pointage.setCommentaire("Importé depuis Excel - " + LocalDateTime.now());
    
    return pointage;
}

// 1. CALCUL DU RETARD
private Double calculerRetardSelonReglement(
        LocalDateTime heureArrivee, 
        LocalTime heureEntreeNormale) {
    
    if (heureArrivee == null || heureEntreeNormale == null) {
        return 0.0;
    }
    
    LocalTime heureArriveeTime = heureArrivee.toLocalTime();
    
    // Si arrivée <= heure normale → pas de retard
    if (!heureArriveeTime.isAfter(heureEntreeNormale)) {
        return 0.0;
    }
    
    // Calculer le retard en minutes
    long minutesRetard = Duration.between(heureEntreeNormale, heureArriveeTime)
                                .toMinutes();
    
    return minutesRetard > 0 ? (double) minutesRetard : 0.0;
}

// 2. CALCUL DES HEURES TRAVAILLÉES
private Double calculerHeuresTravailleesSelonReglement(
        LocalDateTime heureArrivee,
        LocalDateTime heureDepart,
        LocalTime heureEntreeNormale,
        Double retard) {
    
    if (heureArrivee == null || heureDepart == null || heureEntreeNormale == null) {
        return 0.0;
    }
    
    LocalTime heureArriveeTime = heureArrivee.toLocalTime();
    
    // Déterminer l'heure de début à prendre en compte
    LocalDateTime heureDebutCalcul;
    
    if (retard == null || retard <= 0) {
        // Pas de retard : commencer à l'heure normale d'entrée
        // Même si arrivé plus tôt, on commence à l'heure normale
        heureDebutCalcul = LocalDateTime.of(heureArrivee.toLocalDate(), heureEntreeNormale);
    } else {
        // Retard : commencer à l'heure d'arrivée réelle
        heureDebutCalcul = heureArrivee;
    }
    
    // S'assurer que l'heure de début ne dépasse pas l'heure de départ
    if (heureDebutCalcul.isAfter(heureDepart)) {
        return 0.0;
    }
    
    // Calculer la durée en minutes
    long dureeMinutes = Duration.between(heureDebutCalcul, heureDepart).toMinutes();
    
    return (double) Math.max(0, dureeMinutes);
}

// 3. CALCUL DES HEURES SUPPLÉMENTAIRES
private Double calculerHeuresSupplementairesSelonReglement(
        LocalDateTime heureDepart,
        LocalTime heureSortieNormale) {
    
    if (heureDepart == null || heureSortieNormale == null) {
        return 0.0;
    }
    
    LocalTime heureDepartTime = heureDepart.toLocalTime();
    
    // Si départ <= heure normale de sortie → pas d'heures supplémentaires
    if (!heureDepartTime.isAfter(heureSortieNormale)) {
        return 0.0;
    }
    
    // Calculer les heures supplémentaires en minutes
    long minutesSupplementaires = Duration.between(heureSortieNormale, heureDepartTime)
                                         .toMinutes();
    
    return minutesSupplementaires > 0 ? (double) minutesSupplementaires : 0.0;
}

// 4. DÉDUCTION DE LA PAUSE
private Double deduirePause(
        Double heuresTravailleesBrutes,
        BigDecimal dureePauseMinutes) {
    
    if (heuresTravailleesBrutes == null || heuresTravailleesBrutes <= 0) {
        return 0.0;
    }
    
    // Convertir BigDecimal pause en double
    double pauseMinutes = dureePauseMinutes != null ? 
                         dureePauseMinutes.doubleValue() : 0.0;
    
    if (pauseMinutes <= 0) {
        return heuresTravailleesBrutes;
    }
    
    // Déduire la pause seulement si assez d'heures travaillées
    // Exemple : pause déduite si travail >= 4h (240 min)
    double seuilPause = 240.0;
    
    if (heuresTravailleesBrutes >= seuilPause) {
        double heuresApresPause = heuresTravailleesBrutes - pauseMinutes;
        return heuresApresPause >= 0 ? heuresApresPause : 0.0;
    }
    
    return heuresTravailleesBrutes;
}

// // 5. CALCUL DU SHIFT JOUR/NUIT
// private Boolean calculerShiftJour(LocalDateTime heureArrivee) {
//     if (heureArrivee == null) {
//         return true; // Par défaut shift jour
//     }
    
//     int heure = heureArrivee.getHour();
//     // Shift jour si arrivée entre 5h et 17h
//     return heure >= 5 && heure < 17;
// }

// // 6. VÉRIFICATION JOUR FÉRIÉ
// private Boolean estJourFerie(LocalDate date) {
//     // À implémenter selon votre logique
//     // Exemple simple : vérifier si c'est un jour férié français
//     return false;
// }

// // 7. VÉRIFICATION WEEK-END
// private Boolean estWeekEnd(LocalDate date) {
//     if (date == null) {
//         return false;
//     }
//     DayOfWeek day = date.getDayOfWeek();
//     return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
// }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    // Méthode pour créer les pointages filles
    private List<PointageFille> creerPointagesFilles(Pointage pointageMere, 
                                                    List<PointageImport> pointagesDuJour) {
        
        List<PointageFille> pointagesFilles = new ArrayList<>();
        
        for (int i = 0; i < pointagesDuJour.size(); i++) {
            PointageImport pointageImport = pointagesDuJour.get(i);
            
            PointageFille pointageFille = new PointageFille();
            
            // ID sera généré automatiquement
            pointageFille.setDateHeurePointage(convertirEnLocalDateTime(pointageImport.getAttendance_time()));
            pointageFille.setPointage(pointageMere); // Référence temporaire
            pointageFille.setSource(pointageImport.getMachine_name());
            
            // Déterminer type d'action (IN/OUT alternés)
            String typeAction = determinerTypeAction(i, pointagesDuJour.size());
            pointageFille.setTypeAction(typeAction);
            
            pointagesFilles.add(pointageFille);
        }
        
        return pointagesFilles;
    }

    // Méthode pour déterminer IN/OUT
    private String determinerTypeAction(int index, int total) {
        if (total == 1) {
            return "IN"; // Un seul pointage = considéré comme entrée
        }
        
        if (total % 2 == 0) {
            // Nombre pair: alternance IN/OUT
            return index % 2 == 0 ? "IN" : "OUT";
        } else {
            // Nombre impair: premier IN, dernier OUT, autres alternés
            if (index == 0) return "IN";
            if (index == total - 1) return "OUT";
            return index % 2 == 1 ? "IN" : "OUT"; // Alternance inversée
        }
    }

    // Méthode d'insertion batch
    private void insererEnBatch(List<Pointage> pointagesMeres, 
                            List<PointageFille> pointagesFilles) {
        
        System.out.println("🚀 Insertion batch: " + pointagesMeres.size() + 
                        " pointages mères, " + pointagesFilles.size() + " filles");
        
        try {
            // 1. Insérer les pointages mères
            long start = System.currentTimeMillis();
            List<Pointage> meresInserees = pointageService.createMultiplePointages(pointagesMeres);
            long tempsMeres = System.currentTimeMillis() - start;
            
            System.out.println("✅ " + meresInserees.size() + " pointages mères insérés en " + 
                            tempsMeres + " ms");
            
            // 2. Mettre à jour les références dans les pointages filles
            // Créer une Map pour retrouver rapidement les pointages mères par ID temporaire
            Map<Pointage, Pointage> mapCorrespondance = new HashMap<>();
            for (int i = 0; i < pointagesMeres.size(); i++) {
                mapCorrespondance.put(pointagesMeres.get(i), meresInserees.get(i));
            }
            
            // Mettre à jour les références
            pointagesFilles.forEach(pf -> {
                Pointage mereOriginale = pf.getPointage();
                Pointage mereInseree = mapCorrespondance.get(mereOriginale);
                pf.setPointage(mereInseree);
            });
            
            // 3. Insérer les pointages filles
            start = System.currentTimeMillis();
            List<PointageFille> fillesInserees = 
                pointageFilleService.createMultiplePointageFilles(pointagesFilles);
            long tempsFilles = System.currentTimeMillis() - start;
            
            System.out.println("✅ " + fillesInserees.size() + " pointages filles insérés en " + 
                            tempsFilles + " ms");
            
            System.out.println("🎉 Import terminé avec succès !");
            System.out.println("⏱️  Temps total d'insertion: " + (tempsMeres + tempsFilles) + " ms");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'insertion batch: " + e.getMessage());
            throw new RuntimeException("Échec de l'import dans la base de données", e);
        }
    }

    // Méthodes utilitaires (à implémenter selon votre logique)
    private Boolean estJourFerie(LocalDate date) {
        // À implémenter: vérifier si date est jour férié
        return false;
    }

    private Boolean estWeekEnd(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private Double calculerRetard(LocalDateTime heureArrivee, Employe employe) {
        // À implémenter: comparer avec heure d'arrivée théorique
        return 0.0;
    }

    private Double calculerHeuresSupplementaires(double dureeMinutes, Employe employe) {
        // À implémenter: calcul selon contrat
        return 0.0;
    }

    private Boolean calculerShiftJour(LocalDateTime heureArrivee) {
        // À implémenter: déterminer si shift jour/nuit
        return heureArrivee.getHour() < 12;
    }

    //Mifarana eto //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return datetimeFormat.format(date);
                    }
                    double num = cell.getNumericCellValue();
                    if (num == Math.floor(num)) {
                        return String.valueOf((int) num);
                    }
                    return String.valueOf(num);
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception e) {
                        return cell.getStringCellValue();
                    }
                case BLANK:
                    return "";
                default:
                    return "";
            }
        } catch (Exception e) {
            System.err.println("Erreur lecture cellule: " + e.getMessage());
            return "";
        }
    }


    /**
     * Valide un objet PointageImport et retourne la liste des erreurs
     */
    public List<String> validatePointage(PointageImport pointage) {
        List<String> errors = new ArrayList<>();
        
        if (pointage == null) {
            errors.add("L'objet pointage est null");
            return errors;
        }
        
        // Valider l'ID
        validateId(pointage.getId(), errors);
        
        // Valider le nom
        validateName(pointage.getName(), errors);
        
        // Valider le département
        validateDepartment(pointage.getDept(), errors);
        
        // Valider la date/heure
        validateAttendanceTime(pointage.getAttendance_time(), errors);
        
        // Valider le type de pointage
        // validateAttendanceType(pointage.getAttendance_type(), errors);
        
        
        // Valider le nom de la machine
        validateMachineName(pointage.getMachine_name(), errors);
        
        return errors;
    }

    public Map<String, Object> validatePointagePairs(List<PointageImport> pointages) {
        Map<String, List<PointageImport>> pointagesByUser = new HashMap<>();
        Map<String, Object> result = new LinkedHashMap<>();
        List<String> errors = new ArrayList<>();
        
        // Grouper les pointages par utilisateur (id + name)
        for (PointageImport pointage : pointages) {
            String userKey = pointage.getId() + "_" + pointage.getName();
            pointagesByUser.computeIfAbsent(userKey, k -> new ArrayList<>()).add(pointage);
        }
        
        // Vérifier chaque utilisateur
        for (Map.Entry<String, List<PointageImport>> entry : pointagesByUser.entrySet()) {
            String userKey = entry.getKey();
            List<PointageImport> userPointages = entry.getValue();
            
            // Trier par date/heure
            userPointages.sort(Comparator.comparing(PointageImport::getAttendance_time));
            
            int pointageCount = userPointages.size();
            
            // Vérifier si le nombre est pair
            if (pointageCount % 2 != 0) {
                String[] parts = userKey.split("_");
                String id = parts.length > 0 ? parts[0] : "";
                String name = parts.length > 1 ? parts[1] : "";
                
                String errorMessage = String.format(
                    "L'utilisateur %s (%s) a un nombre impair de pointages (%d). " +
                    "Cela signifie qu'il n'est jamais sorti de l'établissement, " +
                    "ce qui n'est pas logique. Vérifiez les pointages manquants.",
                    name, id, pointageCount
                );
                errors.add(errorMessage);
                
                // Ajouter des détails supplémentaires
                System.out.println("⚠️ Pointages incomplets pour " + name + " (" + id + "):");
                for (int i = 0; i < userPointages.size(); i++) {
                    PointageImport p = userPointages.get(i);
                    System.out.println("  " + (i + 1) + ". " + p.getAttendance_time() + 
                                    " - " + p.getAttendance_type());
                }
            }
        }
        
        // Préparer le résultat
        result.put("totalUsers", pointagesByUser.size());
        result.put("usersWithErrors", errors.size());
        result.put("errors", errors);
        result.put("isValid", errors.isEmpty());
        
        // Statistiques
        int totalPointages = pointages.size();
        int validUsers = pointagesByUser.size() - errors.size();
        result.put("totalPointages", totalPointages);
        result.put("validUsers", validUsers);
        
        return result;
    }
    
    /**
     * Valide une liste de PointageImport et retourne les résultats
     */
    public ValidationResult validatePointages(List<PointageImport> pointages) {
    ValidationResult result = new ValidationResult();

    if (pointages == null || pointages.isEmpty()) {
        result.addGlobalError("La liste des pointages est vide");
        return result;
    }
    
    // Récupérer tous les matricules actifs
    List<InfosProfessionnelles> lesInfosProEmploye = infosProfessionnellesService.getAllInfoProActif();
    
        // Créer une Map pour recherche rapide (matricule -> InfosProfessionnelles)
        Map<String, InfosProfessionnelles> employesParMatricule = new HashMap<>();
        for (InfosProfessionnelles infoPro : lesInfosProEmploye) {
            if (infoPro.getMatricule() != null) {
                String matriculeStr = infoPro.getMatricule().toString();
                employesParMatricule.put(matriculeStr, infoPro);
            }
        }
        
        System.out.println("=== VALIDATION DE " + pointages.size() + " POINTAGES ===");
        System.out.println("Nombre d'employés actifs en base: " + lesInfosProEmploye.size());
        
        for (int i = 0; i < pointages.size(); i++) {
            PointageImport pointage = pointages.get(i);
            int rowNumber = i + 1;
            
            List<String> errors = validatePointage(pointage);
            
            // Vérification matricule et nom
            String matricule = String.valueOf(pointage.getId());
            String nomCompletPointage = pointage.getName() != null ? 
                                    pointage.getName().trim() : "";
            
            if (matricule != null && !matricule.trim().isEmpty() && !matricule.equals("null")) {
                matricule = matricule.trim();
                
                // 1. Vérifier si le matricule existe
                if (!employesParMatricule.containsKey(matricule)) {
                    errors.add("Matricule '" + matricule + "' non trouvé parmi les employés actifs");
                } 
                // 2. Vérifier que le nom correspond au matricule
                else {
                    InfosProfessionnelles employe = employesParMatricule.get(matricule);
                    
                    // Construire le nom complet de l'employé en base
                    String nomCompletBase = "";
                    if (employe.getEmploye().getPrenom() != null && employe.getEmploye().getNom() != null) {
                        nomCompletBase = (employe.getEmploye().getNom() + " " + employe.getEmploye().getPrenom()).trim();
                    } else if (employe.getEmploye().getNom() != null) {
                        nomCompletBase = employe.getEmploye().getNom().trim();
                    }
                    
                    // Vérifier la correspondance
                    if (!nomCompletPointage.isEmpty() && !nomCompletBase.isEmpty()) {
                        // Normaliser pour comparaison (minuscules, espaces)
                        String nomPointageNormalise = nomCompletPointage.toLowerCase()
                            .replaceAll("\\s+", " ").trim();
                        String nomBaseNormalise = nomCompletBase.toLowerCase()
                            .replaceAll("\\s+", " ").trim();
                        
                        if (!nomPointageNormalise.equals(nomBaseNormalise)) {
                            errors.add("Incohérence: Le matricule '" + matricule + 
                                    "' correspond à '" + nomCompletBase + 
                                    "' mais vous avez saisi '" + nomCompletPointage + "'");
                        }
                    } else if (nomCompletPointage.isEmpty()) {
                        errors.add("Nom manquant pour le matricule '" + matricule + "'");
                    }
                }
            } else {
                errors.add("Matricule manquant ou invalide: '" + matricule + "'");
            }
            
            if (errors.isEmpty()) {
                result.addValidPointage(pointage);
                System.out.println("Ligne " + rowNumber + " ✅ Valide - " + 
                                nomCompletPointage + " (" + matricule + ")");
            } else {
                result.addErrorPointage(pointage, rowNumber, errors);
                System.out.println("Ligne " + rowNumber + " ❌ Erreurs: " + errors.size());
            }
        }
        
        // Calculer les statistiques
        result.calculateStatistics();
        
        System.out.println("\n=== RÉSUMÉ VALIDATION ===");
        System.out.println("Total pointages: " + result.getTotalCount());
        System.out.println("Valides: " + result.getValidCount());
        System.out.println("Erreurs: " + result.getErrorCount());
        
        return result;
    }
    
    /**
     * Valide un ID
     */
    private void validateId(String id, List<String> errors) {
        if (id == null || id.trim().isEmpty()) {
            errors.add(ERR_ID_REQUIRED);
        }
    }
    
    /**
     * Valide un ID à partir d'une chaîne
     */
    public List<String> validateId(String idStr) {
        List<String> errors = new ArrayList<>();
        
        if (idStr == null || idStr.trim().isEmpty()) {
            errors.add(ERR_ID_REQUIRED);
            return errors;
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            if (id <= 0) {
                errors.add(ERR_ID_POSITIVE);
            }
        } catch (NumberFormatException e) {
            errors.add(ERR_ID_FORMAT + ": " + idStr);
        }
        
        return errors;
    }
    
    /**
     * Valide un nom
     */
    private void validateName(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(ERR_NAME_REQUIRED);
            return;
        }
        
        String trimmedName = name.trim();
        if (trimmedName.length() < 2 || trimmedName.length() > 100) {
            errors.add(ERR_NAME_LENGTH);
        }
    }
    
    /**
     * Valide un département
     */
    private void validateDepartment(String dept, List<String> errors) {
        if (dept == null) {
            return; // Optionnel
        }
        
        String trimmedDept = dept.trim();
        if (!trimmedDept.isEmpty() && trimmedDept.length() > 50) {
            errors.add(ERR_DEPT_LENGTH);
        }
    }
    
    /**
     * Valide une date/heure
     */
    private void validateAttendanceTime(String timeStr, List<String> errors) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            errors.add(ERR_TIME_REQUIRED);
            return;
        }
        
        LocalDateTime time = null;
        String trimmedTime = timeStr.trim();
        
        try {
            // Essayer le format ISO d'abord : "2022-11-25 11:50:07"
            if (trimmedTime.contains("-")) {
                try {
                    // Avec secondes
                    DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time = LocalDateTime.parse(trimmedTime, isoFormatter);
                } catch (DateTimeParseException e1) {
                    // Sans secondes
                    try {
                        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        time = LocalDateTime.parse(trimmedTime, isoFormatter);
                    } catch (DateTimeParseException e2) {
                        // Format français : "25/11/2022 11:50"
                        DateTimeFormatter frenchFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        time = LocalDateTime.parse(trimmedTime, frenchFormatter);
                    }
                }
            } 
            // Format français : "25/11/2022 11:50"
            else if (trimmedTime.contains("/")) {
                DateTimeFormatter frenchFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                time = LocalDateTime.parse(trimmedTime, frenchFormatter);
            }
            else {
                errors.add("Format de date non reconnu: '" + trimmedTime + "'");
                return;
            }
            
        } catch (DateTimeParseException e) {
            errors.add("Impossible de parser la date: '" + trimmedTime + 
                    "'. Formats acceptés:\n" +
                    "- Format français: JJ/MM/AAAA HH:MM (ex: 25/11/2022 11:50)\n" +
                    "- Format ISO: AAAA-MM-JJ HH:MM:SS (ex: 2022-11-25 11:50:07)");
            return;
        }
        
        // Validation des dates (même code que avant)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        
        if (time.isAfter(now)) {
            errors.add(ERR_TIME_FUTURE);
        }
        
        if (time.isBefore(minDate)) {
            errors.add(ERR_TIME_PAST);
        }
    }
    
    /**
     * Valide une date/heure à partir d'une chaîne
     */
    public List<String> validateAttendanceTime(String timeStr) {
        List<String> errors = new ArrayList<>();
        
        if (timeStr == null || timeStr.trim().isEmpty()) {
            errors.add(ERR_TIME_REQUIRED);
            return errors;
        }
        
        try {
            // LocalDateTime time = parseDateTime(timeStr.trim());
            if (timeStr == null) {
                errors.add(ERR_TIME_FORMAT + ": " + timeStr);
            } else {
                validateAttendanceTime(timeStr, errors);
            }
        } catch (Exception e) {
            errors.add(ERR_TIME_FORMAT + ": " + e.getMessage());
        }
        
        return errors;
    }
    
    /**
     * Valide un type de pointage
     */
    private void validateAttendanceType(String type, List<String> errors) {
        if (type == null || type.trim().isEmpty()) {
            errors.add(ERR_TYPE_REQUIRED);
            return;
        }
        
        String trimmedType = type.trim();
        if (!isValidAttendanceType(trimmedType)) {
            errors.add(ERR_TYPE_INVALID);
        }
    }
    
    /**
     * Valide un nom de machine
     */
    private void validateMachineName(String machineName, List<String> errors) {
        if (machineName == null) {
            return; // Optionnel
        }
        
        String trimmedMachine = machineName.trim();
        if (!trimmedMachine.isEmpty() && trimmedMachine.length() > 50) {
            errors.add(ERR_MACHINE_LENGTH);
        }
    }
    
    /**
     * Vérifie si un type de pointage est valide
     */
    private boolean isValidAttendanceType(String type) {
        String[] validTypes = {"Check-In", "Check-Out", "Entrée", "Sortie", 
                              "CHECK-IN", "CHECK-OUT", "ENTREE", "SORTIE",
                              "check-in", "check-out", "entrée", "sortie"};
        
        for (String validType : validTypes) {
            if (validType.equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Parse une date/heure depuis une chaîne
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
        };
        
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        
        return null;
    }
}