import { State, Action, StateContext, NgxsOnInit, getActionTypeFromInstance } from '@ngxs/store';
import * as events from './auth.events';
import { AuthService } from '../auth/auth.service';
import { catchError, map } from 'rxjs/operators';
import { throwError, timer, interval } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthModel, TokenModel } from './auth.model';
import * as str from '../shared/utils/string.utils';
import * as t from '../shared/utils/test.utils';

const ONE_MINUTE_MILLISECONDS = 1 * 60 * 1000;
const TEN_MINUTES_MILLISECONDS = 10 * 60 * 1000;

export class AuthStateModel {
  authModel: AuthModel = {
    id: 0,
    roles: [],
    username: ''
  };
  token = new TokenModel();
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

    if (!t.inTests()) {
      interval(ONE_MINUTE_MILLISECONDS)
        .subscribe((val) => {
          this.authService.checkIfTokenToBeRefreshed(TEN_MINUTES_MILLISECONDS);
        });
    }
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
    setState({
      ...getState(),
      token: {
        issuedAt: new Date().getTime(),
        value: payload
      }
    });
  }

  @Action(events.Login)
  login(ctx: StateContext<AuthStateModel>, { payload }: events.Login) {
    this.spinner.show();
    return this.authService.login(payload).pipe(
      map(response => {
        this.spinner.hide();
        if (ctx.getState().valid && (response.auth.username === ctx.getState().authModel.username)) {
          ctx.dispatch([new events.SetToken(response.token), new events.SetAuthData(response.auth)]);
        } else {
          ctx.dispatch([new events.ResetState(), new events.SetToken(response.token), new events.SetAuthData(response.auth)]);
        }
      }),
      catchError(err => {
        this.spinner.hide();
        return throwError(err);
      })
    );
  }

  @Action(events.Logout)
  logout(ctx: StateContext<AuthStateModel>) {
    ctx.dispatch([new events.ResetState()]);
  }

  @Action(events.ResetState)
  resetSession({ getState, setState }: StateContext<AuthStateModel>) {
    this.authService.removeToken();
    setState(new AuthStateModel());
  }

  @Action(events.RefreshToken)
  refreshToken(ctx: StateContext<AuthStateModel>) {
    return this.authService.refreshToken().pipe(
      map(response => {
        ctx.dispatch(new events.SetToken(response.token));
      }),
      catchError(err => {
        return throwError(err);
      })
    );
  }
}
