import { Store, NgxsModule } from '@ngxs/store';
import { async, TestBed } from '@angular/core/testing';
import { CommonState, CommonStateModel } from './common.state';
import { SetLanguage } from './common.events';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from './language.service';

class MockTranslateService {

    defaultLang: String;
    lang: String;

    setDefaultLang(language: String) {
        this.defaultLang = language;
    }
    use(language: String): any {
        this.lang = language;
    }
  }

describe('[Common] LanguageService', () => {
    let store: Store;
    let translateService: MockTranslateService;
    let languageService: LanguageService;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [
                NgxsModule.forRoot([CommonState])
            ],
            providers: [
                LanguageService,
                {provide: TranslateService, useClass: MockTranslateService}
            ]
        }).compileComponents();
        store = TestBed.get(Store);
        translateService = TestBed.get(TranslateService);
        languageService = TestBed.get(LanguageService);
    }));

    it('it should set the current language', () => {

        expect(languageService).not.toBeNull();

        const newLanguage = 'fr';
        store.dispatch(new SetLanguage({ language: newLanguage }));
        store.selectOnce(CommonState).subscribe((state: CommonStateModel) => {
            expect(state.language).toBe(newLanguage);
        });
        expect(translateService.lang).toBe(newLanguage);
    });

});
