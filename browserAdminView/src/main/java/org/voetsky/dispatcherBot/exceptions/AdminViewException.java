package org.voetsky.dispatcherBot.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AdminViewException extends RuntimeException {

    private final HttpStatus status;

    public AdminViewException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
