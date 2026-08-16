package org.jcdev.stockflow.backend.controller.login;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.login.LoginRequestDto;
import org.jcdev.stockflow.backend.dto.login.LoginResponseDto;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.jcdev.stockflow.backend.service.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class LoginAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginAuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> loginAuth(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(token);
        //como authentication devuelve un objeto debemos hacer cast para tratar el userDetail como un usuario
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        //obtener el rol del usuario
        GrantedAuthority grantedAuthority = userDetails.getAuthorities().iterator().next();
        String authorityRole = grantedAuthority.getAuthority();
        String role = authorityRole.replace("ROLE_", "");
        Rol rolUsuario = Rol.valueOf(role);

        //creo un objeto LoginResponseDto usando el token, el email y el rol que acabo de obtener
        LoginResponseDto respuestaLogin = new LoginResponseDto(
                jwtToken, //el token
                userDetails.getUsername(), //el email
                rolUsuario //el rol
        );
        return ResponseEntity.ok(respuestaLogin);
    }
}
