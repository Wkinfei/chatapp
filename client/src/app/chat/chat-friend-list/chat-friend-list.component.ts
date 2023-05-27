import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { FriendProfiles } from 'src/app/models/profiles';
import { ProfileService } from 'src/app/services/profile.service';
import { DeleteComponent } from './modals/delete/delete.component';
import { AuthService } from 'src/app/services/auth.service';
import { ToastService } from 'src/app/toast-container/toast.service';

@Component({
  selector: 'app-chat-friend-list',
  templateUrl: './chat-friend-list.component.html',
  styleUrls: ['./chat-friend-list.component.css']
})
export class ChatFriendListComponent {

  profiles$: Observable<FriendProfiles[]>;
  total$: Observable<number>;

  
  constructor(
    private router: Router,
    public service: ProfileService,
    private modalService: NgbModal,
    private authSvc: AuthService,
    private toastService: ToastService
    ){
      this.profiles$ = service.profiles$;
      this.total$ = service.total$;
    }

    //Oninit to listen to the delta

    onSelectFriend(id:number): void {
      this.router.navigate(["/chat", id]);
    }

    openDelete(profile: FriendProfiles) {
      const modalRef = this.modalService.open(DeleteComponent);
      modalRef.componentInstance.id = profile.userId;
      modalRef.componentInstance.name = profile.username;
      modalRef.result
        .then(id => { if(typeof(id) !== "string") {this.toastService.showPrimary("Friend removed!!!"); 
                                                    this.service.removeProfile(id)}})
        .catch(err => console.log(err));
      // modalRef.result
      //     .then(resolve => console.log("resolve --> ", resolve))
      //     .catch(reject => console.log("reject --> " , reject));
    }

    onAddFriend(){
      this.router.navigate(["/chat/add-friend"])
    }

    onEditProfile(){
      this.router.navigate(["/chat/edit-profile"])
    }

    onLogOut(){
      this.authSvc.logoutUser();
      this.router.navigate(["/log-in"]);
      //add toaster
      
    }
}
