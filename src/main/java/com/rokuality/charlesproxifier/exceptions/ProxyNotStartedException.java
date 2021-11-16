package com.rokuality.charlesproxifier.exceptions;

public class ProxyNotStartedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProxyNotStartedException(String inText) {
        super(inText);
    }
  
}
