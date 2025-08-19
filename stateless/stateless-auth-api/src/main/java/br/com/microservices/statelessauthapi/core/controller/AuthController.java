package br.com.microservices.statelessauthapi.core.controller;

import br.com.microservices.statelessauthapi.core.dto.AuthRequest;
import br.com.microservices.statelessauthapi.core.dto.TokenDTO;
import br.com.microservices.statelessauthapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public TokenDTO login(@RequestBody AuthRequest request) throws Throwable {
        return authService.login(request);
    }


    @PostMapping("token/validate")
    public TokenDTO validate(@RequestHeader("Authorization") String authorizationHeader) {
        return authService.validateToken(authorizationHeader);
    }
}
