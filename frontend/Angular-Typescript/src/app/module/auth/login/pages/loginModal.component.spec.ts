import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthLoginModule } from '../';
import { NgxsModule } from '@ngxs/store';
import { LoginModalComponent } from './loginModal.component';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe(`LoginModalComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                AuthLoginModule,
                NgbModule.forRoot(),
                NgxsModule.forRoot(),
            ],
            providers: [
                NgbActiveModal,
                LoginModalComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([LoginModalComponent],
            (component: LoginModalComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
