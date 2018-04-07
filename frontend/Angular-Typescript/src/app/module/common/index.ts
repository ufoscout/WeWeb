import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { HeaderComponent } from './header/header.component';

@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    NgbModule,
  ],
  providers: [],
  exports: [
    HeaderComponent
  ]
})
export class CommonModule { }
