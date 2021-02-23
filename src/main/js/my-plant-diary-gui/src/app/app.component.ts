import { Component, ElementRef, ViewChild } from '@angular/core';
import { faBars } from '@fortawesome/free-solid-svg-icons';

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

    sidebarToggle() {
        if (this.sidebar && this.mainContent) {
            if (this.sidebar.nativeElement.style.display !== 'block') {
                this.sidebar.nativeElement.style.display = 'block';
            }  else {
                this.sidebar.nativeElement.style.display = 'none';
            }
        }
    }

}
