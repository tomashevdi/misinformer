import {Component, OnInit, OnDestroy} from '@angular/core';
import {ADService} from '../../services/ad.service';
import {ADObject, ADUser} from '../../model/ad.model';
import {of, Subscription} from 'rxjs';
import {first, flatMap, switchMap, tap} from 'rxjs/operators';
import {MessageGroup, MessageGroupItem} from '../../model/message-group.model';
import {MessageGroupService} from '../../services/mg.service';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {MessageService as MessageServiceNG} from 'primeng/api';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit, OnDestroy {

  sub: Subscription;

  msgGroup: MessageGroup = new MessageGroup();

  groups: ADObject[] = [];
  groupsSource: ADObject[];

  ou: ADObject[] = [];
  ouSource: ADObject[];

  users: ADUser[] = [];
  usersSource: ADUser[];

  mGroups: MessageGroup[] = [];
  mGroupsSource: MessageGroup[];

  constructor(private ad: ADService, private mgs: MessageGroupService, private route: ActivatedRoute, private router: Router, private messageService: MessageServiceNG) {
  }

  ngOnInit() {
    this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
        if (params.get('mgId')) {
          if (params.get('mgId') === 'new') {
            const mg = new MessageGroup();
            mg.predefined = true;
            return of(mg);
          } else {
            return this.mgs.fetchMessageGroup(+params.get('mgId'));
          }
        } else {
          return of(null);
        }
      })
    ).subscribe((m: MessageGroup) => this.loadMessageGroup(m));
  }

  preloadDictionaries() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
    this.groups = [];
    this.users = [];
    this.ou = [];
    this.mGroups = [];
    return this.ad.groups.pipe(first(arr => arr.length > 0)).pipe(
      tap(data => this.groupsSource = data.map<ADObject>(x => ({...x}))),
      flatMap(() => this.ad.ou.pipe(first(arr => arr.length > 0))),
      tap(data => this.ouSource = data.map<ADObject>(x => ({...x}))),
      flatMap(() => this.ad.users.pipe(first(arr => arr.length > 0))),
      tap(data => this.usersSource = data.map<ADUser>(x => ({...x}))),
      flatMap(() => this.mgs.mGroups.pipe(first(arr => arr.length > 0))),
      tap(data => this.mGroupsSource = data.map<MessageGroup>(x => ({...x}))));
  }

  fetchMessageGroup(id: number) {
    this.sub = this.preloadDictionaries().pipe(
      flatMap(() => this.mgs.fetchMessageGroup(id))
    ).subscribe(data => this.setMessageGroup(data));
  }

  loadMessageGroup(m: MessageGroup) {
    this.sub = this.preloadDictionaries().subscribe(() => this.setMessageGroup(m));
  }


  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSave() {
    this.mgs.save(this.getMessageGroup()).subscribe(
      (res) => {
        this.messageService.add({severity: 'success', summary: 'Успех', detail: 'Группа создана / сохранена.'});
        this.router.navigate(['/groups']);
      }, (error) => {
        this.messageService.add({severity: 'error', summary: 'Ошибка', detail: error.message});
      }
    );
  }

  setMessageGroup(mg: MessageGroup) {
    if (!mg) mg = new MessageGroup();
    const guids: string[] = mg.items.map((mgi) => mgi.guid);
    const notGuids: string[] = mg.items.filter((mgi) => mgi.notFlag).map((mgi) => mgi.guid);
    this.groupsSource.filter(obj => guids.includes(obj.guid)).forEach(obj => {
      if (notGuids.includes(obj.guid)) {
        obj.notFlag = true;
      }
      this.groups.push(obj);
    });
    this.groupsSource = this.groupsSource.filter(obj => !this.groups.includes(obj));

    this.usersSource.filter(obj => guids.includes(obj.guid)).forEach(obj => {
      if (notGuids.includes(obj.guid)) {
        obj.notFlag = true;
      }
      this.users.push(obj);
    });
    this.usersSource = this.usersSource.filter(obj => !this.users.includes(obj));

    this.ouSource.filter(obj => guids.includes(obj.guid)).forEach(obj => {
      if (notGuids.includes(obj.guid)) {
        obj.notFlag = true;
      }
      this.ou.push(obj);
    });
    this.ouSource = this.ouSource.filter(obj => !this.ou.includes(obj));

    const mgg = mg.items.filter(mgi => mgi.itemType === 'MSG_GROUP').map((mgi) => mgi.msgGroupId);
    const notMgg = mg.items.filter(mgi => mgi.itemType === 'MSG_GROUP' && mgi.notFlag).map((mgi) => mgi.msgGroupId);
    this.mGroupsSource.filter(obj => mgg.includes(obj.id)).forEach(obj => {
      if (notMgg.includes(obj.id)) {
        obj.notFlag = true;
      }
      this.mGroups.push(obj);
    });
    this.mGroupsSource = this.mGroupsSource.filter(obj => !this.mGroups.includes(obj));

    this.msgGroup = mg;
  }

  getMessageGroup(): MessageGroup {
    this.msgGroup.items = [];
    this.groups.forEach(sel => {
      const mgi = new MessageGroupItem();
      mgi.guid = sel.guid;
      mgi.itemType = 'GROUP';
      if (sel.notFlag) {
        mgi.notFlag = true;
      }
      this.msgGroup.items.push(mgi);
    });
    this.users.forEach(sel => {
      const mgi = new MessageGroupItem();
      mgi.guid = sel.guid;
      mgi.itemType = 'USER';
      if (sel.notFlag) {
        mgi.notFlag = true;
      }
      this.msgGroup.items.push(mgi);
    });
    this.ou.forEach(sel => {
      const mgi = new MessageGroupItem();
      mgi.guid = sel.guid;
      mgi.itemType = 'OU';
      if (sel.notFlag) {
        mgi.notFlag = true;
      }
      this.msgGroup.items.push(mgi);
    });
    this.mGroups.forEach(sel => {
      const mgi = new MessageGroupItem();
      mgi.msgGroupId = sel.id;
      mgi.itemType = 'MSG_GROUP';
      if (sel.notFlag) {
        mgi.notFlag = true;
      }
      this.msgGroup.items.push(mgi);
    });
    return this.msgGroup;
  }

}
