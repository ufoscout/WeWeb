import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '../common';
import { HomeComponent } from './home.component';
import { HomeRoutingModule } from './home.routing';

@NgModule({
  declarations: [
    HomeComponent,
  ],
  imports: [
    NgbModule,
    CommonModule,
    HomeRoutingModule,
  ],
  providers: []
})
export class HomeModule { }
