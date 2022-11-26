package me.agronaut.springrest.exception;

public class EmailException extends RuntimeException{
    public EmailException(String msg) {
        super(msg);
    }
}
