export interface MessageDetail {
    text: string,
    chatId: number,
    senderId: number,
    msgTime: Date,
    msgType: string;
    // type?: string;
    // image?: string;
  }

  // export interface UpdatedMessage extends MessageDetail{
  //   type:String
  // }

  export interface UpdatedMessage{
    type:String,
    messageDetail:MessageDetail
  }

  export interface GifDetails{
    // id: string;
    // content_description: string;
    tinyGifUrl: string;
    nanoGifUrl: string;
}