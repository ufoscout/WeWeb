import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateAccountComponent } from './pages/createAccount/createAccount.component';
import { UmParentComponent } from './pages/umParent.component';
import { ProfileComponent } from './pages/profile/profile.component';

const routes: Routes = [
  {
    path: 'um', component: UmParentComponent,
    children: [
      { path: 'profile', component: ProfileComponent },
      { path: 'createAccount', component: CreateAccountComponent }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UmRoutingModule { }
