package com.example.demo.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String s) {
        super(s);
    }
}
