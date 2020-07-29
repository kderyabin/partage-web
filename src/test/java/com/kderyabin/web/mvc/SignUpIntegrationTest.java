package com.kderyabin.web.mvc;

import com.kderyabin.web.model.MailActionModel;
import com.kderyabin.web.model.UserModel;
import com.kderyabin.web.services.MailWorkerService;
import com.kderyabin.web.storage.AccountManager;
import com.kderyabin.web.storage.MailAction;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpIntegrationTest {

    final private Logger LOG = LoggerFactory.getLogger(SignUpIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    MailWorkerService mailWorkerService;

    /**
     * Test registration: sign up form submission and email validation
     *
     * @throws Exception
     */
    @Test
    void register() throws Exception {
        String lang = "en";
        String login = "john@john.com";
        // 1. Submit sign up form
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "John");
        form.add("login", login);
        form.add("pwd", "123456789");
        form.add("confirmPwd", "123456789");
        mockMvc.perform(post("/{lang}/signup", lang)
                .params(form))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/en/confirm-email"));

        UserModel user = accountManager.findUserByLogin("john@john.com");
        assertNotNull(user);
        assertEquals(false, user.getIsConfirmed());
        MailActionModel action = accountManager.findMailActionByUser(user);
        assertNotNull(action);
        assertEquals(MailAction.CONFIRM, action.getAction());

        // 2. validate email
        LOG.debug("Saved mail action" + action.toString());
        String confirmUrl = mailWorkerService.getConfirmEmailLink(action, lang);
        String expectedRedirectUrl = String.format("redirect:/%s/app/%s/", lang, user.getId());
        LOG.debug("confirmUrl" + confirmUrl);

        MvcResult mvcResult = mockMvc.perform(get(confirmUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(expectedRedirectUrl))
                .andReturn();
        // check user status status is updated
        user = accountManager.findUserByLogin("john@john.com");
        assertEquals(true, user.getIsConfirmed());
        // check mail action is removed
        action = accountManager.findMailActionByUser(user);
        assertNull(action);
        // check session variables are set up
        HttpSession session = mvcResult.getRequest().getSession();
        assertEquals(user.getId(), session.getAttribute("userId"));
        assertEquals(user, session.getAttribute("user"));
        assertNotNull(session.getAttribute("tenantId"));
    }
}