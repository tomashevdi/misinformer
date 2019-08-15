import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {AuthenticationService} from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'МИС Информер';

  items: MenuItem[];

  constructor(public auth: AuthenticationService) {
  }


  ngOnInit() {
    this.items = [
      {label: 'Сообщения', icon: 'fa fa-fw fa-twitter', routerLink: ['/messages']},
      {label: 'Сохраненные группы', icon: 'fa fa-fw fa-object-group', routerLink: ['/groups']},
    ];
  }


  onLogout() {
    this.auth.logout();
  }
}
