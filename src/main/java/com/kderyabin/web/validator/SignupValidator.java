package com.kderyabin.web.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kderyabin.web.dto.SignupDTO;

public class SignupValidator {
	
	final private Logger LOG = LoggerFactory.getLogger(SignupValidator.class);
	/**
	 * Validator instance.
	 */
	Validator validator;
	
	/**
	 * Labeled error messages where key is a DOM object name and value is a resource key.
	 */
	Map<String, List<String>> messages = new HashMap<>();
	
	public SignupValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	/**
	 * Validates provided object.
	 * @param bean
	 */
	public void validate(SignupDTO bean) {
		Set<ConstraintViolation<SignupDTO>> violations = validator.validate(bean);
	
		if(violations.size() > 0 ) {
			for (ConstraintViolation<SignupDTO> violation : violations) {
				String property =  violation.getPropertyPath().toString();
				addMessage(property, violation.getMessage());
			}
		}
		// Check if passwords match
		if ( ! Objects.equals(bean.getPwd(), bean.getConfirmPwd() )) {
			addMessage("pwd", "error.password_mismatch");
			addMessage("confirmPwd", "error.password_mismatch");
		}
	}
	/**
	 * Add labeled message.
	 * @param key Property name.
	 * @param message Message.
	 */
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
	public boolean isValid() {
		return messages.isEmpty();
	}
	/**
	 * 
	 * @return labeled error messages
	 */
	public Map<String, List<String>> getMessages() {
		return messages;
	}
}
