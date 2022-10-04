import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router'
import { AuthGuard } from '../auth.guard';
import { LoginComponent } from './login/login.component';
import { ManageComponent } from './manage/manage.component';
const routes: Route[] = [
    {
        path: 'manage',
        component: ManageComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'login',
        component: LoginComponent
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})

export class UserRoutingModule { }