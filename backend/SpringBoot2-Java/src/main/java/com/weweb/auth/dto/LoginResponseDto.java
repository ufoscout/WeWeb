package com.weweb.auth.dto;

import com.ufoscout.coreutils.auth.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private Auth auth;

}
