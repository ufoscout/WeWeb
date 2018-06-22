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
    username: string;
}
