import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ViewComponent } from './view/view.component';
import { MaintenanceRoutingModule } from './maintenance-routing.module';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { DialogModule } from 'primeng/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { MaintenanceService } from './maintenance.service';
import { HttpTokenInterceptorProvider } from '../http-interceptor-provider';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [
    ViewComponent
  ],
  imports: [
    CommonModule,
    MaintenanceRoutingModule,
    TableModule,
    ButtonModule,
    RippleModule,
    DialogModule,
    ReactiveFormsModule,
    InputTextModule,
    CalendarModule,
    HttpClientModule
  ],
  providers: [MaintenanceService, HttpTokenInterceptorProvider]
})
export class MaintenanceModule { }
