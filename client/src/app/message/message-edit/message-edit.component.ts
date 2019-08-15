import {Component, OnInit, ViewChild} from '@angular/core';
import {MessageService} from '../../services/msg.service';
import {Message, ReadMark} from '../../model/message.model';
import {MessageService as MessageServiceNG} from 'primeng/api';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {switchMap, tap} from 'rxjs/operators';
import {of} from 'rxjs';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-message-edit',
  templateUrl: './message-edit.component.html',
  styleUrls: ['./message-edit.component.css']
})
export class MessageEditComponent implements OnInit {

  @ViewChild('groupsComponent') groupsComponent;

  msg: Message = new Message();
  readMarks: ReadMark[] = [];

  colsRM: any[] = [
    { field: 'userName', header: 'Пользователь' },
    { field: 'computerName', header: 'Компьютер' },
    { field: 'date', header: 'Дата' },
    { field: 'answer', header: 'Ответ' },
    { field: 'guid', header: 'GUID пользователя' }
  ];

  constructor(private msgService: MessageService, private route: ActivatedRoute, private router: Router, private messageService: MessageServiceNG, private auth: AuthenticationService) { }

  ngOnInit() {
    this.msg.msgGroup.predefined = false;
    this.route.paramMap.pipe(
      switchMap( (params: ParamMap) => {
        if (params.get('msgId')) {
          if (params.get('msgId') === 'new') {
            const m = new Message();
            m.msgGroup.predefined = false;
            m.author = this.auth.user.getValue().name;
            return of(m);
          } else {
            return this.msgService.fetchMessage(+params.get('msgId'));
          }
        }
      }),
      tap( (m: Message) => {
        if (m.id) {
          this.msgService.fetchReadMarks(m.id).toPromise().then( (rm) => this.readMarks = rm);
        }
      })
    ).subscribe( (m: Message) => {
      this.msg = m;
      this.groupsComponent.loadMessageGroup(m.msgGroup);
    });
  }

  onSave() {
    this.msg.msgGroup = this.groupsComponent.getMessageGroup();
    this.msgService.save(this.msg).subscribe( (res) => {
      this.messageService.add({severity: 'success', summary: 'Успех', detail: 'Сообщение создано / сохранено.'});
      this.router.navigate(['/messages']);
    }, (error) => {
      this.messageService.add({severity: 'error', summary: 'Ошибка', detail: error.message});
    });
  }

  deleteReadMark(id: number) {
    this.msgService.deleteReadMark(id).subscribe( () => {
      this.messageService.add({severity: 'success', summary: 'Успех', detail: 'Отметка удалена.'});
      this.msgService.fetchReadMarks(this.msg.id).toPromise().then( (rm) => this.readMarks = rm);
    });
  }

  onDelete() {
    this.msgService.deleteMessage(this.msg.id).subscribe( () => {
      this.messageService.add({severity: 'success', summary: 'Успех', detail: 'Сообщение удалено.'});
      this.router.navigate(['/messages']);
    });
  }
}
