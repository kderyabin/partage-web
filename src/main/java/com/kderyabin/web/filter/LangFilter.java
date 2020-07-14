package com.kderyabin.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.kderyabin.web.services.SettingsService;
import com.kderyabin.web.services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Analyses incoming request and sets language parameter.
 * @author Konstantin Deryabin
 *
 */
@Component
public class LangFilter implements Filter{
	

	final private Logger LOG = LoggerFactory.getLogger(LangFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
        Pattern pattern = Pattern.compile("^/(fr|en)/.*?");
        Matcher matcher = pattern.matcher(req.getRequestURI());
        if(matcher.matches()) {
        	String lang = matcher.group(1);
        	request.setAttribute("lang", lang);
        	LOG.debug("Matched language: " + matcher.group(1));
        }
		chain.doFilter(request, response);
	}

}
