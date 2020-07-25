package com.kderyabin.web.filter;

import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.services.Utils;
import com.kderyabin.web.storage.multitenancy.TenantContext;
import org.apache.catalina.filters.ExpiresFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls access to the user space and sets tenant ID for DB initialization.
 * Access requirements: session must be active and must contain tenant ID.
 */
@Component
public class TenantFilter implements Filter {

    final private Logger LOG = LoggerFactory.getLogger(TenantFilter.class);

    private SettingsService settingsService;

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Pattern pattern = Pattern.compile("^/[a-z]{2}/app/([a-z0-9A-Z\\-]{36})/.*?");
        Matcher matcher = pattern.matcher(req.getRequestURI());
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        if(matcher.matches()) {
            LOG.info("TenantFilter: user space pattern matched");
            HttpSession session = req.getSession( false );
            if(session == null ){
                LOG.info("TenantFilter: session is not initialized");
                // User space can be accessed only with valid session
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String tenantId = (String) session.getAttribute("tenantId");
            if(tenantId == null) {
                LOG.info("TenantFilter: tenantId is not set in session");
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            LOG.info("TenantFilter: tenantId is OK in session");
            TenantContext.setTenant(tenantId);
            // Initialize user settings
            initUserSettings(req);
        }
        chain.doFilter(request, response);
    }

    /**
     * Initialize user settings.
     * The method must be run after TenantContext is switched to user space
     * @param req Request
     */
    protected void initUserSettings(HttpServletRequest req) {
        // Initialize  default settings
        String lang = (String) req.getAttribute("lang");
        settingsService.setLanguage(lang);
        // Try to find the user currency from the header
        String header = req.getHeader("Accept-Language");
        LOG.debug("Accept-Language header: " + header);
        Locale userLocale = Utils.getLocaleFromAcceptLanguageHeader(header);
        Currency defaultCurrency = Currency.getInstance(userLocale);
        settingsService.setCurrency(defaultCurrency);
        LOG.debug("Default settings currency will be : " + defaultCurrency.getCurrencyCode());
        // Load user settings
        settingsService.load();
    }
}
