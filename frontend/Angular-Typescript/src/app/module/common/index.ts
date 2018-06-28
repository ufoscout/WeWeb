import { NgModule } from '@angular/core';
import { CommonModule as NgCommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap/collapse/collapse.module';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap/dropdown/dropdown.module';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { NgxsModule } from '@ngxs/store';
import { CommonState } from './common.state';
import { NgxSpinnerModule } from 'ngx-spinner';
import { AuthModule } from '../auth';
import { LanguageService } from './language.service';
import { FieldErrorsComponent } from './errors/field-errors.component';

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    HeaderComponent,
    FieldErrorsComponent,
  ],
  imports: [
    AuthModule,
    NgCommonModule,
    NgxsModule.forFeature([CommonState]),
    NgbCollapseModule,
    NgbDropdownModule,
    RouterModule,
    NgxSpinnerModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    LanguageService,
  ],
  exports: [
    TranslateModule,
    NgCommonModule,
    HeaderComponent,
    FieldErrorsComponent,
  ]
})
export class CommonModule {
  constructor(languageService: LanguageService) {
  }
}
