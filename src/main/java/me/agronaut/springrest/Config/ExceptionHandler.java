package me.agronaut.springrest.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import liquibase.pro.packaged.W;
import me.agronaut.springrest.Exception.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request)
    {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setDebugMessage("Cannot find requested entity");
        error.setTimestamp(LocalDateTime.now());

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpired(ExpiredJwtException ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getMessage());
        error.setDebugMessage("Token expired please login again for new token");
        error.setTimestamp(LocalDateTime.now());

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpired(UnsupportedJwtException ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getMessage());
        error.setDebugMessage("Token is unsupported");
        error.setTimestamp(LocalDateTime.now());

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpired(MalformedJwtException ex, WebRequest request) {
        ApiError error = new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getMessage());
        error.setDebugMessage("Token is malformed");
        error.setTimestamp(LocalDateTime.now());

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }
}
