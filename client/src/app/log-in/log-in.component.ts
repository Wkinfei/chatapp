import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../services/auth.service';

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
    private fb : FormBuilder) {}

  ngOnInit(): void {
    this.loginForm = this.createForm();
  }

  createForm(){
    let grp = this.fb.group({
      email: this.fb.control<string>(""),
      password: this.fb.control<string>("")
    });
    return grp;
  }

  processForm(){
    this.authSvc.loginUser(this.loginForm.value).subscribe(
      x => console.log(x)
    );
  }

}
