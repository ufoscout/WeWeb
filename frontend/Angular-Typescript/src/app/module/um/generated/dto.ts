import { AuthModel } from '../../auth/auth.model';

/* tslint:disable */

export interface CreateLoginDto {
    email: string;
    password: string;
    passwordConfirm: string;
}

export interface LoginDto {
    username: string;
    password: string;
}

export interface LoginResponseDto {
    token: string;
    auth: AuthModel;
}

export interface TokenResponseDto {
    token: string;
}
