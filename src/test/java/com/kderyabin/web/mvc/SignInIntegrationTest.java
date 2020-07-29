package com.kderyabin.web.mvc;

import com.kderyabin.web.model.UserModel;
import com.kderyabin.web.storage.AccountManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class SignInIntegrationTest {

    final private Logger LOG = LoggerFactory.getLogger(SignInIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountManager accountManager;

    /**
     * Test authentication
     * @throws Exception
     */
    @Test
    void authenticate() throws Exception {
        String lang = "en";
        String LOGIN = "konst93@hotmail.com";
        String PWD = "123456789";
        UserModel user = accountManager.findUserByLogin(LOGIN);
        String expectedRedirectUrl = String.format("redirect:app/%s/", user.getId());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("login", LOGIN);
        form.add("pwd", PWD);
        MvcResult mvcResult = mockMvc.perform(post("/{lang}/signin", lang)
                .params(form))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(expectedRedirectUrl))
                .andReturn();

        // check session variables are set up
        HttpSession session = mvcResult.getRequest().getSession();
        assertEquals(user.getId(), session.getAttribute("userId"));
        assertEquals(user, session.getAttribute("user"));
        assertNotNull(session.getAttribute("tenantId"));
    }
}