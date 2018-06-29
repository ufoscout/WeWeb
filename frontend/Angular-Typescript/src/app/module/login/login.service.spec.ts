import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { LoginService } from './login.service';
import { LoginModule } from '.';
import { NgbModule, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SessionExpired, SetAuthData } from '../auth/auth.events';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';

describe('[Common] LoginService', () => {
    let store: Store;
    let loginService: LoginService;
    let modalService: NgbModal;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                NgxsModule.forRoot(),
                TranslateModule.forRoot(),
                LoginModule,
                NgbModule.forRoot(),
            ],
            providers: [
                NgbActiveModal,
                LoginService,
            ]
        }).compileComponents();
        store = TestBed.get(Store);
        modalService = TestBed.get(NgbModal);
        loginService = TestBed.get(LoginService);
    }));

    it('it should be defined', async(() => {
        expect(loginService).not.toBeNull();
    }));

    it('it should open the modal once', async(() => {
        loginService.previousForceReLogin = false;

        const modalSpy = spyOn(modalService, 'open').and.callThrough();
        store.dispatch(new SessionExpired());

        expect(loginService.previousForceReLogin).toBeTruthy();
        expect(modalSpy).toHaveBeenCalledTimes(1);

        store.dispatch(new SessionExpired());
        store.dispatch(new SessionExpired());

        expect(modalSpy).toHaveBeenCalledTimes(1);
    }));

    it('SetAuthData should reset forceReLogin status', async(() => {
        loginService.previousForceReLogin = false;

        store.dispatch(new SessionExpired());
        expect(loginService.previousForceReLogin).toBeTruthy();

        store.dispatch(new SetAuthData({
            id: 0,
            username: '',
            roles: []
        }));
        expect(loginService.previousForceReLogin).toBeFalsy();

    }));

});
