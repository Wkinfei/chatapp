export interface FriendProfiles {
    userId: number;
    username: string;
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

export interface Claims{
  userId: number, 
  username: string, 
  email: string,
  exp: number,
  token: string
}

export class UserToken {
  constructor(
      private _userId: number, 
      private _username: string,
      private _email: string,
      private _exp: number,
      private _token: string
  ) {}

  get userId(): number {
    return this._userId;
  }

  get username(): string {
    return this._username;
  }

  get email(): string {
    return this._email;
  }

  get exp(): number {
    return this._exp;
  }

  get token(): string {
    return this._token;
  }



}

// export class UserToken {
  
//   private userId!: number; 
//   private username!: string; 
//   private email!: string;
//   private exp!: number; 
//   private token!: string;

  // constructor(token: string) {
  //   const claims = this.parseToken(token);

  //   this.userId = claims.userId;
  //   this.username = claims.username;
  //   this.email = claims.email;
  //   this.exp = claims.exp;
  //   this.token = token;
  // }

  // private parseToken(jwt: string) : Claims {

  //   var base64Url = jwt.split('.')[1];
  //   var base64 = base64Url.replace('-', '+').replace('_', '/');
  //   var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
  //       return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  //   }).join(''));
  //   return JSON.parse(jsonPayload);
  // }
// }