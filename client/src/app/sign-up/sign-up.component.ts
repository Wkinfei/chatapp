import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../toast-container/toast.service';
import { passwordMatchValidator } from './matchPassword';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit{
  
  signUpForm! : FormGroup;

  constructor( private fb : FormBuilder,
              private authSvc: AuthService,
              private router: Router,
              private toastService: ToastService ){

  }
  
  ngOnInit(): void {
    this.signUpForm = this.createForm();
  }

  createForm(): FormGroup {
    let grp = this.fb.group({
      email: this.fb.control<string>("", [ Validators.required, Validators.email ]),
      username: this.fb.control<string>("", Validators.compose([ Validators.required, Validators.minLength(3) ])),
      password: this.fb.control<string>("", Validators.compose([ Validators.required, Validators.minLength(8) ])),
      confirmPassword: this.fb.control<string>("", [ Validators.required ]),
    },{
      validators : passwordMatchValidator 
    }
    );
    return grp;
  }



  processForm(): void {
    console.log(this.signUpForm.value);

    this.authSvc.addNewUser(this.signUpForm.value).subscribe(
      ()=>{
        this.router.navigate(["/log-in"]);
        this.toastService.showSuccess("Sign up successful! Thank you for joining us.");
        this.signUpForm.reset();
      }
    );

    
    
  }
}
