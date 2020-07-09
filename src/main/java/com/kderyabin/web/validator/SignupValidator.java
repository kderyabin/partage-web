package com.kderyabin.web.validator;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.kderyabin.web.bean.Signup;

public class SignupValidator extends FormValidatorImpl<Signup> {

	public SignupValidator() {
		super();
	}

	/**
	 * Validates provided object.
	 * @param bean
	 */
	@Override
	public void validate(Signup bean) {
		super.validate(bean);
		// Check if passwords match
		if ( ! Objects.equals(bean.getPwd(), bean.getConfirmPwd() )) {
			addMessage("pwd", "error.password_mismatch");
			addMessage("confirmPwd", "error.password_mismatch");
		}
	}
}
