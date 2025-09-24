package br.com.fiap.smartmottu.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

    private final SecretKey chave_privada = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String construirToken(String username, String senha) {

        Date data_atual = new Date();

        JwtBuilder builder = Jwts.builder()
                .subject(username)
                .subject(senha)
                .issuedAt(data_atual)
                .expiration(new Date(data_atual.getTime() + (3600000)))
                .signWith(chave_privada);
        return builder.compact();

    }

    public String extrairUsername(String token) {

        JwtParser parser = Jwts.parser().verifyWith(chave_privada).build();

        return parser.parseSignedClaims(token).getPayload().getSubject();

    }

    public boolean validarToken(String token){

        try {

            JwtParser parser = Jwts.parser().verifyWith(chave_privada).build();

            parser.parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }

    }

}