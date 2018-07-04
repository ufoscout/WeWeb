import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponseDto, LoginDto, TokenResponseDto, CreateLoginDto } from '../um/generated/dto';
import { Store } from '@ngxs/store';
import { AuthStateModel } from './auth.state';
import * as str from '../shared/utils/string.utils';
import { RefreshToken } from './auth.events';

const TOKEN_KEY = 'TOKEN';

@Injectable()
export class AuthService {

    constructor(private http: HttpClient, private store: Store) { }

    checkIfTokenToBeRefreshed(expirationMillis: number): boolean {
        console.log('[AuthService] checkIfTokenToBeRefreshed');
        const state = this.store.selectSnapshot<AuthStateModel>(s => s.auth);
        const now = new Date().getTime();
        const elapsed = now - state.token.issuedAt;
        if (state.valid && !str.isBlank(state.token.value) &&
            (elapsed >= expirationMillis)) {
                console.log('[AuthService] Dispatch RefreshToken');
            this.store.dispatch(new RefreshToken());
            return true;
        }
        return false;
    }

    login(loginDto: LoginDto): Observable<LoginResponseDto> {
        return this.http.post<LoginResponseDto>('/api/auth/login', loginDto);
    }

    createLogin(dto: CreateLoginDto): Observable<String> {
        return this.http.post<String>('/api/auth/create', dto);
    }


    getAuthData(): Observable<LoginResponseDto> {
        return this.http.get<LoginResponseDto>('/api/auth/current');
    }

    refreshToken(): Observable<TokenResponseDto> {
        return this.http.get<TokenResponseDto>('/api/auth/token/refresh');
    }

    getToken(): string {
        return localStorage.getItem(TOKEN_KEY);
    }

    setToken(token: string): void {
        localStorage.setItem(TOKEN_KEY, token);
    }

    removeToken(): void {
        localStorage.removeItem(TOKEN_KEY);
    }

}
