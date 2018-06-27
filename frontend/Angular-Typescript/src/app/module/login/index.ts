import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './pages/login.component';
import { LoginRoutingModule } from './login.routing';
import { AuthModule } from '../auth';
import { SharedModule } from '../shared';
import { TranslateModule } from '@ngx-translate/core';
import { NgxSpinnerModule } from 'ngx-spinner';
import { LoginService } from './login.service';
import { LoginFormComponent } from './components/loginForm.component';
import { LoginModalComponent } from './pages/loginModal.component';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    LoginFormComponent,
    LoginComponent,
    LoginModalComponent,
  ],
  entryComponents: [
    LoginModalComponent,
  ],
  imports: [
    TranslateModule,
    CommonModule,
    FormsModule,
    NgxSpinnerModule,
    AuthModule,
    LoginRoutingModule,
    SharedModule,
    NgbModalModule,
  ],
  providers: [
    LoginService,
  ],
  exports: [
    TranslateModule,
  ]
})
export class LoginModule {
  constructor(loginService: LoginService) {}
}
