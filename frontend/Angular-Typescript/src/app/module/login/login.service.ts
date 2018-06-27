import { Store, Actions, ofActionDispatched } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { SessionExpired } from '../auth/auth.events';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginModalComponent } from './pages/loginModal.component';

@Injectable()
export class LoginService {

    constructor(private actions$: Actions, private modalService: NgbModal) {
        this.actions$
          .pipe(ofActionDispatched(SessionExpired))
          .subscribe(() => {
              this.modalService.open(LoginModalComponent, {
                beforeDismiss: () => false
              });
            });
      }

}