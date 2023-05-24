import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ProfileService } from 'src/app/services/profile.service';

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
                public service: ProfileService ){}

  ngOnInit(): void {
    this.editProfileForm = this.createForm();
  }

  createForm(){
    let grp = this.fb.group({
      username : this.fb.control<string>('')
    });
    return grp;
  }

  processForm(){
    console.log("edit>>>",this.editProfileForm.value)
    this.service.upDateNameById(this.userId,this.editProfileForm.value);
    this.router.navigate(["/chat"]);
  }

  cancel(){
    this.router.navigate(["/chat"])
  }
}
