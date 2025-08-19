package br.com.microservices.statelessauthapi.service;

import br.com.microservices.statelessauthapi.code.model.User;
import br.com.microservices.statelessauthapi.core.model.infra.exception.AuthenticationException;
import br.com.microservices.statelessauthapi.core.model.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.token.secret-key}")
    private String secretKey;

    private static final Integer ONE_DAY = 1;

    private static final String EMPYT_SPACE= " ";

    private static final Integer TOKEN_INDEX = 1;

    public String createToken(User user) {
        var data = new HashMap<String, String>();
        data.put("id", user.getId().toString());
        data.put("username", user.getUsername());
        return Jwts
                .builder()
                .claims(data)
                .expiration(generateExpiresAt())
                .signWith(generateSign())
                .compact();
    }

    private SecretKey generateSign(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    private Date generateExpiresAt(){
        return Date.from(
                Instant.now().plusSeconds(24 * 60 * 60) // +24h
        );
    }
    public Claims getClaims(String token) {
        var accessToken = extractToken(token);
        try {
            return Jwts
                    .parser()
                    .verifyWith(generateSign())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token: " + ex.getMessage());
        }
    }


    public void validateAccessToken(String token) {
        var accessToken = extractToken(token);
        try {
            Jwts.parser()
                    .verifyWith(generateSign())
                    .build()
                    .parseEncryptedContent(accessToken)
                    .getPayload();
        } catch (Exception e) {
            throw new AuthenticationException("Inválid Token!" + e.getMessage());
        }
    }
    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }

        if (token.contains(EMPYT_SPACE)) {
            return token.split(EMPYT_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
