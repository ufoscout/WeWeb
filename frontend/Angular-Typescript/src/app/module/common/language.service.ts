import { Injectable, ApplicationRef } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import * as obj from '../shared/utils/object.utils';
import * as brw from '../shared/utils/browser.utils';

const LANG_KEY = 'LANG';
export const defaultLanguage = 'en';
export const supportedLanguages = ['en', 'it'];

@Injectable()
export class LanguageService {

    constructor(private translate: TranslateService, private ref: ApplicationRef) {
        translate.setDefaultLang(defaultLanguage);
    }

    getLang(): string {
        let lang = localStorage.getItem(LANG_KEY);
        if (!obj.exists(lang)) {
            lang = brw.getPreferredSupportedLang(supportedLanguages, defaultLanguage);
        }
        return lang;
    }

    setLang(lang: string): void {
        localStorage.setItem(LANG_KEY, lang);
        this.translate.use(lang).subscribe(
           () => {
               this.ref.tick(); // force refresh or UI is not refreshed. See https://github.com/ngx-translate/core/issues/587
            });
    }

    removeLang(): void {
        localStorage.removeItem(LANG_KEY);
    }

}
