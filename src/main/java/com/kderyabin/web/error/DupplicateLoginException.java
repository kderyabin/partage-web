package com.kderyabin.web.error;

/**
 * Thrown when user login is already registered in database.
 */
public class DupplicateLoginException extends RuntimeException {
    public DupplicateLoginException() {
    }

    public DupplicateLoginException(String message) {
        super(message);
    }

    public DupplicateLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public DupplicateLoginException(Throwable cause) {
        super(cause);
    }

    public DupplicateLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
