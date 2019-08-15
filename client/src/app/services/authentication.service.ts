import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {catchError, retry, switchMap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {restService} from '../config';
import {DialogService} from 'primeng/api';
import {LoginComponent} from '../login/login.component';
import {User} from '../model/user';
import {MessageService as MessageServiceNG} from 'primeng/api';
import * as jwt_decode from 'jwt-decode';

@Injectable()
export class AuthenticationService {

  public readonly user: BehaviorSubject<User> = new BehaviorSubject(new User());

  constructor(private http: HttpClient, private router: Router, private dialogService: DialogService, private messageService: MessageServiceNG) { }

  loginCred(username: string, password: string): Observable<string> {
    return this.http.post<{token: string}>(restService + 'getJWT', {username, password} ).pipe(
      switchMap( jwt => {
        if (jwt.token) {
          localStorage.setItem('token', jwt.token);
          this.setUser(jwt.token);
          return jwt.token;
        }
        return null;
      } ));
  }

  logout() {
    localStorage.removeItem('token');
    this.user.next(new User());
  }

  getToken() {
    if (!this.user.getValue().name && localStorage.getItem('token')) this.setUser(localStorage.getItem('token'));
    return localStorage.getItem('token');
  }

  setUser(token: string) {
    const djwt = jwt_decode(token);
    const u = new User();
    u.username = djwt.sub;
    u.name = djwt.name;
    u.token = token;
    this.user.next(u);
  }

  renewToken(): Observable<string> {
    this.logout();
    return this.showLoginDialog().pipe(
      switchMap( (user: User) => this.loginCred(user.username, user.password) ),
      catchError( (error) => {
        if (error.status === 403) {
          this.messageService.add({severity: 'error', summary: 'Ошибка входа', detail: 'Неверное имя пользователя / пароль.'});
        } else {
          this.messageService.add({severity: 'error', summary: 'Ошибка входа', detail: error.message});
        }
        return this.renewToken();
      })
    );
  }

  showLoginDialog(): Observable<any> {
    return this.dialogService.open(LoginComponent, {header: 'Вход в систему', width: '400px'}).onClose;
  }
}
