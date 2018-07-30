import { TestBed, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { ConfirmationService } from './confirmation.service';
import { CommonModule } from '.';
import { CommonState } from './common.state';
import { NgxsModule } from '@ngxs/store';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: `app-tester-component`,
    template: `
    <div>
    </div>`
})
class TesterComponent {
    constructor(public confirmService: ConfirmationService) {
    }
}

describe('ConfirmationService', () => {
    let fixture: ComponentFixture<TesterComponent>;
    let component: TesterComponent;

    beforeEach(fakeAsync(() => {
        TestBed.configureTestingModule({
            imports: [
                CommonModule,
                NgbModule.forRoot(),
                NgxsModule.forRoot([CommonState]),
                TranslateModule.forRoot(),
            ],
            providers: [
                NgbActiveModal,
                TesterComponent,
            ],
            declarations: [
                TesterComponent,
            ]
        }).compileComponents().then(() => {
            fixture = TestBed.createComponent(TesterComponent);
            component = fixture.componentInstance;
            fixture.detectChanges();
        });
        removeModal();
    }));

    afterEach(fakeAsync(() => {
        removeModal();
    }));

    it('should be instanciated', fakeAsync(() => {
        expect(component).not.toBeUndefined();
    }));

    it('should open the modal', fakeAsync(() => {

        expect(getModal()).toBeNull();

        component.confirmService.openConfirm({
            confirm: () => { }
        });
        tick();

        expect(getModal()).not.toBeNull();
    }));

    it('should trigger the confirm callback on button click', fakeAsync(() => {

        const confirm = jasmine.createSpy();
        const cancel = jasmine.createSpy();
        component.confirmService.openConfirm({
            confirm: confirm,
            cancel: cancel
        });
        tick();

        getConfirmButton().click();
        tick();

        expect(confirm).toHaveBeenCalled();
        expect(cancel).not.toHaveBeenCalled();
        expect(getModal()).toBeNull();

    }));

    it('should trigger the cancel callback on button click', fakeAsync(() => {

        const confirm = jasmine.createSpy();
        const cancel = jasmine.createSpy();
        component.confirmService.openConfirm({
            confirm: confirm,
            cancel: cancel
        });
        tick();

        getCancelButton().click();
        tick();

        expect(confirm).not.toHaveBeenCalled();
        expect(cancel).toHaveBeenCalled();
        expect(getModal()).toBeNull();
    }));

    it('should trigger the cancel callback on window click', fakeAsync(() => {

        const confirm = jasmine.createSpy();
        const cancel = jasmine.createSpy();
        component.confirmService.openConfirm({
            confirm: confirm,
            cancel: cancel
        });
        tick();

        (document.querySelector('ngb-modal-window') as HTMLElement).click();
        tick();

        expect(confirm).not.toHaveBeenCalled();
        expect(cancel).toHaveBeenCalled();
        expect(getModal()).toBeNull();
    }));

    /*
    it('should display header and body text', fakeAsync(() => {

        const titleText = 'title-' + new Date().getTime();
        const bodyText = 'body-' + new Date().getTime();
        component.alertService.openConfirm({
            titleText: titleText,
            bodyText: bodyText,
            confirm: () => {}
        });
        tick();

        console.log(getHeader());
        console.log(getBody());
        expect(getHeader().innerText).toBe(titleText);
        expect(getBody().innerText).toBe(bodyText);
    }));
    */
});

function getModal<E extends HTMLElement>(): E | null {
    return document.querySelector('.modal-content');
}

function getHeader<E extends HTMLElement>(): E {
    return document.querySelector('.modal-header') as E;
}

function getBody<E extends HTMLElement>(): E {
    return document.querySelector('.modal-body') as E;
}

function getConfirmButton<E extends HTMLElement>(): E {
    return document.querySelectorAll('.modal-content .modal-footer button')[0] as E;
}

function getCancelButton<E extends HTMLElement>(): E {
    return document.querySelectorAll('.modal-content .modal-footer button')[1] as E;
}

function removeModal() {

    const remainingModalWindows = document.querySelectorAll('ngb-modal-window');

    for (let i = 0; i < remainingModalWindows.length; i++) {
        const elem = remainingModalWindows[i];
        (elem.parentNode as Node).removeChild(elem);
    }

    const remainingModalBackdrops = document.querySelectorAll('ngb-modal-backdrop');
    for (let i = 0; i < remainingModalBackdrops.length; i++) {
        const elem = remainingModalBackdrops[i];
        (elem.parentNode as Node).removeChild(elem);
    }

    tick();

}
