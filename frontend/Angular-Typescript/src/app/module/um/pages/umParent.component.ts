import { Component, OnInit } from '@angular/core';
import { Store } from '@ngxs/store';
import { GetAuthData } from '../../auth/auth.events';

@Component({
  selector: 'app-um-parent',
  templateUrl: './umParent.component.html'
})
export class UmParentComponent implements OnInit {

  constructor(private store: Store) {
  }

  ngOnInit() {
    this.store.dispatch(new GetAuthData());
  }
}
