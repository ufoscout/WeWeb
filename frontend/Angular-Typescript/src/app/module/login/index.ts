import { NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login.routing';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    LoginComponent,
  ],
  imports: [
    NgbModule,
    CommonModule,
    FormsModule,
    LoginRoutingModule,
  ],
  providers: []
})
export class LoginModule { }
