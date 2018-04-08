import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login.routing';
import { AuthModule } from '../auth';

@NgModule({
  declarations: [
    LoginComponent,
  ],
  imports: [
    NgbModule,
    CommonModule,
    FormsModule,
    AuthModule,
    LoginRoutingModule,
  ],
  providers: []
})
export class LoginModule { }
