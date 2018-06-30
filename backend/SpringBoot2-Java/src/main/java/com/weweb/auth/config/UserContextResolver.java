package com.weweb.auth.config;

import com.ufoscout.coreutils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * A Spring resolver that permits injection of {@link Auth} beans into web controllers. The {@link Auth}
 * is retrieved from request attributes and it is set by the {@link JwtAuthorizationTokenFilter}.
 *
 * @author Francesco Cina'
 */
@Slf4j
public class UserContextResolver implements HandlerMethodArgumentResolver {

    private final String userContextAttributeKey;
    private final Auth emptyUserContext = new Auth();

    UserContextResolver(String userContextAttributeKey) {
        this.userContextAttributeKey = userContextAttributeKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object userContext = nativeWebRequest.getAttribute(userContextAttributeKey, NativeWebRequest.SCOPE_REQUEST);
        return userContext != null ? userContext : emptyUserContext;
    }
}
