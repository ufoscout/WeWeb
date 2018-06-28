import { LoginDto } from '../um/generated/dto';
import { AuthModel } from './auth.model';

export class Login {
    static type = '[Auth] Login';
    constructor(public readonly payload: LoginDto) { }
}

export class ResetState {
    static type = '[Auth] ResetSession';
}

export class Logout {
    static type = '[Auth] Logout';
}

export class SetToken {
    static type = '[Auth] Set Token';
    constructor(public readonly payload: string) { }
}

export class GetAuthData {
    static type = '[Auth] Get Auth data';
}

export class SetAuthData {
    static type = '[Auth] Set Auth data';
    constructor(public readonly payload: AuthModel) { }
}

export class SessionExpired {
    static type = '[Auth] SessionExpired';
}
