import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { WebSocketService } from '../websocket/websocket.service';
import * as moment from 'moment';
import { Title } from '@angular/platform-browser';
import { Constants } from '../contants';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  maintenances!: any[];
  inProgressMaintenance: any = {};
  currentTime: any;
  socketConnected: boolean = false;
  inMaintenance: boolean = false;
  constructor(
    private appService: AppService,
    private websocketService: WebSocketService,
    private title: Title
  ) {
    setTimeout(() => this.listen(), 500);
  }

  ngOnInit(): void {
    this.appService.getUpcomingMaintenance(0).subscribe({
      next: response => {
        if (response.status === 'success') {
          this.maintenances = response.success;
          this.maintenanceListener();
        }
      }
    });

    setInterval(() => { this.maintenanceListener() }, 100);
    this.title.setTitle('Home');
  }

  listen() {
    if (this.socketConnected) return;
    this.websocketService.subscribe(Constants.TOPIC_ADD).subscribe({
      next: response => {
        this.socketConnected = true;
        this.maintenances.push(JSON.parse(response.body));
        this.maintenanceListener();
      }
    });
    this.websocketService.subscribe(Constants.TOPIC_REMOVE).subscribe({
      next: response => {
        let deleted = response.body;
        this.maintenanceListener(deleted);
        let index = this.maintenances.findIndex(x => x.id === deleted);
        this.maintenances.splice(index, 1);
      }
    });
  }

  private maintenanceListener(maintenanceId: any = '') {
    if (this.inMaintenance) {
      this.currentTime = new Date();
      if (moment().isAfter(this.inProgressMaintenance.endTime) || maintenanceId === this.inProgressMaintenance.id) this.inMaintenance = false;
    }
    else {
      for (let i = 0; i < this.maintenances?.length; i++) {
        let selected = this.maintenances[i];
        let start = moment(selected.startTime).format(Constants.DATE_FORMAT);
        let end = moment(selected.endTime).format(Constants.DATE_FORMAT);
        let current = moment().format(Constants.DATE_FORMAT);
        if (moment(current).isBetween(start, end)) {
          this.inMaintenance = true;
          this.inProgressMaintenance.startTime = selected.startTime;
          this.inProgressMaintenance.endTime = selected.endTime;
          this.inProgressMaintenance.id = selected.id;
          this.currentTime = new Date();
          break;
        }
      }
    }
  }
}
