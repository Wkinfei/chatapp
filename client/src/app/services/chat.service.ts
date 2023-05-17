import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GifDetails, MessageDetail, UpdatedMessage } from '../models/message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private URI: string = "/api";

  constructor(private httpClient: HttpClient) { }

  sendMessage(payload: MessageDetail){
    return this.httpClient.post(`${this.URI}/chat`, payload);
  }

  //getAllMessages
  getAllMessages(chatId:number){
    return this.httpClient.get<MessageDetail[]>(`${this.URI}/chat/${chatId}`)
  }

  //getGIFs
  getGif(q: string,limit: number){
    let params = new HttpParams()
                .append("q",q)
                .append("limit",limit)
                
    return this.httpClient.get<{gifs: GifDetails[]}>(`${this.URI}/chat/gif`, {params: params})
  }

  

}
