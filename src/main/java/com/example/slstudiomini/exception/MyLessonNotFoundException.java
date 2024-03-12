package com.example.slstudiomini.exception;

import jakarta.persistence.NoResultException;

public class MyLessonNotFoundException extends NoResultException{
    public MyLessonNotFoundException(String message){
        super(message);
    }
}
