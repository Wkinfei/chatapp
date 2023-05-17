import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChatHomeComponent } from './chat/chat-home/chat-home.component';
import { ChatDetailComponent } from './chat/chat-detail/chat-detail.component';
import { ChatBannerComponent } from './chat/chat-banner/chat-banner.component';
import { AddFriendComponent } from './chat/add-friend/add-friend.component';

const routes: Routes = [
  { path: 'chat', component: ChatHomeComponent, children: [
    { path: '', component: ChatBannerComponent },
    { path: 'add-friend', component: AddFriendComponent },
    // { path: 'edit-profile', component: ChatHomeComponent },
    { path: ':id', component: ChatDetailComponent },
  ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
