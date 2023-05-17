import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ProfileService } from 'src/app/services/profile.service';

@Component({
  selector: 'app-add-friend',
  templateUrl: './add-friend.component.html',
  styleUrls: ['./add-friend.component.css']
})
export class AddFriendComponent implements OnInit{
  addFriendForm!: FormGroup;
  userId=1;

  constructor( private fb: FormBuilder,
                private router: Router,
                public service: ProfileService ){}

  ngOnInit(): void {
    this.addFriendForm = this.createForm();
  }

  createForm(){
    let grp = this.fb.group({
      email : this.fb.control<string>('')
    });
    return grp;
  }

  processForm(){
    console.log("email>>>",this.addFriendForm.value)
    this.service.addProfile(this.userId,this.addFriendForm.value);
    this.router.navigate(["/chat"]);
  }

  cancel(){
    this.router.navigate(["/chat"])
  }
}
