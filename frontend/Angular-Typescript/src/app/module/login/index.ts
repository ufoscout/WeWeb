import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login.routing';
import { AuthModule } from '../auth';
import { SharedModule } from '../shared';
import { TranslateModule } from '@ngx-translate/core';
import { NgxSpinnerModule } from 'ngx-spinner';

@NgModule({
  declarations: [
    LoginComponent,
  ],
  imports: [
    TranslateModule,
    CommonModule,
    FormsModule,
    NgxSpinnerModule,
    AuthModule,
    LoginRoutingModule,
    SharedModule,
  ],
  providers: [
  ],
  exports: [
    TranslateModule
  ]
})
export class LoginModule { }
