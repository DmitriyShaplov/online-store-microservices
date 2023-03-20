package ru.shaplov.common.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.shaplov.common.model.ErrorInfo;
import ru.shaplov.common.model.exception.ResponseCodeException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseCodeException.class)
    ResponseEntity<ErrorInfo> handleResponseCode(ResponseCodeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getCode())
                .body(new ErrorInfo(ex.getCode().value(), ex.getMessage()));
    }
}
