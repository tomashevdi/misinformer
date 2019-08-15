import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/msg.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {

  cols: any[] = [
    { field: 'date', header: 'Дата' },
    { field: 'author', header: 'Автор' },
    { field: 'subject', header: 'Тема' },
    { field: 'startDate', header: 'Начало' },
    { field: 'stopDate', header: 'Завершение'}
  ];

  hideExpired = true;

  constructor(public msgService: MessageService, private router: Router) { }

  ngOnInit() {
  }

  onNew() {
    this.router.navigate(['/messages/new']);
  }

  onRefresh() {
    this.msgService.loadData();
  }
}
