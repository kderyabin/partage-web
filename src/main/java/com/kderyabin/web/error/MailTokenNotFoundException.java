package com.kderyabin.web.error;

/**
 * Thrown in case of mail token is not found in database.
 */
public class MailTokenNotFoundException extends RuntimeException {
    public MailTokenNotFoundException() {
        super();
    }

    public MailTokenNotFoundException(String message) {
        super(message);
    }

    public MailTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    protected MailTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
