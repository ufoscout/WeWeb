package com.weweb.auth.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthWebMvcConfigurer implements WebMvcConfigurer {

    private final UserContextResolver userContextResolver;

    AuthWebMvcConfigurer(UserContextResolver userContextResolver) {
        this.userContextResolver = userContextResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userContextResolver);
    }

}
