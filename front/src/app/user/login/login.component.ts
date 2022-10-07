import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  inProgress: boolean = false;
  error: string = '';
  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private router: Router,
    private title: Title
  ) { }

  ngOnInit(): void {
    this.title.setTitle('Login');
  }

  loginForm = this.formBuilder.group({
    'email': ['', [Validators.required, Validators.email]],
    'password': ['', [Validators.required, Validators.minLength(8)]]
  });

  login() {
    let credentials = {
      'email': this.loginForm.get('email')?.value,
      'password': this.loginForm.get('password')?.value
    }
    this.inProgress = true;
    this.loginForm.disable();
    this.userService.login(credentials).subscribe({
      next: response => {
        if (response.status === 'success') {
          let user = response.success;
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('token', user.token);
          let nextUrl = localStorage.getItem('nextUrl');
          this.router.navigate([nextUrl ? nextUrl : '/user/manage']);
          this.inProgress = false;
          this.loginForm.enable();
          this.loginForm.reset();
          localStorage.removeItem('nextUrl');
        }
        else {
          this.inProgress = false;
          this.loginForm.enable();
          this.loginForm.get('password')?.setValue('');
          this.error = response.failure.error;
        }
      },
      error: error => {
        console.log(error);
        this.error = error.failre.error;
      }
    });
  }
}
