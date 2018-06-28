import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { CommonState, CommonStateModel } from './common.state';
import { SetLanguage } from './common.events';

describe('[Common] Common State', () => {
    let store: Store;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [NgxsModule.forRoot([CommonState])],
        }).compileComponents();
        store = TestBed.get(Store);
    }));

    it('it should set the current language', async(() => {
        const newLanguage = 'fr';
        store.dispatch(new SetLanguage({ language: newLanguage }));
        store.selectOnce(CommonState).subscribe((state: CommonStateModel) => {
            expect(state.language).toBe(newLanguage);
        });
    }));

});
