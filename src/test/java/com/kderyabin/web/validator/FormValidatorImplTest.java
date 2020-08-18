package com.kderyabin.web.validator;

import com.kderyabin.web.bean.ResetPassword;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import java.util.Map;

class FormValidatorImplTest {

    final private Logger LOG = LoggerFactory.getLogger(FormValidatorImplTest.class);

    @Test
    @DisplayName("Should find errors in invalid bean")
    void validate() {
        FormValidator<ResetPassword> validator = new FormValidatorImpl<ResetPassword>();
        ResetPassword bean  = new ResetPassword();
        // wrong password
        bean.setPwd("Az2$");
        validator.validate(bean);

        LOG.debug(validator.getMessages().toString());
        Map<String, List<String>> errors = validator.getMessages();
        assertThat(errors, IsMapContaining.hasKey("pwd"));
        assertThat(errors, IsMapContaining.hasKey("confirmPwd"));
    }
}