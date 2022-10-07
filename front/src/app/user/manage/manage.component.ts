import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.scss']
})
export class ManageComponent implements OnInit {

  users: any[] = [];
  hasMore: boolean = false;
  index = 0;
  addUserPopup: boolean = false;
  inProgress: boolean = false;
  user!: any;
  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private title: Title
  ) { }

  ngOnInit(): void {
    this.fetchUsers();
    this.user = JSON.parse(localStorage.getItem('user') as any);
    this.title.setTitle('Manage Users');
  }

  userForm = this.formBuilder.group({
    'name': ['', Validators.required],
    'email': ['', [Validators.required, Validators.email]],
    'password': ['', [Validators.required, Validators.minLength(8)]],
    'cPassword': ['', [Validators.required, Validators.minLength(8)]]
  });

  fetchUsers() {
    this.userService.getAllUsers(this.index).subscribe({
      next: response => {
        if (response.status === 'success') {
          response.success.forEach((res: any) => this.users.push(res));
          this.hasMore = response.success.length > 0;
          this.index += response.success.length;
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

  update(user: any) {
    if (this.user.id === user.id) {
      alert('You Cannot Update Self');
      return;
    }
    let updateUser = {
      id: user.id,
      status: user.status == 'INACTIVE' ? 'ACTIVE' : 'INACTIVE'
    };

    user.updating = true;
    this.userService.updateUser(updateUser).subscribe({
      next: response => {
        if (response.status === 'success') {
          let selected = this.users[this.users.indexOf(user)]
          selected.status = user.status == 'INACTIVE' ? 'ACTIVE' : 'INACTIVE';
          user.updating = false;
        } else {
          alert(response.failure.error);
          user.updating = false;
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
        user.updating = false;
      }
    })
  }

  delete(user: any) {
    if (this.user.id === user.id) {
      alert('You Cannot Update Self');
      return;
    }
    let answer = confirm('Are You Sure?');
    if(!answer) return;
    user.deleting = true;
    this.userService.deleteUser(user.id).subscribe({
      next: response => {
        if (response.status === 'success') {
          let index = this.users.indexOf(user);
          this.users.splice(index, 1);
        } else {
          alert(response.failure.error);
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
      }
    });
  }

  addUser() {

    if (this.userForm.get('password')?.value !== this.userForm.get('cPassword')?.value) {
      alert('Password Not Matching');
      this.userForm.get('password')?.setErrors(Validators.required);
      this.userForm.get('cPassword')?.setErrors(Validators.required);
      return;
    }

    let newUser = {
      'name': this.userForm.get('name')?.value,
      'email': this.userForm.get('email')?.value,
      'password': this.userForm.get('password')?.value,
      'roles': 'ADMIN'
    };

    this.userForm.disable();
    this.inProgress = true;
    this.userService.addUser(newUser).subscribe({
      next: response => {
        if (response.status === 'success') {
          this.users.push(response.success);
          this.userForm.enable();
          this.userForm.reset();
          this.inProgress = false;
          this.addUserPopup = false;
        } else {
          alert(response.failure.error);
          this.userForm.enable();
          this.inProgress = false;
        }
      },
      error: error => {
        console.log(error);
        alert('Internal Server Error');
        this.userForm.enable();
        this.inProgress = false;
      }
    });
  }
}
