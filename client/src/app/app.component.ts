import { Component } from '@angular/core';
import { ToastService } from './toast-container/toast.service';
import { AuthService } from './services/auth.service';
import { Observable } from 'rxjs';
import { UserToken } from './models/profiles';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';

  token$!:Observable<UserToken | null>;
 
  constructor(
    private authService: AuthService,
  ) {
    this.token$ = authService.token$;
    this.authService.autoLoginLogout();
  }

}
