import { AuthModel } from "../../auth/auth.model";

/* tslint:disable */

export interface CreateUserDto {
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
