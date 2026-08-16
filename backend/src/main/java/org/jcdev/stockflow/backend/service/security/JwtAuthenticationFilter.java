package org.jcdev.stockflow.backend.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; //extraer y validar el token
    private final UserDetailsService userDetailsService; //cargar el usuario apartir del email

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuthorization = request.getHeader("Authorization");
        if (headerAuthorization == null || !headerAuthorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        //usando substring hacemos que token tome el valor de los caracteres despues de "Bearer"
        String token = headerAuthorization.substring("Bearer ".length());
        String emailObtenido = jwtService.extraerEmail(token);
        Authentication authenticationActual = SecurityContextHolder.getContext().getAuthentication(); //preguntamos si ya tenemos un usuario autenticado
        //validamos que el email obtenido exista y no tenga autenticacion
        if (emailObtenido != null && authenticationActual == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(emailObtenido); //cargamos el usuario
            //validamos el token y el usuario
            if (jwtService.validarToken(token, userDetails)) {
                //instanciamos el objeto autenticado y lo pasamos, usuario, ya no necesita password y rol o permisos
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                //registramos la autenticacion del usuario y sus autoridades
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
