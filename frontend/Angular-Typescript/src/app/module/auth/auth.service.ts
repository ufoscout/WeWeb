import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timer } from 'rxjs';
import { LoginResponseDto, LoginDto } from '../um/generated/dto';
import { Select, Store } from '@ngxs/store';
import { AuthState, AuthStateModel } from './auth.state';
import * as str from '../shared/utils/string.utils';
import { RefreshToken } from './auth.events';

const TOKEN_KEY = 'TOKEN';
const ONE_MINUTE_MILLISECONDS = 1 * 60 * 1000;
const TEN_MINUTES_MILLISECONDS = 10 * 60 * 1000;

@Injectable()
export class AuthService implements OnInit {

    constructor(private http: HttpClient, private store: Store) { }

    ngOnInit() {
        timer(0, ONE_MINUTE_MILLISECONDS)
            .subscribe((val) => {
                this.checkIfTokenToBeRefreshed(TEN_MINUTES_MILLISECONDS);
            });
    }

    checkIfTokenToBeRefreshed(expirationMillis: number): boolean {
        const state = this.store.selectSnapshot<AuthStateModel>(s => s.auth);
        const now = new Date().getMilliseconds();
        const elapsed = now - state.token.issuedAt;
        if (state.valid && !str.isBlank(state.token.value) &&
            (elapsed >= expirationMillis)) {
            this.store.dispatch(new RefreshToken());
            return true;
        }
        return false;
    }

    login(loginDto: LoginDto): Observable<LoginResponseDto> {
        return this.http.post<LoginResponseDto>('/api/um/login', loginDto);
    }

    getAuthData(): Observable<LoginResponseDto> {
        return this.http.get<LoginResponseDto>('/api/um/current');
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
