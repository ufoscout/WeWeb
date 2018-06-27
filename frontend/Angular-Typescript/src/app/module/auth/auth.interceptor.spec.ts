import { TestBed } from '@angular/core/testing';
import {
    HttpClientTestingModule,
    HttpTestingController,
} from '@angular/common/http/testing';
import { AuthInterceptor } from './auth.interceptor';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { Store, NgxsModule } from '@ngxs/store';
import { AuthState } from './auth.state';
import { SetAuthData } from './auth.events';
import { AuthService } from './auth.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { LoginResponseDto } from '../um/generated/dto';

describe(`[Auth] AuthInterceptor`, () => {

    let store: Store;
    let httpMock: HttpTestingController;
    let httpClient: HttpClient;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                NgxsModule.forRoot([AuthState]),
            ],
            providers: [
                AuthService,
                NgxSpinnerService,
                {
                    provide: HTTP_INTERCEPTORS,
                    useClass: AuthInterceptor,
                    multi: true,
                },
            ],
        });
        httpMock = TestBed.get(HttpTestingController);
        httpClient = TestBed.get(HttpClient);
        store = TestBed.get(Store);
    });

    it('should add an Authorization header', () => {

        let dto: LoginResponseDto = {
            token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicGF5bG9hZCI6IntcImlkXCI6MCxcInVzZXJuYW1lXCI6XCJ1c2VyXCIsXCJyb2xlc1wiOjJ9IiwiaWF0IjoxNTMwMTE5NjEzLCJleHAiOjE1MzAxMTk2NzN9.1JpBKpo0KpRGob6gpr-KUD67LPvJLMQL61-SwMJ6Foura5Ssdlo8zPNmH8wOjoFArNaTZq7xgShGPJfuv7EFww',
            username: ''
        }
        store.dispatch(new SetAuthData(dto));

        httpClient.get('/url').subscribe();
        const httpRequest = httpMock.expectOne(`/url`);

        expect(httpRequest.request.headers.get('Authorization')).toBe(`Bearer ${dto.token}`);
    });

    it('should not add an Authorization header if the token is not present', () => {
        httpClient.get('/url').subscribe();
        const httpRequest = httpMock.expectOne(`/url`);
        expect(httpRequest.request.headers.get('Authorization')).toBeNull();
    });

});
