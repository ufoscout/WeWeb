import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import * as str from '../shared/utils/string.utils';
import { SessionExpired } from './auth.events';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private _store: Store, private authService: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const tokenString = this.authService.getToken();
        if (!str.isBlank(tokenString)) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${tokenString}`
                }
            });
        }

        return next.handle(req).pipe(
            map((event: HttpEvent<any>) => {
                // if (event instanceof HttpResponse) {
                //    console.info('HttpResponse::event =', event, ';');
                // }
                return event;
            }),
            catchError((err: any, caught) => {
                // console.log(err)
                if (err instanceof HttpErrorResponse) {
                    // console.log('is error')
                    if (err.status === 401 || err.status === 403) {
                        // console.log('is 401')
                        const message = str.getOrEmpty(err.error['message']);
                        // console.log('message: ' + message)
                        switch (message) {
                            case 'NotAuthenticated':
                            case 'TokenExpired':
                                this._store.dispatch(new SessionExpired());
                                break;
                        }
                    }
                    return throwError(err);
                }
            })
        );
    }

}
