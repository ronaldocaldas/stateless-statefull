package br.com.microservices.statelessanyapi.core.service;

import br.com.microservices.statelessanyapi.core.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

    private final JwtService jwtService;

    public AnyResponse getData(String accessToken) {
        jwtService.validateAccessToken(accessToken);
        var authUser = jwtService.getAuthUserResponse(accessToken);

        var ok = HttpStatus.OK;

        return new AnyResponse(ok.name(), ok.value(), authUser);
    }


}
