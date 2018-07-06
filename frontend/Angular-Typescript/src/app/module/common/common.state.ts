import { State, Action, StateContext, NgxsOnInit } from '@ngxs/store';
import * as events from './common.events';
import { supportedLanguages, defaultLanguage, LanguageService } from './language.service';

export class CommonStateModel {
  allLanguages = supportedLanguages;
  language = defaultLanguage;
}

@State<CommonStateModel>({
  name: 'common',
  defaults: new CommonStateModel(),
})
export class CommonState implements NgxsOnInit {

  constructor(private languageService: LanguageService) {}

  ngxsOnInit(ctx: StateContext<CommonStateModel>) {
    ctx.dispatch(new events.SetLanguage({language: this.languageService.getLang()}));
  }

  @Action(events.SetLanguage)
  setToken({ getState, setState }: StateContext<CommonStateModel>, { payload }: events.SetLanguage) {
    const state = getState();
    setState({
      ...state,
      language: payload.language,
    });
    this.languageService.setLang(payload.language);
  }

}
