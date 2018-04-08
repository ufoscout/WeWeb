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

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private _store: Store) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this._store.selectSnapshot<AuthStateModel>((state) => state.auth);

        console.log("AuthInterceptor: found token: " + JSON.stringify(token));
        /*
        req = req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
        */

        return next.handle(req);
    }

}