import { TestBed, async, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HomeModule } from './';
import { HomeComponent } from './home.component';

describe(`HomeComponent`, () => {

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                HomeModule,
            ],
            providers: [
                HomeComponent,
            ],
        });
    });

    it(`should instantiate the component`, async(
        inject([HomeComponent],
            (component: HomeComponent) => {
                expect(component).not.toBeUndefined();
            })));

});
