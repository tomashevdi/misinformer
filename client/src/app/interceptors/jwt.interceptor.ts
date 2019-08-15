import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpSentEvent,
  HttpHeaderResponse,
  HttpProgressEvent,
  HttpResponse,
  HttpUserEvent,
  HttpErrorResponse,
  HttpEvent
} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import { throwError as observableThrowError, BehaviorSubject } from 'rxjs';
import {take, filter, catchError, switchMap, finalize, tap, mergeMap, concatMap} from 'rxjs/operators';
import {AuthenticationService} from '../services/authentication.service';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  isRefreshingToken: boolean = false;
  tokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  constructor (private authService: AuthenticationService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.match(/\/getJWT$/)) {
      return next.handle(req);
    }

    return next.handle(this.addToken(req)).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse) {
          switch ((<HttpErrorResponse>error).status) {
            case 401:
              return this.handle401Error(req, next);
            default:
              return observableThrowError(error);
          }
        } else {
          return observableThrowError(error);
        }
      }));
  }

  handle401Error(req: HttpRequest<any>, next: HttpHandler) {

    if (!this.isRefreshingToken) {
      this.isRefreshingToken = true;

      this.tokenSubject.next(null);

      return this.authService.renewToken().pipe(take(1),
        switchMap((newToken: string) => {
          if (newToken) {
            this.tokenSubject.next(newToken);
            return next.handle(this.addToken(req));
          }
          return this.logoutUser();
        }),
        catchError(error => {
          return this.logoutUser();
        }),
        finalize(() => {
          this.isRefreshingToken = false;
        }));
    } else {
      return this.tokenSubject.pipe(
        filter(token => token != null),
        take(1),
        switchMap(token => {
          return next.handle(this.addToken(req));
        }));
    }
  }

  logoutUser() {
    this.authService.logout();
    return observableThrowError('Invalid credentials');
  }

  addToken(req: HttpRequest<any>): HttpRequest<any> {
    const token = this.authService.getToken();
    if (token) {
      return req.clone({setHeaders: {Authorization: `Bearer ${token}`}});
    } else {
      return req;
    }
  }
}
