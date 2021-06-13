import {Component, OnInit} from '@angular/core';
import {LocationComponent} from "../location-list/location/location.component";
import {MatDialog} from "@angular/material/dialog";
import {PlantComponent} from "./plant/plant.component";
import {PlantService} from "@api/api/plant.service";
import {Plant} from "@api/model/plant";
import {Location} from "@api/model/location";

@Component({
    selector: 'app-plant-list',
    templateUrl: './plant-list.component.html',
    styleUrls: ['./plant-list.component.scss']
})
export class PlantListComponent implements OnInit {

    plants?: Array<Plant> = [];

    constructor(
        private dialog: MatDialog,
        private plantService: PlantService
    ) {
    }

    ngOnInit(): void {
        this.loadPlants();
    }

    private loadPlants() {
        this.plantService.getAllPlants(0,10000).subscribe(page => {
            this.plants = page.content;
        });
    };

    onAddPlant(): void {
        let dialogRef = this.dialog.open(PlantComponent, {
            width: '600px',
            data: {}
        });

        dialogRef.afterClosed().subscribe(data => {
            this.loadPlants();
        });
    }

    onUpdatePlant(plant: Plant): void {
        let dialogRef = this.dialog.open(PlantComponent, {
            width: '600px',
            data: plant
        });

        dialogRef.afterClosed().subscribe(data => {
            console.log(data);
            if (data) {
                const plant = this.plants?.find(l => l.id === data.id);
                if (plant) {
                    Object.assign(plant, data);
                }
            }
        });
    }

    onWattering() {

    }

    onFertilizing() {

    }

    repot() {

    }
}
