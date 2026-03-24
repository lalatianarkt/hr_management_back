package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Token;
import com.rh.manage.Model.User;

import java.lang.StackWalker.Option;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    // Trouver un token par sa valeur
    Optional<Token> findByTokenGenere(String tokenGenere);

    // Méthode 3: Trouver par tokenGenere et isActive
    Optional<Token> findByTokenGenereAndIsActive(String tokenGenere, Integer isActive);

    // Vérifier si un token actif existe pour un utilisateur
    Optional<Token> findByUserAndIsActive(User user, Integer isActive);

    @Query("SELECT t FROM Token t WHERE t.user = :user and t.isActive = 1 ORDER BY t.createdAt DESC LIMIT 1")
    Optional<Token> findLastTokenByUserAndIsActive(User user);

    @Query("SELECT t FROM Token t WHERE t.user = :user ORDER BY t.createdAt DESC LIMIT 1")
    Optional<Token> findLastTokenByUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE Token t SET t.tokenGenere = :tokenGenere, t.isActive = :isActive WHERE t.id = :id")
    void updateToken(String tokenGenere, int isActive, String id);
    
}
