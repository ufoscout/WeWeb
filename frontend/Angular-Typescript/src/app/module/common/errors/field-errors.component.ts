import { Component, OnInit, Input } from '@angular/core';
import * as str from '../../shared/utils/string.utils';

@Component({
  selector: 'app-field-errors',
  template: `
  <div *ngFor="let e of errors" class="alert alert-danger">
    {{ (_str.isBlank(translateKey) ? 'errors.messages.' : translateKey) + e | translate}}
  </div>
  `
})
export class FieldErrorsComponent implements OnInit {

  @Input() errors: String[];
  @Input() translateKey: String;
  _str = str;

  constructor() {}

  ngOnInit() {
  }

}
