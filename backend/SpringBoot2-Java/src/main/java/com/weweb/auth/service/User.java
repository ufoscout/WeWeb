package com.weweb.auth.service;

import java.util.List;
import lombok.Data;

@Data
public class User {

    private final Long id;
    private final String username;
    private final String encodedPassword;
    private final List<String> roles;

}
