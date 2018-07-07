import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxsModule } from '@ngxs/store';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing';
import { HomeModule } from './module/home';
import { NgxsLoggerPluginModule } from '@ngxs/logger-plugin';
import { NgxsStoragePluginModule, StorageOption } from '@ngxs/storage-plugin';
import { UmModule } from './module/um';
import { AuthLoginModule } from './module/auth/login';
import { environment } from '../environments/environment';
import { PageNotFoundModule } from './module/pageNotFound';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    NgbModule.forRoot(),
    NgxsModule.forRoot([]),
    NgxsStoragePluginModule.forRoot({
      storage: StorageOption.SessionStorage,
    }),
    AppRoutingModule,
    HomeModule,
    AuthLoginModule,
    UmModule,
    // This module should be the last with routing imported.
    PageNotFoundModule,
    // NgxsLoggerPluginModule must be loaded at the end
    NgxsLoggerPluginModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    console.log(`-------------`);
    console.log(`Application started. Environment:`);
    console.log(environment);
    console.log(`-------------`);
  }
}
