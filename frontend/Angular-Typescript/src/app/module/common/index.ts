import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    NgbModule,
    RouterModule,
  ],
  providers: [],
  exports: [
    HeaderComponent
  ]
})
export class CommonModule { }
