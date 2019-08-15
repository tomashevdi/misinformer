import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ADObject, ADUser } from '../model/ad.model';
import { HttpClient } from '@angular/common/http';
import {restService} from '../config';

@Injectable()
export class ADService {
    private _groups: BehaviorSubject<ADObject[]> = new BehaviorSubject([]);
    public readonly groups: Observable<ADObject[]> = this._groups.asObservable();

    private _ou: BehaviorSubject<ADObject[]> = new BehaviorSubject([]);
    public readonly ou: Observable<ADObject[]> = this._ou.asObservable();

    private _users: BehaviorSubject<ADUser[]> = new BehaviorSubject([]);
    public readonly users: Observable<ADUser[]> = this._users.asObservable();

    constructor(private http: HttpClient) {
        this.loadData();
    }

    loadData() {
        this.http.get<ADObject[]>(restService + 'admin/groups').subscribe( data => this._groups.next(data));
        this.http.get<ADObject[]>(restService + 'admin/ou').subscribe( data => this._ou.next(data));
        this.http.get<ADUser[]>(restService + 'admin/users').subscribe( data => this._users.next(data));
    }

    allGroups(): ADObject[] {
        return this._groups.value.slice();
    }

}
