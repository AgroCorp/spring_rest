package me.agronaut.springrest.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import me.agronaut.springrest.exception.ApiError;
import me.agronaut.springrest.service.UserService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request)
    {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(), "Cannot find requested entity");

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchException(NoSuchElementException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(), "cannot find requested entity with given id");

        return handleExceptionInternal(ex, error, new HttpHeaders(),  error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpired(ExpiredJwtException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), "Token expired please login again for new token");

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(UnsupportedJwtException.class)
    protected ResponseEntity<Object> handleExpired(UnsupportedJwtException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), "Token is unsupported");

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MalformedJwtException.class)
    protected ResponseEntity<Object> handleExpired(MalformedJwtException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), "Token is malformed");

        return handleExceptionInternal(ex,error,new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserService.NotActiveUserException.class)
    protected ResponseEntity<Object> handleNotActivated(UserService.NotActiveUserException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), "User is not activated. Please click the link in the e-mail");

        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(UserService.UserExistByEmailException.class)
    protected ResponseEntity<Object> handleNotActivated(UserService.UserExistByEmailException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST,LocalDateTime.now(),ex.getMessage(), "This e-mail address is taken. Please select other!");
        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleValidationException(ConstraintViolationException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";")), "");
        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), ex.getMessage(), "You dont have permission to this site, or you need to log in.");

        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleBaseExecption(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), ex.getMessage(), "Unknown exception");

        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST,LocalDateTime.now(), ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")),
                "You have invalid data. Please read error messages for more details");

        return handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

}
