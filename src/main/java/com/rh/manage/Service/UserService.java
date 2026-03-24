package com.rh.manage.Service;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;
import com.rh.manage.Repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    private TokenService tokenService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByEmployeId(String employeId) {
        return userRepository.findByEmployeId(employeId);
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

    public Map<String, Object> authenticateUser(User userRequest) throws Exception, AuthenticationException, ResourceNotFoundException {
        User user = authenticate(userRequest);
        if (user == null) {
            throw new AuthenticationException("Identifiants invalides");
        }
        
        Employe employe = employeService.getById(user.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        String jwt = jwtService.generateToken(user);
        Token token = tokenService.generateToken(user, "AUTH", jwt);
        
        String path = determineRedirectPath(user.getTypeUser().getType());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Authentification réussie");
        response.put("token", token.getTokenGenere());
        response.put("infosPro", infosPro);
        response.put("user", buildUserResponse(user, employe));
        response.put("path", path);
        return response;
    }
    
    private String determineRedirectPath(String userType) {
        switch (userType) {
            case "Admin":
                return "/dashboard-RH/";
            case "Manager":
                return "/dashboard-Manager/";
            case "Employe":
                return "/emp/";
            default:
                return "/";
        }
    }
    
    private Map<String, Object> buildUserResponse(User user, Employe employe) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("email", user.getEmail());
        userResponse.put("role", user.getTypeUser().getType());
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

    public User authenticate(User userRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail().toLowerCase());
        User utilisateur = null;
    
        if (optionalUser.isEmpty()) {
            throw new Exception("Aucun utilisateur trouvé avec cet email !");
        } else {
            utilisateur = optionalUser.get();
        }

        if(optionalUser.get().getStatut() != 1){
            throw new Exception("Votre compte est en cours de validation");
        }

        if (!BCrypt.checkpw(userRequest.getPassword(), utilisateur.getPassword())) {
            throw new Exception("Mot de passe incorrect !");
        }
        return utilisateur;
    }

    @Transactional
    public void validateCountByEmail(String email, String tokenInput) throws Exception{
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

            if(user.getTypeUser().getType().equalsIgnoreCase("Admin")){
                user.setStatut(2);
            } else{
                user.setStatut(1);
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

        User userRegistered = registerUser(user, matricule);
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


    public User registerUser(User user, String matricule) {
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
        
        Optional<Employe> employeOpt = employeService.getByEmail(user.getEmail());
        if (employeOpt.isEmpty()) {
            throw new RuntimeException("Aucun employé trouvé avec cet email: " + user.getEmail());
        }
        Employe employe = employeOpt.get();

        user.setEmploye(employe);

        TypeUser typeUser;

        if (managerService.isManager(employe)) {
            typeUser = typeUserService.getByType("Manager");
        } 
        else if (user.getTypeUser() != null && "Admin".equals(user.getTypeUser().getType())) {
            typeUser = typeUserService.getByType("Admin");
        } 
        else {
            typeUser = typeUserService.getByType("Employe");
        }

        user.setTypeUser(typeUser);

        Optional<InfosProfessionnelles> infosProOpt = infosProfessionnellesService
                .findByMatricule(matricule);
        if(infosProOpt.isEmpty()) {
            throw new RuntimeException("Le matricule n'existe pas. Veuillez mettre le bon matricule");
        }
        else{
            if(userRepository.existsByEmploye_Id(infosProOpt.get().getEmploye().getId())) {
                throw new RuntimeException("Un utilisateur est déjà associé à ce matricule");
            }
        }
        
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }




    

    
}