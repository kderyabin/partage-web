package com.kderyabin.web.filter;

import com.kderyabin.web.services.SettingsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class TenantFilterTest {

    @Test
    void getUserId_success() {
        String path = "/en/app/52ba882b-07b3-48e2-8aff-8183b20f1266/board/13/details";
        String userId = TenantFilter.getUserId(path);
        assertEquals("52ba882b-07b3-48e2-8aff-8183b20f1266", userId);
    }

    @Test
    void getUserId_failed() {
        String path = "/en/app/signup";
        String userId = TenantFilter.getUserId(path);
        assertNull(userId);
    }
}