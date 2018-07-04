package com.weweb.auth.service;

import com.weweb.auth.dto.CreateLoginDto;

public interface UserService {

    User login(String username, String password);

    void createUser(CreateLoginDto dto);
}
