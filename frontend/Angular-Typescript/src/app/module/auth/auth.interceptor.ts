import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
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

        return next.handle(req).map((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                console.info('HttpResponse::event =', event, ';');
            }
            return event;
        })
            .catch((err: any, caught) => {
                if (err instanceof HttpErrorResponse) {
                    console.info('err.error =', err.error, ';');
                    if (err.status === 403 || err.status === 401) {
                        const message = str.getOrEmpty(err.error['message']);
                        console.log("Error message is: " + message);
                        switch (message) {
                            case "AccessDenied":
                                console.log("UC: not authenticated or does not have the rights to access");
                                break;
                            case "TokenExpired":
                                console.log("UC: The token is not valid any more");
                                break;
                        }
                    }
                    return Observable.throw(err);
                }
            });
    }

}