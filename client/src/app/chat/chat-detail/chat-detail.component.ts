import { Component, OnInit } from '@angular/core';
import { FormGroup} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject, Subscription, debounceTime, distinctUntilChanged, filter, firstValueFrom, map, tap } from 'rxjs';
import { GifDetails, MessageDetail} from 'src/app/models/message';
import { FriendProfiles, Relationship } from 'src/app/models/profiles';
import { RxStompService } from 'src/app/rx-stomp/rx-stomp.service';
import { ChatService } from 'src/app/services/chat.service';
import { ProfileService } from 'src/app/services/profile.service';
import { GifComponent } from './modals/gif/gif.component';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-chat-detail',
  templateUrl: './chat-detail.component.html',
  styleUrls: ['./chat-detail.component.css']
})
export class ChatDetailComponent implements OnInit{
  sub$!: Subscription;
  params$!: Subscription;
 

  messages: MessageDetail[] = [];
  relationships: Relationship[]=[]
  
  userId!:number;
  friendId!: number;
  chatId!:number;
  message: string = "";
  myProperty: string = "";
  myProperty$ = new BehaviorSubject('');
  gifs: GifDetails[] =  [];
  friendProfile!: any;


  //Get friend profile imageUrl & displayName

  constructor(private rxStompService: RxStompService, 
    private chatSvc: ChatService,
    private activatedRoute: ActivatedRoute,
    private profileSvc: ProfileService,
    private modalService: NgbModal,
    private authService: AuthService ){
    }
  
  ngOnInit(): void {

    this.searchGif();

    this.authService.token$
				.subscribe(user => {
					if (user) this.userId = user.userId;
				}
				);
    
    
    this.params$ = this.activatedRoute.params.subscribe(
      async (params)=> {
      
        this.friendId = params['id'];
        this.friendProfile = await this.getFriendProfile(this.friendId);
        this.relationships = await this.setRelationship();        
        this.chatId = await this.getChatId(this.userId, this.friendId);
        this.messages = await this.getMsg(this.chatId);
        
      
        if(this.sub$) this.sub$.unsubscribe();

        this.sub$ = this.rxStompService
            .watch(`/notifications/messages/${this.userId}`)
            .pipe(
              map((data) => JSON.parse(data.body) as MessageDetail),
              filter(data => data.chatId == this.chatId),
              tap(data => {

                // TODO: RX stomp sends incorrect time, purely display issue
                // Manually offset by +8 hours on client fix display issue
                // Need root cause analysis
                let dataTime = new Date(data.msgTime).getTime();
                let newTime = dataTime + 8*60*60*1000;
                data.msgTime = new Date(newTime);

                this.messages.push(data);
              })
              )
            .subscribe();
      })
  }//oninit

  resetForm() {
    this.myProperty = ""
  }

  ngModelChange($event: string) {
    this.myProperty=$event;
    this.myProperty$.next(this.myProperty);
  }

  onSendMessage() {
    const body = {
      text: this.myProperty,
      chatId: this.chatId,
      senderId: this.userId,
      msgType: "text",
      msgTime: new Date()
       }

       this.chatSvc.sendMessage(body)
        .subscribe(() => {
          this.resetForm(); 
        });
  }

  sendGif(gifUrl: string) {
    const body = {
      text: gifUrl,
      chatId: this.chatId,
      senderId: this.userId,
      msgType: "image",
      msgTime: new Date()
       }
      
       this.chatSvc.sendMessage(body)
        .subscribe(() => {
          this.resetForm(); 
        });
  }

  async getChatId(userId: number, friendId: number) : Promise<number> {

    let res = this.relationships.find(x => (
        x.userId1 == Math.min(userId, friendId) && 
        x.userId2 == Math.max(userId, friendId)
        ));
    
    // @ts-ignore
    return (!!res ? res.chatId : -1)
  }

  async setRelationship(): Promise<Relationship[]> {
    return firstValueFrom(this.profileSvc.getRelationship())
    // this.profileSvc.getRelationship().subscribe(x => {this.relationships = x})
  }

  async getMsg(chatId:number) : Promise<MessageDetail[]>{
    this.chatSvc.getAllMessages(chatId).subscribe(x => {
      this.messages = x;
    });
    // let filterMsgByChatId = this.messages.filter(msg => (msg.chatId == chatId));
    //  return filterMsgByChatId;
    return this.messages;
  }

  async getFriendProfile(friendId:number):Promise<{username:string,imageUrl:string}>{
    return firstValueFrom( this.profileSvc.getProfileById(friendId));
  }

  searchGif(){
    this.myProperty$.asObservable()
    .pipe(
      debounceTime(800),
      distinctUntilChanged(),
      filter((message) => message.length > 5),
      filter((message) => message.substring(0, 5).toLocaleLowerCase() === "@gif "),
      map((message) => message.substring(5, message.length)),
      filter((message) => !!message.trim()),
    )
    .subscribe(message => {
      console.log(message)
      this.chatSvc.getGif(message, 50).subscribe( x => {
        this.gifs = x.gifs;
        const modalRef = this.modalService.open(GifComponent);
        modalRef.componentInstance.gifs = this.gifs;
        modalRef.result
                  // //modelRef.result to fetch the selected gif and send
                  .then((gif: GifDetails) => this.sendGif(gif.tinyGifUrl))
                  .catch(err => console.log(err));
      });

    });
  }
}
