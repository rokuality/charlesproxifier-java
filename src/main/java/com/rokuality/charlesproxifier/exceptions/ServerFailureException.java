package com.rokuality.charlesproxifier.exceptions;

public class ServerFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerFailureException(String inText) {
        super(inText);
    }
  
}
