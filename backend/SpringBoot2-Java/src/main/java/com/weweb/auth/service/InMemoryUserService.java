package com.weweb.auth.service;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ufoscout.coreutils.validation.ValidatorService;
import com.weweb.auth.dto.CreateLoginDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserService implements UserService {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final PasswordEncoder crypt;
    private final ValidatorService validatorService;

    public InMemoryUserService(PasswordEncoder crypt,
                               ValidatorService validatorService) {
        this.crypt = crypt;
        this.validatorService = validatorService;
        users.put("user", new User(0L, "user", crypt.encode("user"), Arrays.asList("USER")));
        users.put("admin", new User(1L,"admin", crypt.encode("admin"), Arrays.asList("USER", "ADMIN")));
    }

    @Override
    public User login(String username, String password) {
        User user = users.get(username);

        if (user == null || !crypt.matches(password, user.getEncodedPassword())) {
            throw new BadCredentialsException("");
        }

        return user;

    }

    @Override
    public void createUser(CreateLoginDto dto) {

        validatorService.<CreateLoginDto>validator()
                .add("username", "notUnique", (bean) -> !users.containsKey(bean.getEmail()))
                .add("confirmPassword", "notSame", (bean) -> bean.getPassword().equals(bean.getPasswordConfirm()))
                .build()
                .validateThrowException(dto);

        User user = new User( users.size()+1L, dto.getEmail(), crypt.encode(dto.getPassword()), Arrays.asList("USER"));
        users.put(user.getUsername(), user);
    }

}
