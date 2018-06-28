package com.weweb.auth.config;

import com.ufoscout.coreutils.jwt.JwtService;
import com.ufoscout.coreutils.jwt.TokenExpiredException;
import com.weweb.auth.model.UserAuthentication;
import com.weweb.auth.model.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        if (requestHeader != null && requestHeader.startsWith(AuthConfig.JWT_TOKEN_HEADER_SUFFIX)) {
            String authToken = requestHeader.substring(AuthConfig.JWT_TOKEN_HEADER_SUFFIX.length());
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
