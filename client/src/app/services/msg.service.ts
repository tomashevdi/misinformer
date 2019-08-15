import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import {restService} from '../config';
import {Message, ReadMark} from '../model/message.model';
import {tap} from 'rxjs/operators';

@Injectable()
export class MessageService {
    private _messages: BehaviorSubject<Message[]> = new BehaviorSubject([]);
    public readonly messages: Observable<Message[]> = this._messages.asObservable();

    constructor(private http: HttpClient) {
        this.loadData();
    }

    loadData() {
        this.http.get<Message[]>(restService + 'admin/messages').subscribe( data => this._messages.next(data));
    }

    fetchMessage(id: number): Observable<Message> {
        return this.http.get<Message>(restService + 'admin/messages/' + id);
    }

    save(mg: Message) {
        return this.http.post<Message>(restService + 'admin/messages/', mg).pipe(
          tap( () => this.loadData())
        );
    }

    fetchReadMarks(id: number): Observable<ReadMark[]> {
        return this.http.get<ReadMark[]>(restService + 'admin/messages/' + id + '/readMarks');
    }

    deleteReadMark(id: number) {
        return this.http.delete<any>(restService + 'admin/readMarks/' + id);
    }

    deleteMessage(id: number) {
      return this.http.delete<any>(restService + 'admin/messages/' + id).pipe(
        tap( () => this.loadData())
      );
    }
}
