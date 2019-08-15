import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MessageEditComponent} from './message/message-edit/message-edit.component';
import {GroupsComponent} from './groups/group-edit/groups.component';
import {MessageListComponent} from './message/message-list/message-list.component';
import {GroupsListComponent} from './groups/groups-list/groups-list.component';

const routes: Routes = [
  { path: 'messages/:msgId', component: MessageEditComponent},
  { path: 'groups/:mgId', component: GroupsComponent},
  { path: 'messages', component: MessageListComponent},
  { path: 'groups', component: GroupsListComponent},
  { path: '', component: MessageListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
