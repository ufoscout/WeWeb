package com.weweb.auth.config;

import com.weweb.core.jwt.JwtService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private JwtService jwtTokenUtil;
    private String tokenHeader;
    private String userContextAttributeKey;

    public JwtAuthorizationTokenFilter(JwtService jwtService, String tokenHeader, String userContextAttributeKey) {
        this.jwtTokenUtil = jwtService;
        this.tokenHeader = tokenHeader;
        this.userContextAttributeKey = userContextAttributeKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.debug("processing authentication for '{}'", request.getRequestURL());

        final String requestHeader = request.getHeader(this.tokenHeader);

        UserContext userContext = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String authToken = requestHeader.substring(7);
            log.debug("Found jwt [{}]", authToken);
            userContext = jwtTokenUtil.parse(authToken, UserContext.class);
            request.setAttribute(userContextAttributeKey, userContext);
        } else {
            log.debug("couldn't find bearer string, will ignore the header");
        }

        if (userContext != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userContext, null, mapAuthorities(userContext.getRoles()));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.debug("authorizated user '{}', setting security context", userContext.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private static List<GrantedAuthority> mapAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
