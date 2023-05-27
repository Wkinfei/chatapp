import { Injectable } from '@angular/core';
import { Router, UrlTree } from '@angular/router';
import { Observable, map, take, tap } from 'rxjs';
import { ToastService } from '../toast-container/toast.service';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard{

  constructor(
    private authService : AuthService,
    private toastService: ToastService,
    private router: Router,
  ) { }

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    
      return this.authService.token$
              .pipe(
                take(1),
                map(user => {
                  return !!user
                }),
                tap(bool => {
                  if(!bool) {
                    this.router.navigate(["/log-in"]);
                    this.toastService.showWarning("Please login to continue");
                  }
                })
                )
  }
}