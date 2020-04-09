package br.com.ronistone.xyinc.controller;

import br.com.ronistone.xyinc.exception.ValidationAttributeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorHandlerController {

    private static Logger logger = LogManager.getLogger(ErrorHandlerController.class);

    @ExceptionHandler(ValidationAttributeException.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex) {
        logger.warn("Bad Request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
