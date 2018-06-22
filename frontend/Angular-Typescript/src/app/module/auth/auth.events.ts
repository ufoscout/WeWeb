import { LoginDto, LoginResponseDto } from '../um/generated/dto';

export class Login {
    static type = '[Auth] Login';
    constructor(public readonly payload: LoginDto) { }
}

export class Logout {
    static type = '[Auth] Logout';
}

export class SetAuthData {
    static type = '[Auth] Set Auth data';
    constructor(public readonly payload: LoginResponseDto) { }
}