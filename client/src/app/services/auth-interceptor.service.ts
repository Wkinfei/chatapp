import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, exhaustMap, take } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable()
  export class AuthInterceptorService implements HttpInterceptor{
  
    constructor(
      private authService : AuthService,
  
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {


      
        if(req.url.match(".*/log-in") || req.url.match(".*/sign-up") 
        || req.url.match(".*/api/auth")|| req.url.match(".*/api/email")) {
            return next.handle(req);
        }
        return this.authService.token$.pipe(
            take(1), 
            exhaustMap(token => {
                let authReq = req.clone({
                    setHeaders: {"Authorization": `Bearer ${token?.token}`}
                    });
                return next.handle(authReq);                
                }));
    }
  }