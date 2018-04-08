import { NgModule } from '@angular/core';
import { NgxsModule } from '@ngxs/store';
import { AuthState } from './auth.state';

@NgModule({
  declarations: [
  ],
  imports: [
    NgxsModule.forFeature([AuthState])
  ],
  providers: []
})
export class AuthModule { }
