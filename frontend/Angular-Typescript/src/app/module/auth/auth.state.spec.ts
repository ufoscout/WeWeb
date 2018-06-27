import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { AuthState, AuthStateModel } from './auth.state';
import { SetAuthData } from './auth.events';
import { AuthModule } from '.';
import { LoginResponseDto } from '../um/generated/dto';

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

        let dto: LoginResponseDto = {
            token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwicGF5bG9hZCI6IntcImlkXCI6MCxcInVzZXJuYW1lXCI6XCJ1c2VyXCIsXCJyb2xlc1wiOjJ9IiwiaWF0IjoxNTMwMTE5NjEzLCJleHAiOjE1MzAxMTk2NzN9.1JpBKpo0KpRGob6gpr-KUD67LPvJLMQL61-SwMJ6Foura5Ssdlo8zPNmH8wOjoFArNaTZq7xgShGPJfuv7EFww',
            username: ''
        }

        store.dispatch(new SetAuthData(dto));
        store.selectOnce(AuthState).subscribe((state: AuthStateModel) => {
            expect(state.authModel.username).toBe('user');
            expect(state.tokenString).toBe(dto.token);
        });
    });

});
