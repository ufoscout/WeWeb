import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '..';
import { NgxsModule } from '@ngxs/store';
import { ConfirmComponent } from './confirm.component';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe(`ConfirmComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                CommonModule,
                NgbModule.forRoot(),
                NgxsModule.forRoot(),
            ],
            providers: [
                NgbActiveModal,
                ConfirmComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([ConfirmComponent],
            (component: ConfirmComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
