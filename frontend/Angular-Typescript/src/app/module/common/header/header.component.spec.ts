import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '../';
import { HeaderComponent } from './header.component';

describe(`HeaderComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                CommonModule,
            ],
            providers: [
                HeaderComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([HeaderComponent],
            (component: HeaderComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
