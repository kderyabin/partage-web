package com.kderyabin.web.filter;

import com.kderyabin.web.storage.multitenancy.TenantContext;
import org.apache.catalina.filters.ExpiresFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls access to the user space and sets tenant ID for DB initialization.
 * Access requirements: session must be active and must contain tenant ID.
 */
@Component
public class TenantFilter implements Filter {

    final private Logger LOG = LoggerFactory.getLogger(TenantFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Pattern pattern = Pattern.compile("^/[a-z]{2}/app/([a-z0-9A-Z\\-]{36})/.*?");
        Matcher matcher = pattern.matcher(req.getRequestURI());
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
        }
        chain.doFilter(request, response);

    }
}
