import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManageComponent } from './manage/manage.component';
import { ViewComponent } from './view/view.component';
import { MaintenanceRoutingModule } from './maintenance-routing.module';
import { AppService } from '../app.service';
import { MessageService } from 'primeng/api';
import { TableModule } from 'primeng/table';


@NgModule({
  declarations: [
    ManageComponent,
    ViewComponent
  ],
  imports: [
    CommonModule,
    MaintenanceRoutingModule,
    TableModule
  ],
  providers: [AppService, MessageService]
})
export class MaintenanceModule { }
