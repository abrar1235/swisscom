import { Component, OnInit } from '@angular/core';
import { AppService } from 'src/app/app.service';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit {

  maintenance!: any[];
  hasMore: boolean = true;
  index = 0;
  constructor(
    private appService: AppService
  ) { }

  ngOnInit(): void {
    this.fetchMore();
  }

  fetchMore() {
    this.appService.getUpcomingMaintenance(this.index).subscribe({
      next: response => {
        if (response.status === 'success') {
          response.success.forEach((main: any) => this.maintenance.push(main));
          this.index += response.success.length;
          this.hasMore = response.success.length > 0;
        } else {
          alert('Something Went Wrong');
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
      }
    });
  }
}
