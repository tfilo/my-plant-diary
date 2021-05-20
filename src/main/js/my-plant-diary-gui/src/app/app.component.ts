import {Component} from '@angular/core';
import {AuthService} from "./auth/auth.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    constructor(
        private authService: AuthService
    ) {

    }

    get authenticated(): boolean {
        return this.authService.isAuthenticated();
    }

    logout(): void {
        this.authService.logout();
    }
}
