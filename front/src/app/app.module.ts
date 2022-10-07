import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RippleModule } from 'primeng/ripple';
import { ButtonModule } from 'primeng/button';
import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { AppService } from './app.service';
import { MenubarModule } from 'primeng/menubar';
import { WebSocketService } from './websocket/websocket.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    RippleModule,
    ButtonModule,
    AppRoutingModule,
    HttpClientModule,
    MenubarModule,
    BrowserAnimationsModule
  ],
  providers: [AppService, WebSocketService],
  bootstrap: [AppComponent]
})
export class AppModule { }