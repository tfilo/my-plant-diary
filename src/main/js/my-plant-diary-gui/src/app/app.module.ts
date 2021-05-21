import {Inject, Injectable, NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './auth/login/login.component';
import {RegistrationComponent} from './auth/registration/registration.component';
import {
    HTTP_INTERCEPTORS,
    HttpClientModule,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from "@angular/common/http";

import {environment} from '../environments/environment';
import {ActivateComponent} from './auth/registration/activate/activate.component';
import {ApiModule} from "@api/api.module";
import {BASE_PATH} from "@api/variables";
import {DashboardComponent} from './dashboard/dashboard.component';
import {AuthService} from "./auth/auth.service";
import {Observable} from "rxjs";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {FlexLayoutModule} from '@angular/flex-layout';
import {LocationComponent} from './location-list/location/location.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatMenuModule} from '@angular/material/menu';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatExpansionModule} from '@angular/material/expansion';

import { UserComponent } from './user/user.component';
import { LocationListComponent } from './location-list/location-list.component';
import { PlantListComponent } from './plant-list/plant-list.component';
import { ScheduleListComponent } from './schedule-list/schedule-list.component';
import { PlantComponent } from './plant/plant.component';
import {MatDialogModule} from '@angular/material/dialog';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(@Inject(BASE_PATH) private basePath: string) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.startsWith(this.basePath)) {
            const token = localStorage.getItem('token');
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
        DashboardComponent,
        LocationComponent,
        UserComponent,
        LocationListComponent,
        PlantListComponent,
        ScheduleListComponent,
        PlantComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        FontAwesomeModule,
        ApiModule,
        BrowserAnimationsModule,
        MatSidenavModule,
        MatToolbarModule,
        MatIconModule,
        MatListModule,
        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        FlexLayoutModule,
        MatTabsModule,
        MatMenuModule,
        MatTooltipModule,
        MatExpansionModule,
        MatDialogModule
    ],
    providers: [
        {provide: BASE_PATH, useValue: environment.baseUrl},
        {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
