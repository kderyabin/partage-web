package com.kderyabin.web.validator;

import com.kderyabin.web.bean.ResetPassword;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

class ResetPasswordValidatorTest {
    final private Logger LOG = LoggerFactory.getLogger(ResetPasswordValidatorTest.class);

    @Test
    void validate() {
        ResetPassword bean = new ResetPassword();
        bean.setPwd("Azerty99");
        bean.setConfirmPwd("");

        ResetPasswordValidator validator = new ResetPasswordValidator();
        validator.validate(bean);
        Map<String, List<String>> errors =  validator.getMessages();

        LOG.debug(errors.toString());

        assertThat(errors.get("pwd"), hasSize(2));
        assertThat(errors.get("confirmPwd"), hasSize(2));
        assertThat(errors.get("pwd"), hasItem( "error.password_invalid"));
        assertThat(errors.get("pwd"), hasItem( "error.password_mismatch"));
        assertThat(errors.get("confirmPwd"),hasItem( "error.password_confirm_empty"));
        assertThat(errors.get("confirmPwd"),hasItem( "error.password_mismatch"));
    }
}