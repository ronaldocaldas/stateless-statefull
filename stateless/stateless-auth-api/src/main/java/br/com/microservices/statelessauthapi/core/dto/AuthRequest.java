package br.com.microservices.statelessauthapi.core.dto;

public record AuthRequest(String userName, String password) {
}
