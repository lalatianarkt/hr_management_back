// package com.rh.manage;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityDisabledConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf().disable()        // désactive CSRF
//             .authorizeHttpRequests(authorize -> authorize
//                 .anyRequest().permitAll() // autorise toutes les requêtes
//             )
//             .httpBasic().disable();   // désactive httpBasic
//         return http.build();
//     }
// }

