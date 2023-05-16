export interface MessageDetail {
    text: string,
    chatId: number,
    senderId: number,
    msgTime: Date,
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