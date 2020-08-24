package com.kderyabin.web.validator;

import com.kderyabin.web.bean.Signup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignupValidatorTest {
    final private Logger LOG = LoggerFactory.getLogger(SignupValidatorTest.class);

    @Test
    void validate(){
        Signup bean = new Signup();
        bean.setLogin("xxx@xxx.fr");
        bean.setName("James");
        bean.setPwd("Aold2xx!0");
        bean.setConfirmPwd("Aold2xxxo");

        SignupValidator validator = new SignupValidator();
        validator.validate(bean);
        Map<String, List<String>> errors =  validator.getMessages();

        LOG.debug(errors.toString());

        assertThat(errors.get("pwd"), hasSize( 1));
        assertThat(errors.get("confirmPwd"), hasSize( 1));
        assertThat(errors.get("pwd"), hasItem( "error.password_mismatch"));
        assertThat(errors.get("confirmPwd"),hasItem( "error.password_mismatch"));

    }
}