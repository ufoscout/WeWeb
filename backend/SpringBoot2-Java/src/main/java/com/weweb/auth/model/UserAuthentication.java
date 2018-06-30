package com.weweb.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ufoscout.coreutils.auth.Auth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserAuthentication implements Authentication {

    private static String ROLE_PREFIX = "ROLE_";

    private static List<GrantedAuthority> mapAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        }
        return authorities;
    }


    private final Auth userContext;
    private final List<GrantedAuthority> authorities;

    public UserAuthentication(Auth userContext) {
        this.userContext = userContext;
        authorities = mapAuthorities(userContext.getRoles());
    }

    @Override
    public String getName() {
        return userContext.getUsername();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getCredentials() {
        return "";
    }

    @Override
    public Auth getDetails() {
        return userContext;
    }

    @Override
    public String getPrincipal() {
        return userContext.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return !userContext.getUsername().isEmpty();
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    }

}
