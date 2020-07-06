package com.kderyabin.web.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        HttpServletResponse res = (HttpServletResponse) response;
        Pattern pattern = Pattern.compile("^/(fr|en)/.*?");
        Matcher matcher = pattern.matcher(req.getRequestURI());
        if(matcher.matches()) {
        	request.setAttribute("lang", matcher.group(1));
        	LOG.debug("Matched language: " + matcher.group(1));
        }
		
		chain.doFilter(request, response);		
	}

}
