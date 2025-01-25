package com.sw.cmc.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.owasp.html.PolicyFactory;

import java.io.IOException;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.common.filter
 * fileName       : XssFilter
 * author         : ihw
 * date           : 2025. 1. 26.
 * description    : XssFilter
 */
public class XssFilter implements Filter {
    private final PolicyFactory policyFactory;

    public XssFilter(PolicyFactory policyFactory) {
        this.policyFactory = policyFactory;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();

        parameterMap.forEach((key, values) -> {
            for (int i = 0; i < values.length; i++) {
                values[i] = policyFactory.sanitize(values[i]);
            }
        });

        chain.doFilter(request, response);
    }
}
