import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable, pipe, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { AuthState, AuthStateModel } from './auth.state';
import * as str from '../shared/utils/string.utils';
import * as obj from '../shared/utils/object.utils';
import { SessionExpired } from './auth.events';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private _store: Store) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authState = this._store.selectSnapshot<AuthStateModel>((state) => state.auth);

        if (obj.exists(authState) && !str.isBlank(authState.tokenString)) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${authState.tokenString}`
                }
            });
        }

        return next.handle(req).pipe(
            map((event: HttpEvent<any>) => {
                //if (event instanceof HttpResponse) {
                //    console.info('HttpResponse::event =', event, ';');
                //}
                return event;
            }),
            catchError((err: any, caught) => {
                if (err instanceof HttpErrorResponse) {
                    if (err.status === 401 || err.status === 403) {
                        const message = str.getOrEmpty(err.error['message']);
                        switch (message) {
                            case "NotAuthenticated":
                            case "TokenExpired":
                                this._store.dispatch(new SessionExpired())
                                break;
                        }
                    }
                    return throwError(err);
                }
            })
        );
    }

}
