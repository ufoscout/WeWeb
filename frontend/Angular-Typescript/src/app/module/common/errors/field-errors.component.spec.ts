import { TestBed, async, inject, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { CommonModule } from '../';
import { NgxsModule, Store } from '@ngxs/store';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Component } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';
import { FieldErrorsComponent } from './field-errors.component';

@Component({
    selector: `app-tester-component`,
    template: `
    <app-field-errors
        [errors]="errors"
        [translateKey]="key">
    </app-field-errors>`
})
class TesterComponent {

    errors?: String[];
    key?: String;

}

describe('FieldErrorsComponent', () => {
    let fixture: ComponentFixture<TesterComponent>;
    let component: TesterComponent;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                TranslateModule.forRoot(),
            ],
            providers: [
                FieldErrorsComponent,
            ],
            declarations: [
                FieldErrorsComponent,
                TesterComponent,
            ]
        }).compileComponents().then(() => {
            fixture = TestBed.createComponent(TesterComponent);
            component = fixture.componentInstance;
            fixture.detectChanges();
        });
    }));

    it('should be instanciated', async(() => {
        expect(component).not.toBeUndefined();
    }));

    it('should be empty if no errors', fakeAsync(() => {
        const children: HTMLCollection = fixture.nativeElement.querySelector('app-field-errors').children;
        expect(children.length).toBe(0);
    }));

    it('should show the errors', fakeAsync(() => {
        component.errors = ['one', 'two', 'three'];
        fixture.detectChanges();

        const children: HTMLCollection = fixture.nativeElement.querySelector('app-field-errors').children;
        console.log(children);

        expect(children.length).toBe(3);
        expect(children.item(0).textContent).toContain('errors.messages.one');
        expect(children.item(1).textContent).toContain('errors.messages.two');
        expect(children.item(2).textContent).toContain('errors.messages.three');
    }));

    it('should show the errors using a custom key', fakeAsync(() => {
        component.errors = ['one', 'two'];
        component.key = 'key.key-';
        fixture.detectChanges();

        const children: HTMLCollection = fixture.nativeElement.querySelector('app-field-errors').children;
        console.log(children);

        expect(children.length).toBe(2);
        expect(children.item(0).textContent).toContain('key.key-one');
        expect(children.item(1).textContent).toContain('key.key-two');
    }));

});
