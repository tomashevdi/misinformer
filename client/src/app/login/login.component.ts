import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {DynamicDialogRef, MessageService} from 'primeng/api';
import {AuthenticationService} from '../services/authentication.service';
import {User} from '../model/user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = new FormControl('');
  password = new FormControl('');

  constructor(private ref: DynamicDialogRef) { }

  ngOnInit() {
  }

  login() {
    const user = new User();
    user.username = this.username.value;
    user.password = this.password.value;
    this.ref.close(user);
    // this.auth.logout();
    // this.auth.loginCred(this.username.value, this.password.value).subscribe(
    //   () => this.location.back(),
    //   (error) => {
    //     this.msg.add({severity: 'error', summary: 'Неудачный вход', detail: error.message});
    //   }
    // );
  }

}
