import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageDetail, UpdatedMessage } from '../models/message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private URI: string = "/api";

  constructor(private httpClient: HttpClient) { }

  // sendMessage(payload: {text : string, chatID:number, senderID:number, msgTime:Date}){
  //   return this.httpClient.post(`${this.URI}/chat`, payload);
  // }

  sendMessage(payload: MessageDetail){
    return this.httpClient.post(`${this.URI}/chat`, payload);
  }

  //getAllMessages
  getAllMessages(chatId:number){
    return this.httpClient.get<MessageDetail[]>(`${this.URI}/chat/${chatId}`)
  }
}
