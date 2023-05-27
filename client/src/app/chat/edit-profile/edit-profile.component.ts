import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { ProfileService } from 'src/app/services/profile.service';
import { ToastService } from 'src/app/toast-container/toast.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit{
  editProfileForm!: FormGroup;
  userId=1;

  constructor( private fb: FormBuilder,
                private router: Router,
                public service: ProfileService,
                private authService: AuthService,
                private toastService: ToastService ){}

  ngOnInit(): void {
    this.editProfileForm = this.createForm();
    this.authService.token$
				.subscribe(user => {
					if (user) this.userId = user.userId;
				}
				);
  }

  createForm(){
    let grp = this.fb.group({
      username : this.fb.control<string>('',Validators.compose([ Validators.required, Validators.minLength(3) ]))
    });
    return grp;
  }

  processForm(){
    firstValueFrom(this.service.upDateNameById(this.userId,this.editProfileForm.value))
    .then(
      () => {
        this.editProfileForm.reset();
        this.router.navigate(["/chat"]);
        this.toastService.clear();
        this.toastService.showSuccess("You have successfully updated your username!!");
      })
      .catch(() => {
        this.toastService.showWarning("Update UserName Unsuccessful")
      }
    );
   
  }

  cancel(){
    this.router.navigate(["/chat"])
  }
}
