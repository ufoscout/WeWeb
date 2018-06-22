import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Router } from '@angular/router';
import { CreateUserDto } from '../../generated/dto';
import { CreateUser } from '../../um.events';
import { ServerError } from '../../../shared/model/serverError.model';

@Component({
  selector: 'app-um-create-account',
  templateUrl: './createAccount.component.html'
})
export class CreateAccountComponent {

  submitError: ServerError;
  model: CreateUserDto = {
    email: '',
    password: '',
    passwordConfirm: ''
  };

  constructor(private store: Store, private router: Router) { }

  submit(): void {
    this.store.dispatch(new CreateUser(this.model))
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
