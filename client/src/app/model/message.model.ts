import {MessageGroup} from './message-group.model';
import {formatDate} from '@angular/common';

export class Message {
  id: number;
  author: string;
  subject: string;
  text: string;
  date: string = formatDate(new Date(), 'yyyy-MM-dd', 'en');
  important: boolean;
  needConfirm: boolean;
  needAnswer: string;
  startDate: string = formatDate(new Date(), 'yyyy-MM-dd', 'en');
  stopDate: string;
  generated: boolean;
  msgGroup: MessageGroup = new MessageGroup();
}

export class ReadMark {
  id: number;
  date: string;
  guid: string;
  userName: string;
  computerName: string;
  answer: string;
}
