package com.rh.manage.Utils;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rh.manage.Service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        
        // Ignorer les endpoints publics
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.validateToken(token);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    List.of(new SimpleGrantedAuthority(
                        "ROLE_" + claims.get("role", String.class)
                    ))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                "Token expiré", "EXPIRED_TOKEN");
            
        } catch (SignatureException | MalformedJwtException e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                "Token invalide", "INVALID_TOKEN");
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 
                "Erreur d'authentification", "AUTH_ERROR");
        }
    }
    
    private boolean isPublicEndpoint(String path) {
        // Liste des endpoints publics
        return path.startsWith("/api/auth/") ||
               path.equals("/api/users/auth") ||
               path.equals("/api/users/register") ||
               path.equals("/api/users/all") ||
               path.startsWith("/api/public/");
    }
    
    private void sendErrorResponse(HttpServletResponse response, int status, 
                                   String message, String error) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        
        response.setStatus(status);
        response.setContentType("application/json");
        
        String jsonResponse = String.format(
            "{\"status\": %d, \"message\": \"%s\", \"error\": \"%s\"}", 
            status, message, error
        );
        response.getOutputStream().write(jsonResponse.getBytes());
        response.getOutputStream().flush();
    }
} 

