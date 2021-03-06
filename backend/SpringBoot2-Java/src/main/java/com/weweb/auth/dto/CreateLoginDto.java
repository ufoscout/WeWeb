package com.weweb.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoginDto {
    private String email;
    private String password;
    private String passwordConfirm;
}
