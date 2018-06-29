import { Actions, Select } from '@ngxs/store';
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginModalComponent } from './pages/loginModal.component';
import { AuthState, AuthStateModel } from '../auth/auth.state';
import { Observable } from 'rxjs';

@Injectable()
export class LoginService {

  @Select(AuthState) authState$: Observable<AuthStateModel>;
  previousForceReLogin = false;

  constructor(private modalService: NgbModal) {
    this.authState$.subscribe(state => {
      const forceReLogin = state.forceReLogin;
      if (forceReLogin && !this.previousForceReLogin) {
        this.modalService.open(LoginModalComponent, {
          beforeDismiss: () => false
        });
      }
      this.previousForceReLogin = forceReLogin;
    });
  }

}
