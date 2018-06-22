import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateUserDto } from '../um/generated/dto';

@Injectable()
export class UmService {

    constructor(private http: HttpClient) {}

    createUser(dto: CreateUserDto): Observable<String> {
        return this.http.post<String>('/api/um/create', dto);
    }

}
