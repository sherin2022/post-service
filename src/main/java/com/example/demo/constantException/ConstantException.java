package com.example.demo.constantException;

public class ConstantException {
    private ConstantException() {
        // restrict instantiation
    }

    public static final String DELETEPOST = "Post deleted"  ;
    public static final String POSTNOTFOUND = "Post not found  : ";
    public static final String FEIGN_EXCEPTION = "One of the service among user, comment, like is unavailable";
    public static final String POST_ID_MISMATCH = "Id passed in url and request body does not match";
    public static final String NO_POST_FOUND = "No post available";
}
