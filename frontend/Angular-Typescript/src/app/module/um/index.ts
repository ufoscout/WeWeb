import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { NgxsModule } from '@ngxs/store';
import { UmState } from './um.state';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '../common';
import { FormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';
import { AuthModule } from '../auth';
import { UmRoutingModule } from './um.routing';
import { CreateAccountComponent } from './pages/createAccount/createAccount.component';
import { UmParentComponent } from './pages/umParent.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { UmService } from './um.service';

@NgModule({
  declarations: [
    UmParentComponent,
    ProfileComponent,
    CreateAccountComponent,
  ],
  imports: [
    AuthModule,
    HttpClientModule,
    TranslateModule,
    CommonModule,
    FormsModule,
    NgxSpinnerModule,
    AuthModule,
    UmRoutingModule,
    NgxsModule.forFeature([UmState]),
  ],
  providers: [
    UmService,
    UmParentComponent,
  ]
})
export class UmModule { }
