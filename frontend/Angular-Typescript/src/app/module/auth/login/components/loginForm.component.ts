import { Component, Output, EventEmitter } from '@angular/core';
import { Store } from '@ngxs/store';
import { Login } from '../../auth.events';
import { LoginDto } from '../../../um/generated/dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './loginForm.component.html'
})
export class LoginFormComponent {

  @Output() success: EventEmitter<any> = new EventEmitter();
  @Output() cancel: EventEmitter<any> = new EventEmitter();

  submitError = false;
  model: LoginDto = {
    username: '',
    password: ''
  };

  constructor(private store: Store, private router: Router) { }

  cancelLogin(): void {
    this.cancel.emit();
  }

  submitLogin(): void {
    this.submitError = false;
    this.store
      .dispatch(new Login(this.model))
      .subscribe(value => {
       this.success.emit();
      },
      err => {
        this.submitError = true;
      });
  }

}
