import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LocationService} from "@api/api/location.service";
import {Location} from "@api/model/location";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-location',
    templateUrl: './location.component.html'
})
export class LocationComponent implements OnInit {

    locationForm: FormGroup;

    constructor(
        public dialogRef: MatDialogRef<LocationComponent>,
        @Inject(MAT_DIALOG_DATA) public data: Location,
        private locationService: LocationService
    ) {
        this.locationForm = new FormGroup({
            id: new FormControl(this.data.id),
            name: new FormControl(this.data.name, [Validators.maxLength(80), Validators.required])
        });
    }

    ngOnInit(): void {
    }

    onSave(): void {
        (this.data.id ? this.locationService.updateLocation({...this.locationForm.value}) : this.locationService.createLocation({...this.locationForm.value}))
            .subscribe(data=> {
                console.log(data);
                this.dialogRef.close(data);
            });
    }
}
