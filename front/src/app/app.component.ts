import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PrimeNGConfig } from 'primeng/api';
import { MenuItem } from 'primeng/api';
import { WebSocketService } from './websocket/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'maintenance-front';
  menu: MenuItem[] = [];

  constructor(
    private primeNgConfig: PrimeNGConfig,
    private websocketService: WebSocketService,
    private router: Router
  ) {
    this.primeNgConfig.ripple = true;
  }
  ngOnInit(): void {
    this.menu = [
      {
        label: 'Home',
        routerLink: '/'
      },
      {
        label: 'Upcoming Maintenance',
        routerLink: '/maintenance/view'
      },
      {
        label: 'Manage User',
        routerLink: '/user/manage'
      },
      {
        label: 'Logout',
        command: () => this.logout()
      }
    ];

    this.websocketService.connect('', true).subscribe(x => console.log(x));
  }

  logout() {
    let token = localStorage.getItem('token');
    if (token) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}
