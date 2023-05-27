import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../toast-container/toast.service';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent implements OnInit{
  
  loginForm!:FormGroup;
  
  constructor ( 
    private authSvc: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private toastService: ToastService) {}

  ngOnInit(): void {
    this.loginForm = this.createForm();
  }

  createForm(){
    let grp = this.fb.group({
      email: this.fb.control<string>("",[ Validators.required, Validators.email ]),
      password: this.fb.control<string>("", Validators.compose([ Validators.required, Validators.minLength(8) ]))
    });
    return grp;
  }

  processForm(){
    this.authSvc.loginUser(this.loginForm.value).subscribe(
      x => {
        // sessionStorage.setItem("userToken", x.token);
        this.toastService.showSuccess("Welcome !!!!");
        this.router.navigate(["/chat"]);
      },
      err=>{
        this.toastService.showWarning(err.message)
      });
  }

}
