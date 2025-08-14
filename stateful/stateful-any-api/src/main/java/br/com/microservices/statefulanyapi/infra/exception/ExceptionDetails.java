package br.com.microservices.statefulanyapi.infra.exception;

public record ExceptionDetails(int status, String message) {
}
