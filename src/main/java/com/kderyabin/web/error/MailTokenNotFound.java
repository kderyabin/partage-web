package com.kderyabin.web.error;

/**
 * Thrown in case of mail token is not found in database.
 */
public class MailTokenNotFound extends RuntimeException {
    public MailTokenNotFound() {
        super();
    }

    public MailTokenNotFound(String message) {
        super(message);
    }

    public MailTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public MailTokenNotFound(Throwable cause) {
        super(cause);
    }

    protected MailTokenNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
