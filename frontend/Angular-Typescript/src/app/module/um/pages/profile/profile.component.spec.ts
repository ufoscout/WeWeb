import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ProfileComponent } from './profile.component';
import { NgxsModule } from '@ngxs/store';
import { UmModule } from '../..';

describe(`ProfileComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                UmModule,
                NgxsModule.forRoot(),
            ],
            providers: [
                ProfileComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([ProfileComponent],
            (component: ProfileComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
