package com.example.boot.exceptions;

public class QueryNotSupportedException extends Exception {
    public QueryNotSupportedException(String message) {
      super(message);
    }
  }