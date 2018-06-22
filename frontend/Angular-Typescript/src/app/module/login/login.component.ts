import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Login } from '../auth/auth.events';
import { LoginDto } from '../um/generated/dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  submitError = false;
  model: LoginDto = {
    username: '',
    password: ''
  };

  constructor(private store: Store, private router: Router) { }

  submitLogin(): void {
    this.submitError = false;
    this.store
      .dispatch(new Login(this.model))
      .subscribe(value => {
        this.router.navigate(['/']);
      },
      err => {
        this.submitError = true;
      });
  }

}
