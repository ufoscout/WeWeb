import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { AuthState, AuthStateModel } from './auth.state';
import { SetAuthData, Logout, Login, SetToken, RefreshToken, CreateLogin } from './auth.events';
import { AuthModule } from '.';
import { AuthModel } from './auth.model';
import { AuthService } from './auth.service';
import { LoginResponseDto, TokenResponseDto, CreateLoginDto } from '../um/generated/dto';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('[Auth] Auth State', () => {
    let store: Store;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                NgxsModule.forRoot(),
                AuthModule,
            ],
        }).compileComponents();
        store = TestBed.get(Store);

    }));

    it('it should set the AuthModel', async(() => {
        const dto: AuthModel = {
            id: 1,
            username: 'user-' + new Date().getTime(),
            roles: []
        };

        store.dispatch(new SetAuthData(dto));
        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.authModel.username).toBe(dto.username);
        });
    }));

    it('Logout should dispatch a ResetState action', async(() => {

        const dto: AuthModel = {
            id: 1,
            username: 'user-' + new Date().getTime(),
            roles: []
        };
        store.dispatch(new SetAuthData(dto));
        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.valid).toBeTruthy();
        });

        const authService: AuthService = TestBed.get(AuthService);
        const authServiceSpy = spyOn(authService, 'removeToken').and.callThrough();
        const authState: AuthState = TestBed.get(AuthState);
        const authStateSpy = spyOn(authState, 'resetSession').and.callThrough();

        store.dispatch(new Logout());
        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.authModel.username).toBe('');
            expect(state.valid).toBeFalsy();
        });
        expect(authServiceSpy).toHaveBeenCalled();
        expect(authStateSpy).toHaveBeenCalled();
    }));

    it('Login should ResetState and Set AuthModel and token', async(() => {

        const response: LoginResponseDto = {
            auth: {
                id: 12,
                username: 'user-' + new Date().getTime(),
                roles: []
            },
            token: 'token-' + new Date().getTime()
        };

        const authService: AuthService = TestBed.get(AuthService);
        const authServiceSpy = spyOn(authService, 'login').and.returnValue(of(response));
        const authState: AuthState = TestBed.get(AuthState);
        const authStateSpy = spyOn(authState, 'resetSession').and.callThrough();

        const dto = new Login({
            username: response.auth.username,
            password: ''
        });

        store.dispatch(dto);

        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.valid).toBeTruthy();
            expect(state.authModel.username).toBe(response.auth.username);
        });

        expect(authService.getToken()).toBe(response.token);
        expect(authServiceSpy).toHaveBeenCalled();
        expect(authStateSpy).toHaveBeenCalled();
    }));

    it('Login should not ResetState if user was already logged in', async(() => {

        const response: LoginResponseDto = {
            auth: {
                id: 12,
                username: 'user-' + new Date().getTime(),
                roles: []
            },
            token: 'token-' + new Date().getTime()
        };

        store.dispatch(new SetAuthData(response.auth));

        const authService: AuthService = TestBed.get(AuthService);
        const authServiceSpy = spyOn(authService, 'login').and.returnValue(of(response));
        const authState: AuthState = TestBed.get(AuthState);
        const authStateSpy = spyOn(authState, 'resetSession').and.callThrough();

        store.dispatch(new Login({
            username: response.auth.username,
            password: ''
        }));

        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.valid).toBeTruthy();
            expect(state.authModel.username).toBe(response.auth.username);
        });

        expect(authService.getToken()).toBe(response.token);
        expect(authServiceSpy).toHaveBeenCalled();
        expect(authStateSpy).not.toHaveBeenCalled();
    }));

    it('Set token should call the AuthService', async(() => {

        const token =  'token-' + new Date().getTime();

        const authService: AuthService = TestBed.get(AuthService);
        const authServiceSpy = spyOn(authService, 'setToken').and.callThrough();

        store.dispatch(new SetToken(token));

        expect(authService.getToken()).toBe(token);
        expect(authServiceSpy).toHaveBeenCalled();
    }));

    it('Set token issuedAt timestamp', async(() => {

        const before = new Date().getTime();
        const token = 'token-' + new Date().getTime();

        store.dispatch(new SetToken(token));
        const after = new Date().getTime();

        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.token.value).toBe(token);
            expect(state.token.issuedAt).toBeGreaterThanOrEqual(before);
            expect(state.token.issuedAt).toBeLessThanOrEqual(after);
        });
    }));

    it('Should call RefreshToken', async(() => {

        const response: TokenResponseDto = {
            token: 'token-' + new Date().getTime()
        };

        const authService: AuthService = TestBed.get(AuthService);
        const authServiceSpy = spyOn(authService, 'refreshToken').and.returnValue(of(response));

        store.dispatch(new RefreshToken());

        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.token.value).toBe(response.token);
        });

        expect(authService.getToken()).toBe(response.token);
        expect(authServiceSpy).toHaveBeenCalled();
    }));

    it('it should call the createLogin service method', () => {
        const authService: AuthService = TestBed.get(AuthService);
        const createUserSpy = spyOn(authService, 'createLogin').and.callThrough();

        const dto: CreateLoginDto = { email: '', password: '', passwordConfirm: '' };
        store.dispatch( new CreateLogin(dto) );

        expect(createUserSpy).toHaveBeenCalledWith(dto);
    });

});
