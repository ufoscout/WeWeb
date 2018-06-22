import { NgModule } from '@angular/core';
import { NgxsModule } from '@ngxs/store';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { AuthState } from './auth.state';
import { AuthInterceptor } from './auth.interceptor';
import { AuthService } from './auth.service';
import { NgxSpinnerModule } from 'ngx-spinner';

@NgModule({
  declarations: [
  ],
  imports: [
    HttpClientModule,
    NgxsModule.forFeature([AuthState]),
    NgxSpinnerModule,
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class AuthModule { }
