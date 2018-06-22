import { Component } from '@angular/core';
import { Store, Select } from '@ngxs/store';
import { SetLanguage } from '../common.events';
import { CommonState, CommonStateModel } from '../common.state';
import { Observable } from 'rxjs';
import { AuthState, AuthStateModel } from '../../auth/auth.state';
import { Logout } from '../../auth/auth.events';
import { Router } from '@angular/router';

@Component({
  selector: 'app-common-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent {

  @Select(CommonState) commonState$: Observable<CommonStateModel>;
  @Select(AuthState) authState$: Observable<AuthStateModel>;
  navbarCollapsed = true;

  constructor(private store: Store, private router: Router) {
  }

  setLanguage(language: String): void {
    this.store.dispatch([
      new SetLanguage({ language: language }),
    ]);
  }

  logout(): void {
    this.store.dispatch([
      new Logout(),
    ]).subscribe(() => this.router.navigate(['/']));
  }

}
