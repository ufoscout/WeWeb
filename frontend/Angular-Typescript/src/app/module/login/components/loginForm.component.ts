import { Component, Output, EventEmitter } from '@angular/core';
import { Store } from '@ngxs/store';
import { Login } from '../../auth/auth.events';
import { LoginDto } from '../../um/generated/dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './loginForm.component.html'
})
export class LoginFormComponent {

  @Output() onSuccess: EventEmitter<any> = new EventEmitter();
  @Output() onCancel: EventEmitter<any> = new EventEmitter();

  submitError = false;
  model: LoginDto = {
    username: '',
    password: ''
  };

  constructor(private store: Store, private router: Router) { }

  cancelLogin(): void {
    this.onCancel.emit();
  }

  submitLogin(): void {
    this.submitError = false;
    this.store
      .dispatch(new Login(this.model))
      .subscribe(value => {
       this.onSuccess.emit();
      },
      err => {
        this.submitError = true;
      });
  }

}
