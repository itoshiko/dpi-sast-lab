package com.sast.form.exception;

public class ColumnException extends RuntimeException {

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ColumnException(String message) {
        super(message);
    }
}
