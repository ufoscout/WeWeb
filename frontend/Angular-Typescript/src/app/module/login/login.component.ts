import { Component } from '@angular/core';
import { Login } from './login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: ['./header.component.css']
})
export class LoginComponent {

  model = new Login('', '');

  submitLogin(): void {
    console.log("Send login data " + JSON.stringify(this.model));
  }

}
