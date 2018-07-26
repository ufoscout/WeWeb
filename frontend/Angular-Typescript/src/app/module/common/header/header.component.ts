import { Component, OnInit } from '@angular/core';
import { Store, Select } from '@ngxs/store';
import { SetLanguage } from '../common.events';
import { CommonState, CommonStateModel } from '../common.state';
import { Observable } from 'rxjs';
import { Logout } from '../../auth/auth.events';
import { Router } from '@angular/router';
import { AuthContext } from '../../auth/auth.context';

@Component({
  selector: 'app-common-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  @Select(CommonState) commonState$!: Observable<CommonStateModel>;
  navbarCollapsed = true;

  constructor(private store: Store, private router: Router, public auth: AuthContext) {
  }

  ngOnInit() {
  }

  setLanguage(language: string): void {
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
