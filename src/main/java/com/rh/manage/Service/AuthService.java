package com.rh.manage.Service;

import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;
import com.rh.manage.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired 
    private UserService userService;


    /**
     * Vérifie les identifiants et génère un token si valides.
     *
     * @param email    Email de l'utilisateur
     * @param password Mot de passe saisi
     * @return Le token généré si succès, sinon null
     */
    // public Token log(String email, String password) {
    //     // 1️⃣ Rechercher l'utilisateur
    //     Optional<User> optionalUser = userRepository.findByEmail(email);

    //     if (optionalUser.isEmpty()) {
    //         System.out.println("⚠️ Aucun utilisateur trouvé avec cet email !");
    //         return null;
    //     }

    //     User user = optionalUser.get();

    //     // 2️⃣ Vérifier le mot de passe (comparaison avec celui haché en base)
    //     if (!BCrypt.checkpw(password, user.getPassword())) {
    //         System.out.println("❌ Mot de passe incorrect !");
    //         return null;
    //     }

    //     // 3️⃣ Générer un token
    //     Token token = tokenService.generateToken(user, "AUTH");
    //     System.out.println("✅ Connexion réussie, token généré : " + token.getTokenGenere());

    //     return token;
    // } 

    // public log(User user){

    // }



}
