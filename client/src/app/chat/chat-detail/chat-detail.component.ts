import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription, firstValueFrom } from 'rxjs';
import { MessageDetail, UpdatedMessage } from 'src/app/models/message';
import { Relationship } from 'src/app/models/profiles';
import { RxStompService } from 'src/app/rx-stomp/rx-stomp.service';
import { ChatService } from 'src/app/services/chat.service';
import { ProfileService } from 'src/app/services/profile.service';

@Component({
  selector: 'app-chat-detail',
  templateUrl: './chat-detail.component.html',
  styleUrls: ['./chat-detail.component.css']
})
export class ChatDetailComponent implements OnInit{
  sub$!: Subscription;
  params$!: Subscription;
  chatForm!: FormGroup;

  messages: MessageDetail[] = [];
  relationships: Relationship[]=[]
  // {"text":"haha","chatId":3,"senderId":1,"msgTime":new Date("2023-05-13T10:22:22.755Z"), "type":"text"}, 
  //                         {"text": "this is gif", "image": "https://media.tenor.com/SitX046aHEoAAAAd/lunch-lunch-time.gif", "chatId":3,"senderId":1,"msgTime":new Date("2023-05-13T10:22:22.755Z"), "type":"image"}, 

  userId: number = 1;
  friendId!: number;
  chatId!:number;

  constructor(private rxStompService: RxStompService, 
    private fb: FormBuilder,
    private chatSvc: ChatService,
    private activatedRoute: ActivatedRoute,
    private profileSvc: ProfileService ){

  }
  
  ngOnInit(): void {

    this.chatForm = this.createForm();

    this.params$ = this.activatedRoute.params.subscribe(
      async (params)=> {

        this.friendId = params['id'];
        this.relationships = await this.setRelationship();        
        this.chatId = await this.getChatId(this.userId, this.friendId);
        this.messages = await this.getMsg(this.chatId);

        this.resetForm();

        // this.messages.push({"text":"haha","chatId":3,"senderId":1,"msgTime":new Date("2023-05-13T10:22:22.755Z")});

        if(this.sub$) this.sub$.unsubscribe();

        this.sub$ = this.rxStompService
            .watch(`/notifications/messages/${this.userId}`)
            .subscribe((data) => {this.messages.push(JSON.parse(data.body) as MessageDetail);
                                });
                                
        // console.log("sub",this.sub$);
      }
    );
  }

  createForm(): FormGroup {
    let grp = this.fb.group({
      text: this.fb.control<string>(""),
      chatId: this.fb.control<number>(0), 
      senderId: this.fb.control<number>(0),
      msgTime: this.fb.control<Date>(new Date()) 
    });
    console.log("form group");
    return grp;
  }

  resetForm(): void{
    this.chatForm.setValue({
      text: "", 
      chatId: this.chatId, 
      senderId: this.userId, 
      msgTime: new Date()})
  }

  onSendMessage() {
    //assign a type for text
    const message = this.chatForm.value as MessageDetail;
    message.msgTime = new Date();
    console.log(message);

    this.chatSvc.sendMessage(message)
        .subscribe(() => {
          this.resetForm(); 
          // change to delta method
          this.profileSvc.bossGetFriendProfiles(this.userId);
        });
  }

  async getChatId(userId: number, friendId: number) : Promise<number> {
    // this.profileSvc.getRelationship()
    //                           .subscribe(x => {
    //                             this.relationships = x;         
    //                         });

    let res = this.relationships.find(x => (
        x.userId1 == Math.min(userId, friendId) && 
        x.userId2 == Math.max(userId, friendId)
        ));
    
    // @ts-ignore
    return (!!res ? res.chatId : -1)
  }

  async setRelationship(): Promise<Relationship[]> {
    return firstValueFrom(this.profileSvc.getRelationship())
  }

  async getMsg(chatId:number) : Promise<MessageDetail[]>{
    this.chatSvc.getAllMessages(chatId).subscribe(x => {
      this.messages = x;
    });
    let found = this.messages.filter(msg => (msg.chatId == chatId));
     return found;
  }

}
