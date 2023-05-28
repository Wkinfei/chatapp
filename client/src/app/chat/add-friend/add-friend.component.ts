import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { ProfileService } from 'src/app/services/profile.service';
import { ToastService } from 'src/app/toast-container/toast.service';

@Component({
  selector: 'app-add-friend',
  templateUrl: './add-friend.component.html',
  styleUrls: ['./add-friend.component.css']
})
export class AddFriendComponent implements OnInit{
  addFriendForm!: FormGroup;
  userId!:number;

  constructor( private fb: FormBuilder,
                private router: Router,
                public service: ProfileService,
                private authService: AuthService,
                private toastService: ToastService ){}

  ngOnInit(): void {
    this.addFriendForm = this.createForm();
    this.authService.token$
				.subscribe(user => {
					if (user) this.userId = user.userId;
				}
				);
  }

  createForm(){
    let grp = this.fb.group({
      email : this.fb.control<string>('',[ Validators.required, Validators.email ])
    });
    return grp;
  }

  processForm(){
    firstValueFrom(this.service.addProfile(this.userId,this.addFriendForm.value)).then(
      () => {
        this.addFriendForm.reset();
        this.router.navigate(["/chat"]);
        this.toastService.clear();
        this.toastService.showSuccess("Let's chat!!");
      })
      .catch((error) => {
        console.log(error);
        this.toastService.showDanger("The email you entered is not valid or User already exist in your friend list. Please try again.")
      })
  }

  cancel(){
    this.router.navigate(["/chat"])
  }
}
