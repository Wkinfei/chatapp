import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const passwordMatchValidator: ValidatorFn = 
        (control: AbstractControl): ValidationErrors | null => {
            let passwordControl = control.get('password');
            let confirmPasswordControl = control.get('confirmPassword');
        
            if (passwordControl &&
                confirmPasswordControl && 
                passwordControl?.value !== confirmPasswordControl?.value) {
            return { 
                passwordmatcherror : true
             };
            }
        
            return null;
        }