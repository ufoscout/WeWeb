import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-common-confirm',
  templateUrl: './confirm.component.html'
})
export class ConfirmComponent {

  @Input() title!: String;
  @Input() body!: String;

  constructor(public activeModal: NgbActiveModal) { }

  onCancel(): void {
  }

  onSuccess(): void {
  }

}
