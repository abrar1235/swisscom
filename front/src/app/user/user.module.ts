import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserService } from './user.service';
import { HttpClientModule } from '@angular/common/http';
import { ManageComponent } from './manage/manage.component';
import { LoginComponent } from './login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { HttpTokenInterceptorProvider } from '../http-interceptor-provider';

@NgModule({
  declarations: [
    ManageComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
    ProgressSpinnerModule,
    TableModule,
    DialogModule
  ],
  providers: [UserService, HttpTokenInterceptorProvider]
})
export class UserModule { }
