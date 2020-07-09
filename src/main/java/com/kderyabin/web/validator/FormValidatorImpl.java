package com.kderyabin.web.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Implementation of FormValidator interface.
 * @param <T>
 */
public class FormValidatorImpl<T> implements FormValidator<T> {
	/**
	 * Validator instance.
	 */
	protected Validator validator;
	
	/**
	 * Labeled error messages where label is an object property name.
	 */
	protected Map<String, List<String>> messages = new HashMap<>();
	
	public FormValidatorImpl() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	/**
	 * Validates provided object.
	 * @param bean
	 */
	@Override
	public void validate(T bean) {
		Set<ConstraintViolation<T>> violations = validator.validate(bean);
	
		if(violations.size() > 0 ) {
			for (ConstraintViolation<T> violation : violations) {
				String property =  violation.getPropertyPath().toString();
				addMessage(property, violation.getMessage());
			}
		}
	}
	/**
	 * Add labeled message.
	 * @param key Property name.
	 * @param message Message.
	 */
	@Override
	public void addMessage(String key, String message) {
		if(!messages.containsKey(key)) {
			messages.put(key, new ArrayList<>());
		}
		messages.get(key).add(message);
	}
	/**
	 * Get validation status.
	 * @return TRUE if object is valid FALSE if contains errors.
	 */
	@Override
	public boolean isValid() {
		return messages.isEmpty();
	}
	/**
	 * @return labeled error messages
	 */
	@Override
	public Map<String, List<String>> getMessages() {
		return messages;
	}
}
