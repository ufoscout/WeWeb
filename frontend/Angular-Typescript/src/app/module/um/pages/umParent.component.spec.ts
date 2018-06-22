import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NgxsModule } from '@ngxs/store';
import { UmModule } from '..';
import { UmParentComponent } from './umParent.component';

describe(`UmParentComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                UmModule,
                NgxsModule.forRoot(),
            ],
            providers: [
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([UmParentComponent],
            (component: UmParentComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
