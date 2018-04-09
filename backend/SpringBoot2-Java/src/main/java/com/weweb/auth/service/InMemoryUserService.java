package com.weweb.auth.service;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserService {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final PasswordEncoder crypt;

    public InMemoryUserService(PasswordEncoder crypt) {
        this.crypt = crypt;
        users.put("user", new User("user", crypt.encode("user"), Arrays.asList("USER")));
        users.put("admin", new User("admin", crypt.encode("admin"), Arrays.asList("USER", "ADMIN")));
    }

    public User login(String username, String password) throws UsernameNotFoundException {
        User user = users.get(username);

        if (user == null || !crypt.matches(password, user.getEncodedPassword())) {
            throw new BadCredentialsException("");
        }

        return user;

    }

}
