import { NgModule } from '@angular/core';
import { Route, RouterModule } from '@angular/router';
import { ViewComponent } from './view/view.component';
const routes: Route[] = [
    {
        path: 'view',
        component: ViewComponent
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