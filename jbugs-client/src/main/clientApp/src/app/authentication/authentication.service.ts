import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import * as moment from 'moment';
import {now} from 'moment';
import {tap} from 'rxjs/operators';
import {JwtHelperService} from '@auth0/angular-jwt';
import {Router} from '@angular/router';
import {TranslateService} from "@ngx-translate/core";

export interface UserLoginData {
  username: string;
  password: string;
}

export interface UserData {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  baseURL = 'http://localhost:8080/jbugs/rest/';

  constructor(private http: HttpClient,
              private  router: Router,
              private translate: TranslateService
              ) {
  }

  public getToken(): string {
    return localStorage.getItem('token');
  }

  validateUser(username: string, password: string) {

    let body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    return this.http.post<UserData>(this.baseURL + 'authenticate',
      body.toString(),
      {
        headers: new HttpHeaders(
          {'Content-Type': 'application/x-www-form-urlencoded'}
        )
      }).pipe(
      tap(res => this.setSession(res))
    );
  }


  private setSession(authResult) {


    const helper = new JwtHelperService();


    const decodedToken = helper.decodeToken(authResult.token);


    const expiresAt = new Date(decodedToken.exp * 1000);

    localStorage.setItem('token', authResult.token);
    localStorage.setItem('username', decodedToken.iss);
    localStorage.setItem('id_token', decodedToken.iss);
    localStorage.setItem('firstName', decodedToken.firstName);
    localStorage.setItem('lastName', decodedToken.lastName);
    localStorage.setItem('email', decodedToken.email);
    localStorage.setItem('phone', decodedToken.phone);
    localStorage.setItem('expires_at', decodedToken.exp);
    localStorage.setItem('roles', decodedToken.role);
  }

  public isLoggedIn() {

    if (!localStorage['expires_at']) {
      return false;
    }
    return this.getExpiration().isAfter(now());
  }

  public getRolesOfUser() {
    let roles = localStorage['roles'];
    return JSON.parse(roles);
  }

  public userHasPermission(permissionString) {
    let userRoles = this.getRolesOfUser();
    for (let role of userRoles) {
      for (let permission of role.permissions) {
        if (permission.type === permissionString) {
          return true;
        }
      }
    }
    return false;
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  public logout() {
    localStorage.removeItem('id_token');
    localStorage.removeItem('expires_at');
    localStorage.removeItem('username');
    localStorage.removeItem('firstName');
    localStorage.removeItem('lastName');
    localStorage.removeItem('email');
    localStorage.removeItem('phone');
    localStorage.removeItem('roles');
  }

  getExpiration() {
    const time = localStorage['expires_at'];

    const correctSec = time * 1000;
    var expiresAt = new Date(correctSec);


    return moment(expiresAt);
  }


}

