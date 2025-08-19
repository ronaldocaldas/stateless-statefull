package br.com.microservices.statelessauthapi.service;

import br.com.microservices.statelessauthapi.core.dto.AuthRequest;
import br.com.microservices.statelessauthapi.core.dto.TokenDTO;
import br.com.microservices.statelessauthapi.core.model.infra.exception.ValidationException;
import br.com.microservices.statelessauthapi.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenDTO login(AuthRequest request) throws Throwable {
        var user = userRepository.findByUsername(request.userName())
                .orElseThrow(() -> new ValidationException("User not found!"));
        var accessToken = jwtService.createToken(user);

        validPassword(request.password(), user.getPassword());
        return new TokenDTO(accessToken);
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ValidationException("The password do not match!");
        };
    }

    public TokenDTO validateToken(String accessToken){
        validateExistingToken(accessToken);
        jwtService.getClaims(accessToken);
        return new TokenDTO(accessToken);
    }
    private void validateExistingToken(String accessToken){
        if(isEmpty(accessToken)){
            throw new ValidationException("The access token must be informed!");
        }
    }
}
