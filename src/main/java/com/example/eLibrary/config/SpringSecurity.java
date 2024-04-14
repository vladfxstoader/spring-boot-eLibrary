package com.example.eLibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**", "/", "/forgot-password/**", "/access_denied").permitAll()
                        .requestMatchers("/authors/**", "/add-author/**", "/add-publisher/**", "/publishers",
                                "/categories", "/add-category/**", "/delete-author/**", "/delete-category/**",
                                "/delete-publisher/**", "/books", "/add-book/**", "/delete-book/**", "/loans",
                                "/accept-loan/**", "/decline-loan/**", "/edit-category/**", "/edit-author/**",
                                "/edit-book/**", "/edit-publisher/**").hasRole("LIBRARIAN")
                        .requestMatchers("/users", "/decline-user/**", "/accept-user/**", "/change-user-role/**",
                                "/add-role/**").hasRole("ADMIN")
                        .requestMatchers("/add-loan/**", "/return-loan/**", "/loans-user", "/loans-user-exp-date",
                                "/search-book", "/search-book/**", "/display-search-book").authenticated()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler((request, response, authentication) -> {
                                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                                        if (auth.getAuthority().equals("ROLE_ADMIN")) {
                                            response.sendRedirect("/users"); // admin dashboard
                                            return;
                                        } else if (auth.getAuthority().equals("ROLE_LIBRARIAN")) {
                                            response.sendRedirect("/loans"); //librarian dashboard
                                            return;
                                        }
                                    }
                                    response.sendRedirect("/add-loan"); //user dashboard
                                })
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/access_denied");
                        })
                )                .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
