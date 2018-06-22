import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CreateAccountComponent } from './createAccount.component';
import { NgxsModule } from '@ngxs/store';
import { UmModule } from '../..';

describe(`CreateAccountComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                UmModule,
                NgxsModule.forRoot(),
            ],
            providers: [
                CreateAccountComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([CreateAccountComponent],
            (component: CreateAccountComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
