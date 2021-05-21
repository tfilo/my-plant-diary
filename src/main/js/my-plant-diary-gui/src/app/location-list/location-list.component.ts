import {Component, OnInit} from '@angular/core';
import {LocationService} from "@api/api/location.service";
import {Location} from "@api/model/location";
import {MatDialog} from "@angular/material/dialog";
import {LocationComponent} from "./location/location.component";

@Component({
    selector: 'app-location-list',
    templateUrl: './location-list.component.html',
    styleUrls: ['./location-list.component.scss']
})
export class LocationListComponent implements OnInit {

    locations: Array<Location> = [];

    constructor(
        private locationService: LocationService,
        private dialog: MatDialog) {
    }

    ngOnInit(): void {
        this.locationService.getAllLocations().subscribe(locations => {
            this.locations = locations;
        });
    }

    onAddLocation(): void {
        let dialogRef = this.dialog.open(LocationComponent, {
            width: '600px',
            data: {}
        });

        dialogRef.afterClosed().subscribe(data => {
            console.log(data);
            if (data) {
                this.locations.push(data);
            }
        });
    }

    onUpdateLocation(event: Event,location: Location): void {
        event.stopPropagation();
        let dialogRef = this.dialog.open(LocationComponent, {
            width: '600px',
            data: location
        });

        dialogRef.afterClosed().subscribe(data => {
            console.log(data);
            if (data) {
                const location = this.locations.find(l => l.id === data.id);
                if (location) {
                    location.name = data.name;
                }
            }
        });
    }
}
