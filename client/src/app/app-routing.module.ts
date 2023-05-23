import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatHomeComponent } from './chat/chat-home/chat-home.component';
import { ChatDetailComponent } from './chat/chat-detail/chat-detail.component';
import { ChatBannerComponent } from './chat/chat-banner/chat-banner.component';
import { AddFriendComponent } from './chat/add-friend/add-friend.component';
import { EditProfileComponent } from './chat/edit-profile/edit-profile.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { LogInComponent } from './log-in/log-in.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'log-in', component: LogInComponent},
  { path: 'chat', component: ChatHomeComponent, children: [
    { path: '', component: ChatBannerComponent },
    { path: 'add-friend', component: AddFriendComponent },
    { path: 'edit-profile', component: EditProfileComponent },
    { path: ':id', component: ChatDetailComponent },
  ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
