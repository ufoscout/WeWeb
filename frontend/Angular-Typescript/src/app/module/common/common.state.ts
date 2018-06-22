import { State, Action, StateContext } from '@ngxs/store';
import * as events from './common.events';
import { defaultLanguage } from './common.constants';

export class CommonStateModel {
  allLanguages = ['en', 'it'];
  language = defaultLanguage;
}

@State<CommonStateModel>({
  name: 'common',
  defaults: new CommonStateModel(),
})
export class CommonState {

  @Action(events.SetLanguage)
  setToken({ getState, setState }: StateContext<CommonStateModel>, { payload }: events.SetLanguage) {
    const state = getState();
    setState({
      ...state,
      language: payload.language,
    });
  }

}
