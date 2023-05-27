import { Injectable } from '@angular/core';
import { Claims, UserToken } from '../models/profiles';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  timeToExpiry(token: string): number {
    const exp = this.parse(token).exp * 1000;
    return exp - new Date().getTime(); 
  }

  isExpired(token: string) : boolean {
    return this.timeToExpiry(token) <= 0;
  }

  toToken(token: string): UserToken | null {

    if (this.isExpired(token)) {
      return null;
    }

    var claims = this.parse(token);

    return new UserToken(claims.userId, claims.username, claims.email, claims.exp, token)
  }

  invalidate(): void {
    sessionStorage.removeItem("userToken");
  }

  getToken(): string | null {
    return sessionStorage.getItem("userToken");
  }

  setToken(token: string) : void {
    sessionStorage.setItem("userToken", token);
  }

  private parse(jwt: string) : Claims {

    var base64Url = jwt.split('.')[1];
    var base64 = base64Url.replace('-', '+').replace('_', '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    console.log(JSON.parse(jsonPayload) as Claims);

    return JSON.parse(jsonPayload);

  }

}