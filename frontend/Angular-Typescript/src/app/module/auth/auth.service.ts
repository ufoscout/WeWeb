import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponseDto, LoginDto } from '../um/generated/dto';
import { AuthModel } from './auth.model';

const TOKEN_KEY = 'TOKEN'

@Injectable()
export class AuthService {


    constructor(private http: HttpClient) {}

    login(loginDto: LoginDto): Observable<LoginResponseDto> {
        return this.http.post<LoginResponseDto>('/api/um/login', loginDto);
    }

    getAuthData(): Observable<LoginResponseDto> {
        return this.http.get<LoginResponseDto>('/api/um/current');
    }

    getToken(): string {
        return localStorage.getItem(TOKEN_KEY)
    }

    setToken(token: string): void {
        localStorage.setItem(TOKEN_KEY, token)
    }

    removeToken(): void {
        localStorage.removeItem(TOKEN_KEY)
    }
}
