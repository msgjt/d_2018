import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from './authentication.service';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  tokenField = 'token';

  constructor(public auth: AuthenticationService, private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (localStorage.getItem(this.tokenField)) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ` + localStorage.getItem(this.tokenField)
        }
      });
    }

    /**
     * continues request execution
     */
    return next.handle(request).pipe(catchError((error, caught) => {
      // intercept the respons error and displace it to the console
      console.log(error);
      this.handleAuthError(error);
      return of(error);
    }) as any);
  }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 403) {
      console.log('handled error ' + err.status);
      this.router.navigate([`/login`]);

      throw err;
    } else if (err.status === 401) {
      console.log('handled error ' + err.status);
      this.router.navigate([`/login`]);

      throw err;
    } else if (err.status === 302) {

    } else {
      console.log('something went really wrong: ' + err.status);
    }
    throw err;
  }
}
