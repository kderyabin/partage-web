package com.kderyabin.web.filter;

import com.kderyabin.web.model.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TenantFilterTest {

    final public static String USER_ID = "52ba882b-07b3-48e2-8aff-8183b20f1266";
    final public static String USER_URI = "/en/app/52ba882b-07b3-48e2-8aff-8183b20f1266/board/13/details";

    @Test
    @DisplayName("Should fetch a user ID from the path")
    void getUserId_success() {
        String userId = TenantFilter.getUserId(USER_URI);
        assertEquals(USER_ID, userId);
    }

    @Test
    @DisplayName("Should fail to fetch a user ID from the path")
    void getUserId_failed() {
        String path = "/en/app/signup";
        String userId = TenantFilter.getUserId(path);
        assertNull(userId);
    }
    @Test
    @DisplayName("Should fail if session is not initialized")
    void doFilterSessionNull() throws IOException, ServletException {

        MockFilterChain chain = new MockFilterChain();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(USER_URI);
        Filter filter = new TenantFilter();
        filter.doFilter(req, res, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, res.getStatus());
    }
    @Test
    @DisplayName("Should fail if user is not set in session")
    void doFilterUserNullInSession() throws IOException, ServletException {

        MockFilterChain chain = new MockFilterChain();
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(USER_URI);
        req.setSession(session);
        Filter filter = new TenantFilter();
        filter.doFilter(req, res, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, res.getStatus());
    }

    @Test
    @DisplayName("Should fail if userId in session does not match a userId in path")
    void doFilterUserId() throws IOException, ServletException {

        UserModel user = new UserModel();
        user.generateId();
        MockFilterChain chain = new MockFilterChain();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(USER_URI);
        req.setSession(session);
        Filter filter = new TenantFilter();
        filter.doFilter(req, res, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, res.getStatus());
    }

    @Test
    @DisplayName("Should fail if tenantId is not in session")
    void doFilterTenantId() throws IOException, ServletException {

        UserModel user = new UserModel();
        user.setId(USER_ID);
        MockFilterChain chain = new MockFilterChain();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(USER_URI);
        req.setSession(session);
        Filter filter = new TenantFilter();
        filter.doFilter(req, res, chain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, res.getStatus());
    }
}