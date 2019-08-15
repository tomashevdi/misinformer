import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {
  ButtonModule,
  CheckboxModule, DialogService,
  EditorModule,
  InputTextModule,
  MenuItem, PasswordModule,
  PickListModule,
  TabMenuModule,
  TabViewModule
} from 'primeng/primeng';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ADService} from './services/ad.service';
import {GroupsComponent} from './groups/group-edit/groups.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageGroupService} from './services/mg.service';
import { MessageListComponent } from './message/message-list/message-list.component';
import {MessageService} from './services/msg.service';
import {MessageService as MessageServiceNG} from 'primeng/api';
import { MessageEditComponent } from './message/message-edit/message-edit.component';
import {ToastModule} from 'primeng/toast';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TableModule} from 'primeng/table';
import { GroupsListComponent } from './groups/groups-list/groups-list.component';
import {LoginComponent} from './login/login.component';
import {AuthenticationService} from './services/authentication.service';
import {JwtInterceptor} from './interceptors/jwt.interceptor';
import {DynamicDialogModule} from 'primeng/dynamicdialog';
import {MsgFilterPipe} from './message/message-list/message-list.pipe';

@NgModule({
  declarations: [
    AppComponent,
    GroupsComponent,
    MessageListComponent,
    MessageEditComponent,
    GroupsListComponent,
    LoginComponent,
    MsgFilterPipe
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    PickListModule,
    CheckboxModule,
    InputTextModule,
    ButtonModule,
    TabViewModule,
    EditorModule,
    ToastModule,
    TableModule,
    TabMenuModule,
    DynamicDialogModule,
    PasswordModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    AuthenticationService, ADService, MessageGroupService, MessageService, MessageServiceNG, DialogService],
  bootstrap: [AppComponent],
  entryComponents: [LoginComponent]
})
export class AppModule { }
