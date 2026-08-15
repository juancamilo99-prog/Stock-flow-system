package org.jcdev.stockflow.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        //Creamos el proveedor de autenticacion y le indicamos
        //el servicio encargado de buscar los usuarios
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        //Le indicamos el PasswordEncoder que utilizara para comparar
        // la contraseña introducida con el hash guardado en la BD
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
