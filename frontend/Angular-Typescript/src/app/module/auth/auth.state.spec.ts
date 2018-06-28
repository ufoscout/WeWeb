import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { AuthState, AuthStateModel } from './auth.state';
import { SetAuthData } from './auth.events';
import { AuthModule } from '.';
import { AuthModel } from './auth.model';

describe('[Auth] Auth State', () => {
    let store: Store;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                NgxsModule.forRoot(),
                AuthModule,
            ],
        }).compileComponents();
        store = TestBed.get(Store);
    }));

    it('it should set username and password', () => {

        const dto: AuthModel = {
                id: 1,
                username: 'user-' + new Date().getMilliseconds(),
                roles: []
        };

        store.dispatch(new SetAuthData(dto));
        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.authModel.username).toBe(dto.username);
        });
    });

});
