package com.example.demo.exception;

public class NoPostAvailableException extends RuntimeException{
    public NoPostAvailableException(String s){
        super(s);
    }
}
