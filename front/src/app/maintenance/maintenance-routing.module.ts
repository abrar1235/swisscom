import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { AuthGuard } from '../auth.guard';
import { ManageComponent } from './manage/manage.component';
import { ViewComponent } from './view/view.component';
const routes: Route[] = [
    {
        path: 'view',
        component: ViewComponent
    },
    {
        path: 'manage',
        component: ManageComponent,
        canActivate: [AuthGuard]
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

export class MaintenanceRoutingModule { }