package com.kderyabin.web.validator;

import com.kderyabin.web.bean.Signin;
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
        FormValidator<Signin> validator = new FormValidatorImpl<Signin>();
        Signin bean  = new Signin();
        bean.setLogin("James");
        bean.setPwd("xxxx");
        validator.validate(bean);

        LOG.debug(validator.getMessages().toString());

        assertFalse(validator.isValid());

        Map<String, List<String>> errors = validator.getMessages();
        assertThat(errors, IsMapContaining.hasKey("login"));
        assertThat(errors, IsMapContaining.hasKey("pwd"));
    }
}