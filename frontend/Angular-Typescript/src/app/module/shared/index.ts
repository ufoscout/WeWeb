import { NgModule } from '@angular/core';
import { NgxsModule } from '@ngxs/store';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { StorageService } from './service/storage.service';

@NgModule({
  declarations: [],
  imports: [],
  providers: [
    StorageService,
  ]
})
export class SharedModule { }
