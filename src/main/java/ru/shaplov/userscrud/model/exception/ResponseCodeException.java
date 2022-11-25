package ru.shaplov.userscrud.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseCodeException extends RuntimeException {
    private final HttpStatus code;

    public ResponseCodeException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
