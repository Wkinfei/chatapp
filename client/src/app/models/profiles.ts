export interface FriendProfiles {
    userId: number;
    displayName: string;
    text: string;
    msgType: string;
    msgTime: Date ;
    imageUrl: string;
  }

export interface Relationship{
  chatId?: number;
  userId1: number;
  userId2: number;
}

export interface UpdatedProfiles{
  type:string,
  profile:FriendProfiles
}

export interface UserProfile{
  userId: number;
  loginUsername:string;
  loginPassword:string;
  displayName:string;
  email:string;
  imageUrl:string;
}