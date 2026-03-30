
package com.rh.manage.Service;

import com.rh.manage.Dto.UserListDTO;
import com.rh.manage.Dto.UserRequest;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Repository.UserRepository;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private InfosProfessionnellesService infosProfessionnellesService;

    @Autowired 
    private EmployeService employeService;

    @Autowired 
    private TypeUserService typeUserService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRoleService roleUserService;
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByEmployeId(String employeId) {
        return userRepository.findByEmployeIdAndStatut(employeId, 1);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getById(String id){
        return userRepository.findById(id);
    }

    // public List<UserListDTO> getAllUsersForAdminList() {
    //     List<User> users = userRepository.findAllWithEmployeAndTypeUser();
    //     List<UserListDTO> result = new ArrayList<>();

    //     for (User user : users) {
    //         Employe employe = user.getEmploye();
    //         InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
    //         String departement = "Non assigné";
    //         String role = "Non défini";

    //         TypeUser typeUser = user.getTypeUser();
    //         if (typeUser == null) {
    //             typeUser = userRoleService.getPrimaryRoleForUser(user.getId());
    //         }
    //         if (typeUser != null) {
    //             role = typeUser.getType();
    //         }

    //         if (infosPro != null && infosPro.getDepartement() != null) {
    //             departement = infosPro.getDepartement().getNom();
    //         }

    //         result.add(new UserListDTO(
    //             user.getId(),
    //             employe.getNom(),
    //             employe.getPrenom(),
    //             role,
    //             departement,
    //             user.getStatut()
    //         ));
    //     }

    //     return result;
    // }

    // public User updateUserStatut(String userId, Integer statut) {
    //     if (statut == null || (statut != 1 && statut != 2)) {
    //         throw new IllegalArgumentException("Statut invalide. Utilisez 1 pour accepter ou 2 pour refuser.");
    //     }

    //     User user = userRepository.findById(userId)
    //         .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

    //     TypeUser typeUser = user.getTypeUser();
    //     if (typeUser == null) {
    //         typeUser = userRoleService.getPrimaryRoleForUser(user.getId());
    //     }
    //     if (typeUser == null || !"Admin".equalsIgnoreCase(typeUser.getType())) {
    //         throw new IllegalArgumentException("Cette action est réservée aux utilisateurs de type Admin.");
    //     }

    //     if (user.getStatut() == null || user.getStatut() != 0) {
    //         throw new IllegalArgumentException("Seuls les utilisateurs Admin en attente peuvent être acceptés ou refusés.");
    //     }

    //     user.setStatut(statut);
    //     user.setModifiedAt(LocalDateTime.now());
    //     return userRepository.save(user);
    // }

    // public Map<String, Object> authenticateUser(UserRequest userRequest) throws Exception, AuthenticationException, ResourceNotFoundException {
    //     User user = authenticate(userRequest.getUser());
    //     if (user == null) {
    //         throw new AuthenticationException("Identifiants invalides");
    //     }
        
    //     Employe employe = employeService.getById(user.getEmploye().getId())
    //         .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

    //     InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
    //     String jwt = jwtService.generateToken(user, infosPro);
    //     Token token = tokenService.generateToken(user, "AUTH", jwt);
    //     String path = null;
    //     List<UserRole> liste_user_role = roleUserService.getTypeUserByIdUserAndStatut(user.getId(), 0);
    //     if(user.getTypeUser()!= null){
    //         path = determineRedirectPath(user.getTypeUser().getType());
    //     } else {
    //         path = determineRedirectPath(liste_user_role.get(0).getTypeUser().getType());
    //     }
        
    //     Map<String, Object> response = new HashMap<>();
    //     response.put("status", 200);
    //     response.put("message", "Authentification réussie");
    //     response.put("token", token.getTokenGenere());
    //     response.put("infosPro", infosPro);
    //     response.put("user", buildUserResponse(user, employe, user.getTypeUser().getType()));
    //     response.put("path", path);
    //     return response;
    // }
    
    // private String determineRedirectPath(String userType) {
    //     switch (userType) {
    //         case "Admin":
    //             return "/dashboard-RH/";
    //         case "Manager":
    //             return "/dashboard-Manager/";
    //         case "Employe":
    //             return "/emp/infos/fiche-perso";
    //         case "IT":
    //             return "/dashboard-IT/";
    //         default:
    //             return "/";
    //     }
    // }

    // public User authenticate(User userRequest) throws Exception {
    //     Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
    //     User utilisateur = null;
    
    //     if (optionalUser.isEmpty()) {
    //         throw new Exception("Aucun utilisateur trouvé avec cet email !");
    //     } else {
    //         utilisateur = optionalUser.get();
    //     }

    //     if(optionalUser.get().getStatut() != 1){
    //         throw new Exception("Votre compte est en cours de validation");
    //     }

    //     if (!BCrypt.checkpw(userRequest.getPassword(), utilisateur.getPassword())) {
    //         throw new Exception("Mot de passe incorrect !");
    //     }

    //     employeService.getById(userRequest.getEmploye().getId())
    //         .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

    //     List<UserRole> typeUsers = roleUserService.getTypeUserByIdUserAndStatut(optionalUser.get().getId(), 0);
    //     if(typeUsers.size() > 0){
    //         utilisateur.setTypeUser(typeUsers.get(0).getTypeUser());
    //     } else {
    //         UserRequest userReq = new UserRequest();
    //         userReq.setUser(userRequest);
    //         userReq.setTypeUser(null);
    //         authenticateUser(userReq);
    //     }
    //     return utilisateur;
    // } 

    // public Map<String, Object> authenticateUser(UserRequest userRequest) throws Exception, AuthenticationException, ResourceNotFoundException {
    //     User user = authenticate(userRequest.getUser());
    //     System.out.println("idUser : " + user.getId());
    //     if (user == null) {
    //         throw new AuthenticationException("Identifiants invalides");
    //     }
        
    //     Employe employe = employeService.getById(user.getEmploye().getId())
    //         .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

    //     InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        
    //     // Récupérer tous les rôles actifs de l'utilisateur
    //     List<UserRole> listeUserRoles = roleUserService.getUserRolesByUserIdAndStatut(user.getId(), 1);
    //     System.out.println("listeUserRoles size++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ : " + listeUserRoles.size());
        
    //     if (listeUserRoles.isEmpty()) {
    //         throw new ResourceNotFoundException("Aucun rôle trouvé pour cet utilisateur");
    //     }
        
    //     String jwt = jwtService.generateToken(user, userRequest.getTypeUser() != null ? userRequest.getTypeUser() : listeUserRoles.get(0).getTypeUser().getType());
    //     Token token = tokenService.generateToken(user, "AUTH", jwt);
        
    //     Map<String, Object> response = new HashMap<>();
    //     response.put("status", 200);
    //     response.put("message", "Authentification réussie");
    //     response.put("token", token.getTokenGenere());
    //     response.put("infosPro", infosPro);
        
    //     // Si l'utilisateur a plusieurs rôles, on lui envoie la liste des rôles
    //     if (listeUserRoles.size() > 1) {
    //         List<Map<String, Object>> rolesList = new ArrayList<>();
    //         for (UserRole userRole : listeUserRoles) {
    //             Map<String, Object> roleInfo = new HashMap<>();
    //             roleInfo.put("id", userRole.getUser().getId());
    //             roleInfo.put("type", userRole.getTypeUser().getType());
    //             roleInfo.put("libelle", getRoleLibelle(userRole.getTypeUser().getType()));
    //             roleInfo.put("path", determineRedirectPath(userRole.getTypeUser().getType()));
    //             response.put("user", buildUserResponse(user, employe, userRole.getTypeUser().getType()));
    //             rolesList.add(roleInfo);
    //         }
            
    //         response.put("hasMultipleRoles", true);
    //         response.put("roles", rolesList);
    //         response.put("needsRoleSelection", true);
    //         response.put("message", "Plusieurs rôles détectés. Veuillez choisir un rôle.");
            
    //     } else {
    //         // Un seul rôle, redirection directe
    //         TypeUser typeUser = listeUserRoles.get(0).getTypeUser();
    //         String path = determineRedirectPath(typeUser.getType());
            
    //         response.put("hasMultipleRoles", false);
    //         response.put("path", path);
    //         response.put("role", typeUser.getType());
    //         response.put("message", "Authentification réussie");
    //     }
    //     return response;
    // }

    public Map<String, Object> authenticateUser(UserRequest userRequest) throws Exception, AuthenticationException, ResourceNotFoundException {
        User user = authenticate(userRequest.getUser());
        System.out.println("idUser : " + user.getId());
        
        if (user == null) {
            throw new AuthenticationException("Identifiants invalides");
        }
        
        Employe employe = employeService.getById(user.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        
        // Récupérer tous les rôles actifs de l'utilisateur
        List<UserRole> listeUserRoles = roleUserService.getUserRolesByUserIdAndStatut(user.getId(), 1);
        System.out.println("Nombre de rôles trouvés : " + listeUserRoles.size());
        
        if (listeUserRoles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun rôle actif trouvé pour cet utilisateur");
        }
        
        // Déterminer le rôle par défaut (le premier de la liste)
        String defaultRole = listeUserRoles.get(0).getTypeUser().getType();
        
        // Générer le token avec le rôle par défaut
        String jwt = jwtService.generateToken(user, defaultRole);
        Token token = tokenService.generateToken(user, "AUTH", jwt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Authentification réussie");
        response.put("token", token.getTokenGenere());
        response.put("infosPro", infosPro);
        response.put("user", buildUserResponse(user, employe, defaultRole));
        
        // Toujours envoyer la liste des rôles disponibles
        List<Map<String, Object>> rolesList = new ArrayList<>();
        for (UserRole userRole : listeUserRoles) {
            Map<String, Object> roleInfo = new HashMap<>();
            roleInfo.put("id", userRole.getTypeUser().getId());
            roleInfo.put("type", userRole.getTypeUser().getType());
            roleInfo.put("libelle", getRoleLibelle(userRole.getTypeUser().getType()));
            roleInfo.put("path", determineRedirectPath(userRole.getTypeUser().getType()));
            rolesList.add(roleInfo);
        }
        
        response.put("hasMultipleRoles", listeUserRoles.size() > 1);
        response.put("roles", rolesList);
        response.put("currentRole", defaultRole);
        response.put("path", determineRedirectPath(defaultRole));
        
        return response;
    }

    public User authenticate(User userRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        User utilisateur = null;

        if (optionalUser.isEmpty()) {
            throw new Exception("Aucun utilisateur trouvé avec cet email !");
        } else {
            utilisateur = optionalUser.get();
        }

        if (utilisateur.getStatut() != 1) { 
            throw new Exception("Votre compte est en cours de validation");
        }       
        
        if (!BCrypt.checkpw(userRequest.getPassword(), utilisateur.getPassword())) {
            throw new Exception("Mot de passe incorrect !");
        }

        employeService.getById(utilisateur.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        List<UserRole> typeUsers = roleUserService.getUserRolesByUserIdAndStatut(utilisateur.getId(), 1);
        
        if (!typeUsers.isEmpty()) {
            utilisateur.setUserRoles(typeUsers);
        } else {
            throw new ResourceNotFoundException("Aucun rôle actif trouvé pour cet utilisateur");
        }
        
        return utilisateur;
    } 

    // Méthode pour sélectionner un rôle après authentification
    public Map<String, Object> selectUserRole(String userId, String selectedRoleType) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        
        // Vérifier que l'utilisateur a bien ce rôle
        List<UserRole> userRoles = roleUserService.getUserRolesByUserIdAndStatut(userId, 0);
        boolean hasRole = userRoles.stream()
            .anyMatch(ur -> ur.getTypeUser().getType().equals(selectedRoleType));
        
        if (!hasRole) {
            throw new Exception("L'utilisateur n'a pas le rôle sélectionné");
        }
        
        // Récupérer les informations de l'employé
        Employe employe = employeService.getById(user.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));
        
        InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        
        // Générer un nouveau token avec le rôle sélectionné
        String jwt = jwtService.generateToken(user, selectedRoleType);
        Token token = tokenService.generateToken(user, "AUTH", jwt);
        
        String path = determineRedirectPath(selectedRoleType);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Rôle sélectionné avec succès");
        response.put("token", token.getTokenGenere());
        response.put("infosPro", infosPro);
        response.put("user", buildUserResponse(user, employe, selectedRoleType));
        response.put("path", path);
        response.put("role", selectedRoleType);
        
        return response;
    }                       

    // Méthode utilitaire pour obtenir le libellé du rôle
    private String getRoleLibelle(String roleType) {
        switch (roleType) {
            case "Admin":
                return "Administrateur RH";
            case "Manager":
                return "Manager";
            case "Employe":
                return "Employé";
            case "IT":
                return "Administrateur IT";
            default:
                return roleType;
        }
    }

    public String determineRedirectPath(String userType) {
        switch (userType) {
            case "Admin_RH":
                return "/dashboard-RH/";
            case "Manager":
                return "/dashboard-Manager/";
            case "Employe":
                return "/emp/infos/fiche-perso";
            case "IT":
                return "/dashboard-IT/";
            default:
                return "/";
        }
    }
    
    private Map<String, Object> buildUserResponse(User user, Employe employe, String role) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("email", user.getEmail());
        userResponse.put("role", role);
        userResponse.put("nomComplet", employe.getPrenom() + " " + employe.getNom());
        return userResponse;
    }
    
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
    
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Transactional
    public void validateCountByEmail(String email, String tokenInput) throws Exception {
        Optional<User> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new Exception("Utilisateur non trouvé");
        }

        User user = userOpt.get();
        Token token = tokenService.getDernierTokenActiveParUser(user);

        if (token == null) {
            throw new Exception("Aucun token trouvé pour cet utilisateur");
        }

        LocalDateTime now = LocalDateTime.now();
        System.out.println("now amfiry : " + now);
        System.out.println("expiredAt : " + token.getExpiresAt());
        System.out.println("Aorès ve : " + now.isAfter(token.getExpiresAt()));
        
        if (now.isAfter(token.getExpiresAt())) {
            throw new Exception("Le token a expiré. Veuillez renvoyer un nouveau token.");
        }

        if (!token.getTokenGenere().equals(tokenInput)) {
            throw new Exception("Token invalide. Veuillez vérifier votre email.");
        }

        token.setIsActive(0); 
        tokenService.update(token);

        // Récupération des UserRole pour cet utilisateur
        List<UserRole> userRoles = userRoleService.getByUserId(user.getId());
        
        // Vérification si l'utilisateur a des rôles
        if (userRoles == null || userRoles.isEmpty()) {
            throw new Exception("Aucun rôle trouvé pour cet utilisateur");
        }
        
        // // Désactivation de tous les UserRole
        // for (UserRole userRole : userRoles) {
        //     userRole.setStatut(0); 
        //     userRoleService.update(userRole);
        // }
        
        // Récupération du type d'utilisateur principal (ex: le premier rôle actif ou un rôle spécifique)
        TypeUser typeUser = null;
        
        // Essayons d'abord de récupérer via user.getTypeUser() si cette relation existe
        // Sinon, on récupère le premier rôle de la liste
        if (userRoles != null && !userRoles.isEmpty()) {
            // Vous pouvez choisir un rôle spécifique, par exemple le premier
            typeUser = userRoles.get(0).getTypeUser();
            
            // Ou si vous avez une méthode pour récupérer le rôle principal
            // typeUser = userRoleService.getPrimaryRoleForUser(user.getId());
        }
        
        // Définition du statut de l'utilisateur en fonction de son rôle
        if (typeUser != null && "Admin".equalsIgnoreCase(typeUser.getType())) {
            user.setStatut(2); // Statut pour Admin
        } else {
            user.setStatut(1); // Statut pour les autres utilisateurs
        }
        
        user.setModifiedAt(LocalDateTime.now());
        save(user);
    }


    @Transactional
    public void register(User user, String matricule) throws Exception{
        System.out.println("eto 0");
        LocalDateTime now = LocalDateTime.now();

        Optional<User> existingUser = findByEmail(user.getEmail());
        if (!existingUser.isEmpty()) {
            System.out.println("ato ve ????");
            throw new Exception("Email déja existant. Veuillez utiliser un autre compte");
        }

        User userRegistered = registerUser(user);
        String tokenValue = emailService.sendTokenEmail(user.getEmail());
        System.out.println("token : " + tokenValue);
        System.out.println("eto 2");
        Token tok = new Token();
        tok.setCreatedAt(now);
        tok.setExpiresAt(now.plusSeconds(120)); 
        tok.setIsActive(1);
        tok.setTokenGenere(tokenValue);
        tok.setType("inscription");
        tok.setUser(userRegistered);
        tokenService.save(tok);
    }

    public UserRole registerUserRole(UserRole userRole) {
        return userRoleService.create(userRole);
    }

    public User registerUser(User user) {
        // Validation de l'email
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("L'email ne peut pas être vide");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(!user.getEmail().matches(emailRegex)) {
            throw new RuntimeException("L'email n'a pas un format valide");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        // Vérification de l'existence de l'employé
        Optional<Employe> employeOpt = employeService.getByEmail(user.getEmail());
        if (employeOpt.isEmpty()) {
            throw new RuntimeException("Aucun employé trouvé avec cet email: " + user.getEmail());
        }
        Employe employe = employeOpt.get();
        user.setEmploye(employe);

        // Vérification des informations professionnelles
        Optional<InfosProfessionnelles> infosProOpt = infosProfessionnellesService
                .getByIdEmploye(employe.getId());
        if(infosProOpt.isEmpty()) {
            throw new RuntimeException("Le matricule n'existe pas. Veuillez mettre le bon matricule");
        }
        
        // Vérification qu'un utilisateur n'est pas déjà associé à cet employé
        if(userRepository.existsByEmploye_Id(employe.getId())) {
            throw new RuntimeException("Un utilisateur est déjà associé à cet employé");
        }
        
        // Détermination du type d'utilisateur (rôle)
        TypeUser typeUser = determineUserRole(employe, user);
        
        // Hashage du mot de passe
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        // Sauvegarde de l'utilisateur
        User savedUser = userRepository.save(user);
        
        // Création du UserRole (liaison entre l'utilisateur et le type)
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setTypeUser(typeUser);
        userRole.setStatut(1); // 1 = actif
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleService.create(userRole);
        
        return savedUser;
    }

    // Méthode pour déterminer le rôle de l'utilisateur
    private TypeUser determineUserRole(Employe employe, User user) {
        // Vérifier d'abord si l'employé est manager
        if (managerService.isManager(employe)) {
            return typeUserService.getByType("Manager");
        } 
        // Ensuite vérifier si un rôle a été spécifié dans l'objet user (pour Admin ou IT)
        else if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            // Récupérer le type depuis les userRoles si disponibles
            TypeUser specifiedType = user.getUserRoles().get(0).getTypeUser();
            if (specifiedType != null) {
                String typeName = specifiedType.getType();
                if ("Admin".equals(typeName)) {
                    return typeUserService.getByType("Admin");
                } else if ("IT".equals(typeName)) {
                    return typeUserService.getByType("IT");
                }
            }
        }
        
        // Par défaut, rôle Employe
        return typeUserService.getByType("Employe");
    }
    
}
