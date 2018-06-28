package com.weweb.auth.config;

import com.ufoscout.coreutils.jwt.JwtService;
import com.weweb.core.config.CoreConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;

    public JwtWebSecurityConfigurerAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Custom JWT based security filter
        JwtAuthorizationTokenFilter authenticationTokenFilter =
                new JwtAuthorizationTokenFilter(jwtService, AuthConfig.JWT_TOKEN_HEADER_KEY,
                        AuthConfig.AUTH_USER_CONTEXT_ATTRIBUTE_KEY);

        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //.and().addFilterBefore(authenticationTokenFilter, ChannelProcessingFilter.class)

                .authorizeRequests()

                .antMatchers(AuthContants.BASE_UM_API + "/**").permitAll()
                .antMatchers(CoreConstants.BASE_PUBLIC_API+ "/**").permitAll()
                .anyRequest().authenticated();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        AuthContants.BASE_UM_API + AuthContants.CURRENT_USER_AUTH_URL,
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                );
    }
}
