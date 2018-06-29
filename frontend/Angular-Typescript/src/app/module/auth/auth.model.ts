export class AuthModel {
    id: number;
    username: string;
    roles: string[];
}

export class TokenModel {
    value = '';
    issuedAt = 0;
}
