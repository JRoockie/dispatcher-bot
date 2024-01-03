package org.voetsky.dispatcherBot.exceptions.ParentException;

public class LogicCoreException extends RuntimeException{
    public LogicCoreException() {
    }

    public LogicCoreException(String message) {
        super(message);
    }

    public LogicCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicCoreException(Throwable cause) {
        super(cause);
    }
}
