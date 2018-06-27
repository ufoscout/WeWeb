import { State, Action, StateContext, NgxsOnInit } from '@ngxs/store';
import * as events from './auth.events';
import { AuthService } from '../auth/auth.service';
import { catchError, map } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthModel } from './auth.model';
import * as str from '../shared/utils/string.utils';

export class AuthStateModel {
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
export class AuthState implements NgxsOnInit {

  constructor(private authService: AuthService,
    private spinner: NgxSpinnerService) { }

  ngxsOnInit(ctx: StateContext<AuthStateModel>) {
    ctx.dispatch(new events.GetAuthData());
  }

  @Action(events.GetAuthData)
  getAuthData(ctx: StateContext<AuthStateModel>) {
    const token = this.authService.getToken();
    if (!str.isBlank(token) && !ctx.getState().valid) {
      this.spinner.show();
      return this.authService.getAuthData().pipe(
        map(response => {
          this.spinner.hide();
          ctx.dispatch([new events.SetToken(response.token), new events.SetAuthData(response.auth)]);
        }),
        catchError(err => {
          this.authService.removeToken();
          this.spinner.hide();
          return throwError(err);
        })
      );
    }
  }

  @Action(events.SetAuthData)
  setAuthData({ getState, setState }: StateContext<AuthStateModel>, { payload }: events.SetAuthData) {
    const state = getState();
    setState({
      ...state,
      authModel: payload,
      valid: !str.isBlank(payload.username)
    });
  }

  @Action(events.SetToken)
  setToken({ getState, setState }: StateContext<AuthStateModel>, { payload }: events.SetToken) {
    this.authService.setToken(payload);
  }

  @Action(events.Login)
  login(ctx: StateContext<AuthStateModel>, { payload }: events.Login) {
    this.spinner.show();
    return this.authService.login(payload).pipe(
      map(response => {
        this.spinner.hide();
        ctx.dispatch([new events.SetToken(response.token), new events.SetAuthData(response.auth)]);
      }),
      catchError(err => {
        this.spinner.hide();
        return throwError(err);
      })
    );
  }

  @Action(events.Logout)
  logout({ getState, setState }: StateContext<AuthStateModel>) {
    this.authService.removeToken();
    const state = getState();
    setState({
      ...state,
      authModel: {
        id: 0,
        roles: [],
        username: ''
      },
      valid: false
    });
  }

}
