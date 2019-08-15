import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {MessageGroup} from '../model/message-group.model';
import { BehaviorSubject, Observable } from 'rxjs';
import {restService} from '../config';
import {tap} from 'rxjs/operators';

@Injectable()
export class MessageGroupService {
    private _mGroups: BehaviorSubject<MessageGroup[]> = new BehaviorSubject([]);
    public readonly mGroups: Observable<MessageGroup[]> = this._mGroups.asObservable();

    constructor(private http: HttpClient) {
        this.loadData();
    }

    loadData() {
        this.http.get<MessageGroup[]>(restService + 'admin/msgGroups').subscribe( data => this._mGroups.next(data));
    }

    fetchMessageGroup(id: number): Observable<MessageGroup> {
        return this.http.get<MessageGroup>(restService + 'admin/msgGroups/' + id);
    }

    save(mg: MessageGroup): Observable<MessageGroup> {
        return this.http.post<MessageGroup>(restService + 'admin/msgGroups/', mg).pipe(
          tap( () => this.loadData() )
        );
    }

}
