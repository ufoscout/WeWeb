import { State, Action, StateContext } from '@ngxs/store';
import { UmService } from './um.service';
import * as events from './um.events';

export class UmStateModel {

}

@State<UmStateModel>({
  name: 'um',
  defaults: new UmStateModel(),
})
export class UmState {

  constructor(private umService: UmService) { }

  @Action(events.CreateUser)
  login(ctx: StateContext<UmStateModel>, {payload}: events.CreateUser) {
    return this.umService.createUser(payload);
  }

}
