package com.sw.cmc.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.owasp.html.PolicyFactory;

import java.io.IOException;
import java.util.HashMap;
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

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            XssRequestWrapper wrappedRequest = new XssRequestWrapper(httpRequest, policyFactory);
            chain.doFilter(wrappedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class XssRequestWrapper extends HttpServletRequestWrapper {
        private final PolicyFactory policyFactory;

        public XssRequestWrapper(HttpServletRequest request, PolicyFactory policyFactory) {
            super(request);
            this.policyFactory = policyFactory;
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value != null ? policyFactory.sanitize(value) : null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }
            String[] sanitizedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                sanitizedValues[i] = policyFactory.sanitize(values[i]);
            }
            return sanitizedValues;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            Map<String, String[]> sanitizedMap = new HashMap<>();
            parameterMap.forEach((key, values) -> {
                String[] sanitizedValues = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    sanitizedValues[i] = policyFactory.sanitize(values[i]);
                }
                sanitizedMap.put(key, sanitizedValues);
            });
            return sanitizedMap;
        }
    }
}
