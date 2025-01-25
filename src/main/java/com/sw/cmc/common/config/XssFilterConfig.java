package com.sw.cmc.common.config;

//import org.owasp.html.PolicyFactory;
import com.sw.cmc.common.filter.XssFilter;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : XssPolicyConfig
 * author         : ihw
 * date           : 2025. 1. 26.
 * description    : XssFilterConfig
 */
@Configuration
public class XssFilterConfig {

    private final PolicyFactory policyFactory;

    public XssFilterConfig(PolicyFactory policyFactory) {
        this.policyFactory = policyFactory;
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter(policyFactory));
        registrationBean.setOrder(1); // 필터 우선순위 설정
        registrationBean.addUrlPatterns("/*"); // 모든 요청에 필터 적용
        return registrationBean;
    }
}