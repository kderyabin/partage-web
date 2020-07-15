package com.kderyabin.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class SimpleHttpServletRequest extends HttpServletRequestWrapper {

    final private Logger LOG = LoggerFactory.getLogger(SimpleHttpServletRequest.class);

    private Map<String, String> headers = new HashMap<>();
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public SimpleHttpServletRequest(HttpServletRequest request) {
        super(request);
        initHeaders(request);
    }

    private void initHeaders(HttpServletRequest request){
        LOG.debug("Headers");
        Enumeration<String> headerNames =  request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        headers.put("accept", "text/html");
        headers.remove("content-type");
        headers.remove("x-requested-with");
        headers.put("content-length", "0");
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> set =  new HashSet<>();
        set.add(headers.get(name));
        return Collections.enumeration(set);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    @Override
    public int getIntHeader(String name) {
        return -1;
    }
}
