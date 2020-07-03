package com.kderyabin.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Analyses incoming request and sets language parameter.
 * @author Konstantin Deryabin
 *
 */
//@Component
public class LangFilter implements Filter{
	

	final private Logger LOG = LoggerFactory.getLogger(LangFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}


	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		LOG.info("Start filter");
		LOG.info(req.getPathInfo());
		
		chain.doFilter(request, response);
		LOG.info("End filter");
		
	}

}
