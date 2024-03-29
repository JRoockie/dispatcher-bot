package org.voetsky.dispatcherBot.exceptions;

import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;

public class IncorrectInputException extends LogicCoreException {

    public IncorrectInputException() {
    }

    public IncorrectInputException(String message) {
        super(message);
    }

    public IncorrectInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectInputException(Throwable cause) {
        super(cause);
    }

}
