package com.weweb.auth.service;

import com.weweb.auth.dto.CreateUserDto;

public interface UserService {

    User login(String username, String password);

    void createUser(CreateUserDto dto);
}
