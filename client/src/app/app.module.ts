import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChatHomeComponent } from './chat/chat-home/chat-home.component';
import { ChatFriendListComponent } from './chat/chat-friend-list/chat-friend-list.component';
import { ChatDetailComponent } from './chat/chat-detail/chat-detail.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { RxStompService } from './rx-stomp/rx-stomp.service';
import { rxStompServiceFactory } from './rx-stomp/rx-stomp-service-factory';
import { NgbPaginationModule, NgbAlertModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DecimalPipe } from '@angular/common';
import { DeleteComponent } from './chat/chat-friend-list/modals/delete/delete.component';
import { ChatBannerComponent } from './chat/chat-banner/chat-banner.component';
import { AddFriendComponent } from './chat/add-friend/add-friend.component';
import { GifComponent } from './chat/chat-detail/modals/gif/gif.component';
import { EditProfileComponent } from './chat/edit-profile/edit-profile.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { LogInComponent } from './log-in/log-in.component';
import { EmailComponent } from './landing-page/email/email.component';
import { ToastContainerComponent } from './toast-container/toast-container.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';


@NgModule({
  declarations: [
    AppComponent,
    ChatHomeComponent,
    ChatFriendListComponent,
    ChatDetailComponent,
    DeleteComponent,
    ChatBannerComponent,
    AddFriendComponent,
    GifComponent,
    EditProfileComponent,
    LandingPageComponent,
    SignUpComponent,
    LogInComponent,
    EmailComponent,
    ToastContainerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    NgbPaginationModule,
    NgbAlertModule,
    NgbModule,
  ],
  providers: [
    DecimalPipe, 
    {
    provide: RxStompService,
    useFactory: rxStompServiceFactory,
    },
    { 
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    },
  ],
    
  bootstrap: [AppComponent]
})
export class AppModule {}
