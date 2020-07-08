package com.kderyabin.web.validator;

import java.util.List;
import java.util.Map;

/**
 * Validates form inputs.
 * @param <T>
 */
public interface FormValidator<T> {
    /**
     * Validates provided object.
     * @param bean Instance
     */
    void validate(T bean);
    /**
     * Add labeled message.
     * @param key Property name (form input name).
     * @param message Message (Error message).
     */
    void addMessage(String key, String message);
    /**
     * Get validation status.
     * @return TRUE if object is valid FALSE if contains errors.
     */
    boolean isValid();
    /**
     * Get error messages.
     * @return Map of labeled error messages
     */
    Map<String, List<String>> getMessages();
}
