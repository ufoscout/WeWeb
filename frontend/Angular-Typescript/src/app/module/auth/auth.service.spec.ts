import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed, } from '@angular/core/testing';
import { SetAuthData, SetToken } from './auth.events';
import { AuthModule } from '.';
import { AuthService } from './auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('[Auth] AuthService', () => {
    let store: Store;
    let authService: AuthService;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                NgxsModule.forRoot(),
                AuthModule,
            ],
        }).compileComponents();
        store = TestBed.get(Store);
        authService = TestBed.get(AuthService);
    }));

    it('it should not refresh the token if not expired', async(() => {
        store.dispatch(new SetAuthData({
            id: 0,
            username: 'hello',
            roles: []
        }));
        store.dispatch(new SetToken('12345'));

        const storeSpy = spyOn(store, 'dispatch').and.callThrough();
        expect(authService.checkIfTokenToBeRefreshed(1000000)).toBeFalsy();
        expect(storeSpy).not.toHaveBeenCalled();
    }));

    it('it should refresh the token if expired', async(() => {
        store.dispatch(new SetAuthData({
            id: 0,
            username: 'hello',
            roles: []
        }));
        store.dispatch(new SetToken('12345'));

        const storeSpy = spyOn(store, 'dispatch').and.callThrough();
        expect(authService.checkIfTokenToBeRefreshed(0)).toBeTruthy();
        expect(storeSpy).toHaveBeenCalled();
    }));

    it('it should not refresh the token if not valid auth', async(() => {
        store.dispatch(new SetToken('12345'));

        const storeSpy = spyOn(store, 'dispatch').and.callThrough();
        expect(authService.checkIfTokenToBeRefreshed(0)).toBeFalsy();
        expect(storeSpy).not.toHaveBeenCalled();
    }));

    it('it should not refresh the token if not present', async(() => {
        store.dispatch(new SetAuthData({
            id: 0,
            username: 'hello',
            roles: []
        }));

        const storeSpy = spyOn(store, 'dispatch').and.callThrough();
        expect(authService.checkIfTokenToBeRefreshed(0)).toBeFalsy();
        expect(storeSpy).not.toHaveBeenCalled();
    }));

});
