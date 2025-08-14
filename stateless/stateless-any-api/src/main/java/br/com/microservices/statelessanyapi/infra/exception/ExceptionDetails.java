package br.com.microservices.statelessanyapi.infra.exception;

public record ExceptionDetails(int status, String message) {
}
