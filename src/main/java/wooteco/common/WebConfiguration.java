package wooteco.common;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<PageFilter> loggingFilter(){
        FilterRegistrationBean<PageFilter> registrationBean
            = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PageFilter());
        registrationBean.addUrlPatterns("/", "/stations", "/lines",
            "/sections", "/path", "/login",
            "/join", "/mypage", "/mypage/edit", "/favorites");
        return registrationBean;
    }
}
