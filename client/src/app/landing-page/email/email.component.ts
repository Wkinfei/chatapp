import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { EmailService } from 'src/app/services/email.service';

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit{
 
  emailForm!: FormGroup; 
  email$!: Subscription;

  constructor( private fb : FormBuilder,
                private emailSvc: EmailService ){

  }
  
  ngOnInit(): void {
    this.emailForm = this.createForm();
  
  }

  //Add Validation
  createForm(): FormGroup {
    let grp = this.fb.group({
      name: this.fb.control<string>(""),
      email: this.fb.control<string>(""),
      subject: this.fb.control<string>(""),
      content: this.fb.control<string>("")
    });
    return grp;
  }

  processForm(){
    console.log(this.emailForm.value);
    //http call
    this.email$ = this.emailSvc.sendEmail(this.emailForm.value).subscribe();

    this.emailForm.reset();
  }  

  // ngOnDestroy(): void {
  //   console.log("DESTROYYYYY")
  //   if (this.email$){
  //     this.email$.unsubscribe
  //   }
    
  // }
}
