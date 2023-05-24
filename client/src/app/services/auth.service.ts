import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private URI: string = "/api";

  constructor( private httpClient: HttpClient, ) { }

  addNewUser(payload:{email:string, username:string, password:string}){
    return this.httpClient.post(`${this.URI}/sign-up`, payload);
  }
  
  loginUser(payload:{email:string, password:string}) : Observable<{"token": string}> {

    return this.httpClient.post<{"token": string}>(`${this.URI}/log-in`, payload);
  }
}
