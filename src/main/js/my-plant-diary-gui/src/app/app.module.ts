import {Inject, Injectable, NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './auth/login/login.component';
import {RegistrationComponent} from './auth/registration/registration.component';
import {HttpClientModule, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";

import { environment } from '../environments/environment';
import { ActivateComponent } from './auth/registration/activate/activate.component';
import {ApiModule} from "@api/api.module";
import {BASE_PATH} from "@api/variables";
import { DashboardComponent } from './dashboard/dashboard.component';
import {AuthService} from "./auth/auth.service";
import {Observable} from "rxjs";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(private authService: AuthService, @Inject(BASE_PATH) private basePath: string) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.startsWith(this.basePath)) {
            const token = this.authService.getToken();
            if (token) {
                req = req.clone({
                    headers: req.headers.set('Authorization', 'Bearer ' + token)
                });
            }
        }
        return next.handle(req);
    }
}


@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegistrationComponent,
        ActivateComponent,
        DashboardComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FontAwesomeModule,
        ApiModule
    ],
    providers: [
        { provide: BASE_PATH, useValue: environment.baseUrl}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
