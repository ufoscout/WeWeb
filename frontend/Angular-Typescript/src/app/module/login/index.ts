import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login.routing';

@NgModule({
  declarations: [
    LoginComponent,
  ],
  imports: [
    NgbModule,
    LoginRoutingModule,
  ],
  providers: []
})
export class LoginModule { }
