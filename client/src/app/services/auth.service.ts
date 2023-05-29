import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { UserToken } from '../models/profiles';
import { TokenService } from './token.service';
import { ToastService } from '../toast-container/toast.service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private _token = new BehaviorSubject<UserToken | null>(null);
  private autoLogoutTimer: any;

  get token$() {
    return this._token.asObservable();
  }

  private URI: string = "/api";

  constructor( private httpClient: HttpClient,
              private tokenService: TokenService,
              private toastService: ToastService
              ) { }

  addNewUser(payload:{email:string, username:string, password:string}){
    return this.httpClient.post(`${this.URI}/sign-up`, payload);
  }
  
  loginUser(payload:{email:string, password:string}) : Observable<{"token": string}> {

    return this.httpClient
                .post<{"token": string}>(`${this.URI}/log-in`, payload)
                .pipe(
                  tap(payload => this.login(payload.token))
                  );
  }

  logoutUser() {
    this.logout();
  }

 public autoLoginLogout() {
    const token = this.tokenService.getToken();
    if(!token){
      return;
    }
    if(this.tokenService.isExpired(token)) {
      this.logout();
      return
    } 
      this.login(token)
  }

  private login(token: string) : void {
    this.tokenService.setToken(token);
    let userToken = this.tokenService.toToken(token);
    this.setLogoutTimer(token);
    this._token.next(userToken);
  }

  private logout() {
    this._token.next(null);
    this.tokenService.invalidate();
    this.clearLogoutTimer();
    this.toastService.showSuccess("You are now logged out. We look forward to seeing you again.")
  }

 private setLogoutTimer(token: string) {
    if(token) {
      setTimeout(() => {
        this.logout();
      }, this.tokenService.timeToExpiry(token));
    }

  }

 private clearLogoutTimer() {
    this.autoLogoutTimer = null;
  }

  emailExists(email: string) : Observable<boolean> {
		let params = new HttpParams().append("email", email);
		return this.httpClient.get<boolean>("/api/auth", {params});
	  }

}



