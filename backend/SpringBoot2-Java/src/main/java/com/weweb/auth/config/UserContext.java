package com.weweb.auth.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    private String username = "";
    private String[] roles = new String[0];

}
