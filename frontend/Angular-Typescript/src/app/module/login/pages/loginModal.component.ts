import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Logout } from '../../auth/auth.events';

@Component({
  selector: 'app-login-modal',
  templateUrl: './loginModal.component.html'
})
export class LoginModalComponent {

  constructor(public activeModal: NgbActiveModal, private router: Router, private store: Store) { }

  onCancel(): void {
    this.store.dispatch([
      new Logout(),
    ]).subscribe(() => {
      this.activeModal.close();
      this.router.navigate(['/']);
    });
  }

  onSuccess(): void {
    this.activeModal.close();
  }

}
