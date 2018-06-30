export class AuthModel {
    id: number;
    username: string;
    roles: string[];
    properties?: { [index: string]: any };
}

export class TokenModel {
    value = '';
    issuedAt = 0;
}
