package com.sw.cmc.common.config;

//import org.owasp.html.PolicyFactory;
import com.sw.cmc.common.filter.XssFilter;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.io.IOException;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : XssPolicyConfig
 * author         : ihw
 * date           : 2025. 1. 26.
 * description    : XssFilterConfig
 */
@Configuration
public class XssFilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> testFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                System.out.println("Test filter executed");
                chain.doFilter(request, response);
            }
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}

//@Configuration
//public class XssFilterConfig {
//
//    @Autowired
//    private PolicyFactory policyFactory;
//
////    @Bean
////    public PolicyFactory policyFactory() {
////        return new HtmlPolicyBuilder()
////                .allowElements("a", "b", "i", "u", "strong", "em")
////                .allowAttributes("href").onElements("a")
////                .toFactory();
////    }
//
//    @Bean
//    public PolicyFactory testPolicyFactory() {
//        return new HtmlPolicyBuilder().allowElements("b", "i").toFactory();
//    }
//
//
////    @Bean
////    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
////        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
////        registrationBean.setFilter(new XssFilter(policyFactory));
////        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
////        registrationBean.addUrlPatterns("/*");
////        return registrationBean;
////    }
//
//    @Bean
//    public FilterRegistrationBean<Filter> simpleFilterRegistrationBean() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new Filter() {
//            @Override
//            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//                    throws IOException, ServletException {
//                System.out.println("Simple Filter is working!");
//                chain.doFilter(request, response);
//            }
//        });
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//
//}
