import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './auth/login/login.component';
import {RegistrationComponent} from './auth/registration/registration.component';
import {ActivateComponent} from "./auth/registration/activate/activate.component";
import {DashboardComponent} from "./dashboard/dashboard.component";

const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'activate', component: ActivateComponent},
    {path: 'dashboard', component: DashboardComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
