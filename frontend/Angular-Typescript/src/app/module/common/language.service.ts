import { Select } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { CommonStateModel, CommonState } from './common.state';
import { defaultLanguage } from './common.constants';

@Injectable()
export class LanguageService {

    @Select(CommonState) commonState$: Observable<CommonStateModel>;

    constructor(private translate: TranslateService) {
        translate.setDefaultLang(defaultLanguage);
        translate.use(defaultLanguage);
        this.commonState$.subscribe(state => {
            translate.use(state.language);
        });
    }

}
