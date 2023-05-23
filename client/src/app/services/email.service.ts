import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmailDetails } from '../models/email';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private URI: string = "/api/email";

  constructor(private httpClient: HttpClient) { }

  sendEmail(payload: EmailDetails){
    return this.httpClient.post(`${this.URI}`, payload);
  }
}
