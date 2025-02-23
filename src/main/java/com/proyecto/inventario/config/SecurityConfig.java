package com.proyecto.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/", "/login", "/registro").permitAll()
	            .requestMatchers("/css/**", "/js/**", "/assets/**").permitAll() // Permitir acceso a estáticos
	            .requestMatchers("/administrador/**").hasAuthority("ADMIN")
	            .requestMatchers("/empleador/**").hasAuthority("EMPLEADOR")
	            .anyRequest().authenticated()
	        )
	        .formLogin(login -> login
	            .loginPage("/")
	            .loginProcessingUrl("/login")
	            .successHandler((request, response, authentication) -> {
	                // Obtener el rol del usuario autenticado
	                authentication.getAuthorities().forEach(grantedAuthority -> {
	                    String role = grantedAuthority.getAuthority();
	                    try {
	                        if (role.equals("ADMIN")) {
	                            response.sendRedirect("/administrador/index");
	                        } else if (role.equals("EMPLEADOR")) {
	                            response.sendRedirect("/empleador/index");
	                        }
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                });
	            })
	            .failureUrl("/?error=true")
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll()
	        );

	    return http.build();
	}



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
