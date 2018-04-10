package com.weweb.auth.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.weweb.auth.model.UserAuthentication;
import com.weweb.auth.model.UserContext;
import com.weweb.core.jwt.JwtService;
import com.weweb.core.jwt.TokenExpiredException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            try {
                userContext = jwtTokenUtil.parse(authToken, UserContext.class);
                request.setAttribute(userContextAttributeKey, userContext);
            } catch (TokenExpiredException e) {
                log.debug("Token [{}] is expired", authToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.getWriter().print("{\"code\":401,\"message\":\"TokenExpired\"}");
                response.getWriter().close();
                return;
            }
        } else {
            log.debug("couldn't find bearer string, will ignore the header");
        }

        if (userContext != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("authorizated user '{}', setting security context", userContext.getUsername());
            Authentication authentication = new UserAuthentication(userContext);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
