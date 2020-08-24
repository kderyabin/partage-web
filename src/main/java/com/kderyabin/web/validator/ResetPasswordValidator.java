package com.kderyabin.web.validator;

import com.kderyabin.web.bean.ResetPassword;
import com.kderyabin.web.bean.Signup;

import java.util.Objects;

public class ResetPasswordValidator extends FormValidatorImpl<ResetPassword>{
    /**
     * Validates provided object.
     * @param bean
     */
    @Override
    public void validate(ResetPassword bean) {
        super.validate(bean);
        // Check if passwords match
        if ( ! Objects.equals(bean.getPwd(), bean.getConfirmPwd() )) {
            addMessage("pwd", "error.password_mismatch");
            addMessage("confirmPwd", "error.password_mismatch");
        }
    }
}
