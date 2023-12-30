package org.voetsky.dispatcherBot.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.voetsky.dispatcherBot.dtos.ErrorDto;
import org.voetsky.dispatcherBot.exceptions.AdminViewException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { AdminViewException.class })
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AdminViewException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorDto(ex.getMessage()));
    }
}