package br.com.microservices.statelessanyapi.core.service;

import br.com.microservices.statelessanyapi.core.dto.AuthUserResponse;
import br.com.microservices.statelessanyapi.infra.exception.AuthenticationException;
import br.com.microservices.statelessanyapi.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.token.secret-key}")
    private String secretKey;

    private static final String EMPYT_SPACE = " ";

    private static final Integer TOKEN_INDEX = 1;

    public AuthUserResponse getAuthUserResponse(String token) {
        var tokenClains = getClaims(token);
        var userId = Integer.valueOf((String) tokenClains.get("id"));

        return new AuthUserResponse(userId, (String) tokenClains.get("username"));
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

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }

        if (token.contains(EMPYT_SPACE)) {
            return token.split(EMPYT_SPACE)[TOKEN_INDEX];
        }
        return token;
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public void validateAccessToken(String token) {
        getClaims(token);
    }
}
