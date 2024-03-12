package com.example.slstudiomini.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class MyUniqueConstraintViolationException extends DataIntegrityViolationException{
    public MyUniqueConstraintViolationException(String message){
        super(message);
    }
}
