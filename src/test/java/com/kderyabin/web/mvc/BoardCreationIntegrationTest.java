package com.kderyabin.web.mvc;

import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.model.UserModel;
import com.kderyabin.web.storage.AccountManager;
import com.kderyabin.web.storage.multitenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardCreationIntegrationTest {
    final private Logger LOG = LoggerFactory.getLogger(BoardCreationIntegrationTest.class);

    final private String USER_ID = "52ba882b-07b3-48e2-8aff-8183b20f1266";

    private UserModel user;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private StorageManager storageManager;

    @BeforeEach
    void setUp() {
        user = accountManager.findUserByLogin("konst93@hotmail.com");
        accountManager.createUserWorkspace(user.getId());
        accountManager.setUserSchemaFileName("data.user.sql");
        accountManager.populateUserDatabase(user.getId());
    }

    /**
     * Create an empty board
     * @throws Exception
     */
    @Test
    void boardCreation() throws Exception {
        HttpSession session = new MockHttpSession();
        // Authentication data
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("tenantId", accountManager.getUserWorkspaceName(user.getId()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Language", "en,en-US;q=0.8,fr-FR;q=0.5,en;q=0.3");

        String lang = "en";
        MvcResult mvcResult = mockMvc.perform(get("/{lang}/app/{userId}/board/new", lang, user.getId())
                        .session((MockHttpSession) session)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("app/board-edit.jsp"))
                .andReturn();

        session = mvcResult.getRequest().getSession();

        // Add participant (5,'Anna')
        mvcResult = mockMvc.perform(post("/{lang}/app/{userId}/board/add-participant", lang, user.getId())
                    .session((MockHttpSession) session)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Anna\",\"id\":5}")
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andReturn();
        session = mvcResult.getRequest().getSession();
        //  Add participant (10,'Christopher')
        mvcResult =mockMvc.perform(post("/{lang}/app/{userId}/board/add-participant", lang, user.getId())
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Christopher\",\"id\":10}")
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andReturn();
        session = mvcResult.getRequest().getSession();

        // Submit board data
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("name", "Paris");
        data.add("currency", "RUB");

        mockMvc.perform(post("/{lang}/app/{userId}/board/new", lang, user.getId())
                .session((MockHttpSession) session)
                .params(data)
                .headers(headers))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        // Validate in database.
        List<BoardModel> boards = storageManager.getRecentBoards(1);
        assertNotNull(boards.get(0));
        BoardModel savedBoard = boards.get(0);
        assertEquals("Paris", savedBoard.getName());
        assertEquals("RUB", savedBoard.getCurrencyCode());
        savedBoard = storageManager.loadParticipants(savedBoard);
        assertEquals(2, savedBoard.getParticipants().size());
    }
}
