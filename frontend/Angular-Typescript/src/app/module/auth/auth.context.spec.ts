import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { SetAuthData } from './auth.events';
import { AuthModule } from '.';
import { AuthContext } from './auth.context';
import { AuthModel } from './auth.model';

describe('[Auth] Auth Context', () => {
    let store: Store;
    let auth: AuthContext;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                NgxsModule.forRoot(),
                AuthModule,
            ],
        }).compileComponents();
        store = TestBed.get(Store);
        auth = TestBed.get(AuthContext);
    }));

    it('isAuthenticated should be false if no AuthModel', async(() => {
        expect(auth.isAuthenticated()).toBeFalsy();
    }));

    it('isAuthenticated should be false if no valid AuthModel', async(() => {

        const dto: AuthModel = {
            id: 1,
            username: '',
            roles: []
        };
        store.dispatch(new SetAuthData(dto));

        expect(auth.isAuthenticated()).toBeFalsy();

    }));

    it('isAuthenticated should be true if valid AuthModel', async(() => {

        const dto: AuthModel = {
            id: 1,
            username: 'user',
            roles: []
        };
        store.dispatch(new SetAuthData(dto));

        expect(auth.isAuthenticated()).toBeTruthy();

    }));

    it('it should not have role if not isAuthenticated', async(() => {

        const dto: AuthModel = {
            id: 1,
            username: '',
            roles: ['one']
        };
        store.dispatch(new SetAuthData(dto));

        expect(auth.hasRole('one')).toBeFalsy();

    }));

    it('it should not have role', async(() => {

        const dto: AuthModel = {
            id: 1,
            username: 'user',
            roles: ['one']
        };
        store.dispatch(new SetAuthData(dto));

        expect(auth.hasRole('one')).toBeTruthy();

    }));

});
