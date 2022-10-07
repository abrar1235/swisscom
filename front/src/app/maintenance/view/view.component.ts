import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MaintenanceService } from '../maintenance.service';
import * as moment from 'moment';
import { Title } from '@angular/platform-browser';
import { Constants } from 'src/app/contants';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnInit {

  maintenance: any[] = [];
  hasMore: boolean = true;
  index = 0;
  scheduleMaintenancePopUp: boolean = false;
  inProgress: boolean = false;
  isAdmin = false;
  minDate = new Date();
  user!: any;
  constructor(
    private formBuilder: FormBuilder,
    private maintenanceService: MaintenanceService,
    private title: Title
  ) { }

  ngOnInit(): void {
    this.user = JSON.parse(localStorage.getItem('user') as any);
    this.isAdmin = this.user?.roles === 'ADMIN';
    this.fetchMore();
    this.title.setTitle('View Maintenance');
  }

  maintenanceForm = this.formBuilder.group({
    'description': [''],
    'startTime': ['', Validators.required],
    'endTime': ['', Validators.required]
  });

  fetchMore() {
    this.maintenanceService.getUpcomingMaintenance(this.index).subscribe({
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

  scheduleMaintenance() {
    let maintenance: any = {};
    maintenance.description = this.maintenanceForm.get('description')?.value;
    maintenance.startTime = this.maintenanceForm.get('startTime')?.value;
    maintenance.endTime = this.maintenanceForm.get('endTime')?.value;
    maintenance.userId = this.user.id;

    maintenance.startTime = moment(maintenance.startTime).format(Constants.DATE_FORMAT);
    maintenance.endTime = moment(maintenance.endTime).format(Constants.DATE_FORMAT);
    this.inProgress = true;
    this.maintenanceForm.disable();
    this.maintenanceService.addMaintenance(maintenance).subscribe({
      next: response => {
        if (response.status === 'success') {
          this.maintenance.unshift(response.success);
          this.scheduleMaintenancePopUp = false;
          this.maintenanceForm.enable();
          this.maintenanceForm.reset();
        } else {
          alert(response.failure.error);
          this.maintenanceForm.enable();
          this.inProgress = false;
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
        this.maintenanceForm.enable();
        this.inProgress = false;
      }
    });
  }

  delete(maintenance: any) {
    maintenance.deleting = true;
    this.maintenanceService.deleteMaintenance(maintenance.id).subscribe({
      next: response => {
        if (response.status === 'success') {
          let index = this.maintenance.indexOf(maintenance);
          this.maintenance.splice(index, 1);
        } else {
          alert(response.failure.error);
          maintenance.deleting = false;
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
        maintenance.deleting = false;
      }
    });
  }
}
