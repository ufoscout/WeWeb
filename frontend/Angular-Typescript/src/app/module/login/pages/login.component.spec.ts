import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginModule } from '../';
import { LoginComponent } from './login.component';
import { NgxsModule } from '@ngxs/store';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe(`LoginComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                NgbModule.forRoot(),
                LoginModule,
                NgxsModule.forRoot(),
            ],
            providers: [
                NgbActiveModal,
                LoginComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([LoginComponent],
            (component: LoginComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
