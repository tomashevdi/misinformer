import { Component, OnInit } from '@angular/core';
import {MessageGroupService} from '../../services/mg.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-groups-list',
  templateUrl: './groups-list.component.html',
  styleUrls: ['./groups-list.component.css']
})
export class GroupsListComponent implements OnInit {

  cols: any[] = [
    { field: 'name', header: 'Название' },
    { field: 'comment', header: 'Примечание' }
  ];

  constructor(public mgs: MessageGroupService, private router: Router) { }

  ngOnInit() {
  }

  onNew() {
    this.router.navigate(['/groups', 'new']);
  }

  onRefresh() {
    this.mgs.loadData();
  }

}
