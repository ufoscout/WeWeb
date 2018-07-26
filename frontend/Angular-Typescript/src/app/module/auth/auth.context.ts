import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Select } from '@ngxs/store';
import { AuthStateModel, AuthState } from './auth.state';
import * as obj from '../shared/utils/object.utils';
import * as str from '../shared/utils/string.utils';
import { AuthModel } from './auth.model';

@Injectable()
export class AuthContext {

    @Select(AuthState) authState$!: Observable<AuthStateModel>;
    private authModel!: AuthModel;

    constructor() {
        this.authState$.subscribe(state => {
            this.authModel = state.authModel;
        });
    }

    public data(): AuthModel {
        return this.authModel;
    }

    public isAuthenticated(): boolean {
        return obj.exists(this.authModel) && !str.isBlank(this.authModel.username);
    }

    public hasRole(role: string): boolean {
        return this.isAuthenticated() &&
                obj.ifExistsOrDefault(this.authModel.roles, false, (roles) => roles.includes(role));
    }
}
