import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Location} from "@api/model/location";
import {LocationService} from "@api/api/location.service";
import {PlantService} from "@api/api/plant.service";
import {Plant} from "@api/model/plant";
import {PlantType} from "@api/model/plant-type";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-plant',
  templateUrl: './plant.component.html',
  styleUrls: ['./plant.component.scss']
})
export class PlantComponent implements OnInit {

    plantForm: FormGroup;
    plantTypes: Array<PlantType> = [];
    locations: Array<Location> = [];

    constructor(
        public dialogRef: MatDialogRef<PlantComponent>,
        @Inject(MAT_DIALOG_DATA) public data: Plant,
        private plantService: PlantService,
        private locationService: LocationService
    ) {
        this.plantForm = new FormGroup({
            id: new FormControl(null),
            name: new FormControl(null, [Validators.maxLength(100), Validators.required]),
            description: new FormControl(null, Validators.maxLength(1000)),
            type: new FormControl(null, Validators.required),
            location: new FormControl(null, Validators.required),
            deleted: new FormControl(null)
        });
    }

    ngOnInit(): void {
        forkJoin([
            this.plantService.getAllPlantTypes(),
            this.locationService.getAllLocations()
        ]).subscribe(([types, locations]) => {
            this.plantTypes = types;
            this.locations = locations;
            this.plantForm.patchValue(this.data);
        });
    }

    onSave(): void {
        (this.data.id ? this.plantService.updatePlant({...this.plantForm.value}) : this.plantService.createPlant({...this.plantForm.value}))
            .subscribe(data=> {
                this.dialogRef.close(data);
            });
    }
    compareById(o1: any, o2: any): boolean {
        return o1.id === o2.id;
    }

}
