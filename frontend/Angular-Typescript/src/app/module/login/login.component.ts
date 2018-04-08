import { Component, OnInit } from '@angular/core';
import { Select, Store } from '@ngxs/store';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AuthState, AuthStateModel } from '../auth/auth.state';
import { Login } from './login.model';
import { SetAuthData } from '../auth/auth.events';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: ['./header.component.css']
})
export class LoginComponent implements OnInit {

  @Select(AuthState) auth$: Observable<AuthStateModel>;
  model = new Login('', '');

  constructor(private store: Store, private http: HttpClient) {}

  ngOnInit(): void {
    this.auth$.subscribe(auth => {
      console.log("Auth data is: " + JSON.stringify(auth));
    });
  }

  submitLogin(): void {
    this.store.dispatch(new SetAuthData({username: this.model.username, token: this.model.password}));
    console.log("Send login data " + JSON.stringify(this.model));
  }

}
