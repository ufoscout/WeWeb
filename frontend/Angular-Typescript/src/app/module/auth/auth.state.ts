import { State, Action, StateContext } from '@ngxs/store';
import * as events from './auth.events';
import { AuthService } from '../auth/auth.service';
import { catchError, map } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthModel } from './auth.model';
import * as JWT from 'jwt-decode';

export class AuthStateModel {
    tokenString = '';
    authModel: AuthModel = {
      id: 0,
      roles: [],
      username: ''
    }
    valid = false;
}

@State<AuthStateModel>({
  name: 'auth',
  defaults: new AuthStateModel(),
})
export class AuthState {

  constructor(private loginService: AuthService,
  private spinner: NgxSpinnerService) {}

  @Action(events.SetAuthData)
  setToken({ getState, setState }: StateContext<AuthStateModel>, {payload}: events.SetAuthData) {
    const state = getState();
    setState({
      ...state,
      authModel: JSON.parse(JWT<any>(payload.token).payload),
      tokenString: payload.token,
      valid: true
    });
  }

  @Action(events.Login)
  login(ctx: StateContext<AuthStateModel>, {payload}: events.Login) {
    this.spinner.show();
    return this.loginService.login(payload).pipe(
      map(response => {
        this.spinner.hide();
        ctx.dispatch(new events.SetAuthData(response));
      }),
      catchError(err => {
        this.spinner.hide();
        return throwError(err);
      })
     );
  }

  @Action(events.Logout)
  logout({ getState, setState }: StateContext<AuthStateModel>) {
        const state = getState();
        setState({
          ...state,
          authModel: {
            id: 0,
            roles: [],
            username: ''
          },
          tokenString: '',
          valid: false
        });
  }

}
