import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Router } from '@angular/router';
import { CreateLoginDto } from '../../generated/dto';
import { ServerError } from '../../../shared/model/serverError.model';
import { CreateLogin } from '../../../auth/auth.events';

@Component({
  selector: 'app-um-create-account',
  templateUrl: './createAccount.component.html'
})
export class CreateAccountComponent {

  submitError?: ServerError;
  model: CreateLoginDto = {
    email: '',
    password: '',
    passwordConfirm: ''
  };

  constructor(private store: Store, private router: Router) { }

  submit(): void {
    this.store.dispatch(new CreateLogin(this.model))
    .subscribe(value => {
      this.router.navigate(['/']);
    },
    (err) => {
      this.submitError = err.error;
    });
  }

  passwordsMatch(): boolean {
    // if (str.isBlank(this.model.password) && str.isBlank(this.model.passwordConfirm) {
      return this.model.password === this.model.passwordConfirm;
    // }
  }

}
