import {Component, ElementRef, ViewChild} from '@angular/core';
import {faBars} from '@fortawesome/free-solid-svg-icons';
import {AuthService} from "./auth/auth.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    faBars = faBars;

    @ViewChild('sidebar')
    sidebar: ElementRef | null = null;

    @ViewChild('mainContent')
    mainContent: ElementRef | null = null;

    constructor(private authService: AuthService) {
    }

    get authenticated(): boolean {
        return this.authService.isAuthenticated();
    }

    sidebarToggle() {
        // w3.css has defined 992px for w3-collapse
        if (window.innerWidth <= 992 && this.sidebar && this.mainContent) {
            if (this.sidebar.nativeElement.style.display !== 'block') {
                this.sidebar.nativeElement.style.display = 'block';
            } else {
                this.sidebar.nativeElement.style.display = 'none';
            }
        }
    }

}
