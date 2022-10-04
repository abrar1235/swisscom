import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { WebSocketService } from '../websocket/websocket.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  maintenances!: any[];
  maintenanceStartTime: any = new Date();
  maintenanceEndTime: any = new Date();
  currentTime: any;
  socketConnected: boolean = false;
  constructor(
    private appService: AppService,
    private websocketService: WebSocketService
  ) { 
    setTimeout(() => this.listen(), 500);
  }

  ngOnInit(): void {
    this.appService.getUpcomingMaintenance(0).subscribe({
      next: response => {
        if (response.status === 'success') {
          this.maintenances = response.success;
        }
      }
    });

    setInterval(() => { this.currentTime = new Date() }, 100);
  }

  listen() {
    if (this.socketConnected) return;
    this.websocketService.subscribe('/topic/maintenance').subscribe({
      next: response => {
        this.socketConnected = true;
        this.maintenances.push(JSON.parse(response.body));
      }
    });
  }
}
