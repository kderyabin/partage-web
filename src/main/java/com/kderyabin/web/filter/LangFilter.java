package com.kderyabin.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzes incoming request and sets language parameter.
 *
 * @author Konstantin Deryabin
 */
@Component
public class LangFilter implements Filter {

    final private Logger LOG = LoggerFactory.getLogger(LangFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!req.getRequestURI().equals("/") && !isStaticContent(req) && !checkLanguage(req)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * Checks if requested URI is a static content
     *
     * @param req Current request
     * @return TRUE if requested URI is a static content FALSE otherwise
     */
    public boolean isStaticContent(HttpServletRequest req) {
        Pattern pattern = Pattern.compile(".*?\\.(css|js|ico|png)$");
        Matcher matcher = pattern.matcher(req.getRequestURI());
        return matcher.matches();
    }

    /**
     * Checks language parameter in URI path and sets language parameter in request.
     *
     * @param req Current request
     * @return TRUE if language is valid FALSE otherwise
     */
    public boolean checkLanguage(HttpServletRequest req) {
        Pattern pattern = Pattern.compile("^/(fr|en)/.*?");
        Matcher matcher = pattern.matcher(req.getRequestURI());
        if (matcher.matches()) {
            String lang = matcher.group(1);
            req.setAttribute("lang", lang);
            LOG.debug("Matched language: " + matcher.group(1));
            return true;
        }
        return false;
    }
}
