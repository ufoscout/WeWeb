import { TestBed } from '@angular/core/testing';
import {
    HttpClientTestingModule,
    HttpTestingController,
} from '@angular/common/http/testing';
import { AuthInterceptor } from './auth.interceptor';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { Store, NgxsModule } from '@ngxs/store';
import { AuthState } from './auth.state';
import { SetToken } from './auth.events';
import { AuthService } from './auth.service';
import { NgxSpinnerService } from 'ngx-spinner';

describe(`[Auth] AuthInterceptor`, () => {

    let store: Store;
    let httpMock: HttpTestingController;
    let httpClient: HttpClient;
    let authService: AuthService;

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
        authService = TestBed.get(AuthService);
        authService.removeToken();
    });

    it('should add an Authorization header', () => {

        const token = 'eyJhb';

        store.dispatch(new SetToken(token));

        httpClient.get('/url').subscribe();
        const httpRequest = httpMock.expectOne(`/url`);

        expect(httpRequest.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
    });

    it('should not add an Authorization header if the token is not present', () => {

        httpClient.get('/url').subscribe();
        const httpRequest = httpMock.expectOne(`/url`);
        expect(httpRequest.request.headers.get('Authorization')).toBeNull();
    });

    it('should dispatch SessionExpired action if 401 TokenExpired', function () {
        httpClient.get('/api').subscribe();
        httpMock.expectOne('/api').flush({message: 'TokenExpired'}, { status: 401, statusText: 'error' });

        // TODO: verify that Session expired is dispatched
    });

});
