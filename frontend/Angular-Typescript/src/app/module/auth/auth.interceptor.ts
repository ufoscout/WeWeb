import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngxs/store';
import { AuthState, AuthStateModel } from './auth.state';
import * as str from '../shared/utils/string.utils';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private _store: Store) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authState = this._store.selectSnapshot<AuthStateModel>((state) => state.auth);

        if (!str.isBlank(authState.token)) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${authState.token}`
                }
            });
        }

        return next.handle(req);
    }

}
