import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginResponseDto, LoginDto } from '../um/generated/dto';

@Injectable()
export class AuthService {

    constructor(private http: HttpClient) {}

    login(loginDto: LoginDto): Observable<LoginResponseDto> {
        return this.http.post<LoginResponseDto>('/api/um/login', loginDto);
    }

}
