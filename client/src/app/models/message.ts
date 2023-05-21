export interface MessageDetail {
    text: string,
    chatId: number,
    senderId: number,
    msgTime: Date,
    msgType: string;

  }

  export interface UpdatedMessage{
    type:String,
    messageDetail:MessageDetail
  }

  export interface GifDetails{
 
    tinyGifUrl: string;
    nanoGifUrl: string;
}