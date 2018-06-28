import { TestBed, async, ComponentFixture, fakeAsync } from '@angular/core/testing';
import { CommonModule } from '../';
import { NgxsModule } from '@ngxs/store';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Component } from '@angular/core';

@Component({
    selector: `app-tester-component`,
    template: `
    <app-field-errors
        [errors]="errors"
        [translateKey]="key">
    </app-field-errors>`
})
class TesterComponent {

    errors: String[];
    key: String;

}

describe('FieldErrorsComponent', () => {
    let fixture: ComponentFixture<TesterComponent>;
    let component: TesterComponent;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                NgbModule.forRoot(),
                NgxsModule.forRoot([]),
                CommonModule,
            ],
            declarations: [
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


    /*
    it('should list all languages', fakeAsync(() => {
        fixture.detectChanges();
        const state = store.selectSnapshot<CommonStateModel>(s => s.common);
        const languages: HTMLCollection = fixture.debugElement.nativeElement.querySelector('#dropdownLanguages').children;

        expect(languages.length).toBe(state.allLanguages.length);
        for (let i = 0; i < state.allLanguages.length; i++) {
            // console.log(languages.item(i));
            expect(languages.item(i).textContent.trim()).toBe(state.allLanguages[i]);
        }

    }));

    it('should change the selected language', fakeAsync(() => {
        fixture.detectChanges();
        spyOn(component, 'setLanguage').and.callThrough();

        let state = store.selectSnapshot<CommonStateModel>(s => s.common);

        // Select a language different than the current one
        const previouslySelectedLanguage = state.language;
        let notDefaultLanguageIndex = 0;
        for (let i = 0; i < state.allLanguages.length; i++) {
            if (previouslySelectedLanguage !== state.allLanguages[i]) {
                notDefaultLanguageIndex = i;
            }
        }

        // click on the dropdown entry for the new language
        const languages: HTMLCollection = fixture.debugElement.nativeElement.querySelector('#dropdownLanguages').children;
        (<any>languages.item(notDefaultLanguageIndex)).click();

        tick();
        fixture.detectChanges();

        expect(component.setLanguage).toHaveBeenCalled();

        // Check the store is updated
        state = store.selectSnapshot<CommonStateModel>(s => s.common);
        expect(state.language).toBe(state.allLanguages[notDefaultLanguageIndex]);

        // Check the new language is correctly displayed
        const selectedLanguage = fixture.debugElement.nativeElement.querySelector('#dropdownLanguage');
        expect(selectedLanguage.textContent.trim()).not.toBe(previouslySelectedLanguage);
        expect(selectedLanguage.textContent.trim()).toBe(state.allLanguages[notDefaultLanguageIndex]);
    }));
*/
});
