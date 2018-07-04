import { State, Action, StateContext } from '@ngxs/store';
import { UmService } from './um.service';
import { ResetState } from '../auth/auth.events';

export class UmStateModel {

}

@State<UmStateModel>({
  name: 'um',
  defaults: new UmStateModel(),
})
export class UmState {

  constructor(private umService: UmService) { }

  @Action(ResetState)
  resetSession({ getState, setState }: StateContext<UmStateModel>) {
    setState(new UmStateModel());
  }
}
