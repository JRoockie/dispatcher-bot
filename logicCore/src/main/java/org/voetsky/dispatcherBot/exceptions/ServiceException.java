package org.voetsky.dispatcherBot.exceptions;

import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;

public class ServiceException extends LogicCoreException {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
