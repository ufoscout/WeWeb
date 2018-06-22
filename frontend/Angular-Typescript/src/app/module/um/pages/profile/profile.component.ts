import { Component } from '@angular/core';
import { Store } from '@ngxs/store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-um-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent {

  submitError = false;
  model: "";

  constructor(private store: Store, private router: Router) { }

}
