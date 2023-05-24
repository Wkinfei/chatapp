import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit{
  
  signUpForm! : FormGroup;

  constructor( private fb : FormBuilder,
              private authSvc: AuthService ){

  }
  
  ngOnInit(): void {
    this.signUpForm = this.createForm();
  }

  createForm(): FormGroup {
    let grp = this.fb.group({
      email: this.fb.control<string>(""),
      username: this.fb.control<string>(""),
      password: this.fb.control<string>(""),
      cPassword: this.fb.control<string>(""),
    });
    return grp;
  }

  processForm(): void {
    console.log(this.signUpForm.value);

    this.authSvc.addNewUser(this.signUpForm.value).subscribe();

    this.signUpForm.reset();
  }
}
