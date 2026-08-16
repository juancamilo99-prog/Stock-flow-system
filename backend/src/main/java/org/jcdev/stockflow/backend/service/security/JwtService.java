package org.jcdev.stockflow.backend.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String jwtSecret;
    private final Long jwtExpiration;


    public JwtService(@Value("${jwt.secret}")String tokenJjwt, @Value("${jwt.expiration}") Long tokenExpirationTime) {
        jwtSecret = tokenJjwt;
        jwtExpiration = tokenExpirationTime;
    }

    //usamos la clave secreta para convertirla a bytes con una codificacion estable
    //como UTF-8 y pasar esos bytes.
    private SecretKey generarClaveCodificada(String key) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    //generamos el JWT
    public String generateToken(UserDetails userDetails) {
        //separamos la fecha de emision y la fecha de expiracion
        Date fechaEmision = new Date(System.currentTimeMillis());
        //fecha expiracion = fecha emision + fecha de expiracion (property)
        Date fechaExpiracion = new Date(fechaEmision.getTime() + jwtExpiration);
        return Jwts.builder()
                .subject(userDetails.getUsername()) // identifica a quien le pertenece el token
                .issuedAt(fechaEmision) // fecha y hora actual de la emision del token
                .expiration(fechaExpiracion) // fecha de expiracion del token
                .signWith(generarClaveCodificada(jwtSecret)) //firma con nuesta secretKey
                .compact(); // genera el String JWT
    }

    public String extraerEmail(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(generarClaveCodificada(jwtSecret)) //verifica la firma coincida con la jwt
                .build() // se construye el parseo
                .parseSignedClaims(token); //le entregamos el jwt recibido y verifica y parsea el jwt
        return claimsJws.getPayload().getSubject();
    }

    public boolean validarToken(String token, UserDetails userDetails) {
        String emailExtraido = extraerEmail(token);
        String emailUserDetail = userDetails.getUsername();
        //comparamos que los email coincidan
        return emailExtraido.equalsIgnoreCase(emailUserDetail);
    }
}
