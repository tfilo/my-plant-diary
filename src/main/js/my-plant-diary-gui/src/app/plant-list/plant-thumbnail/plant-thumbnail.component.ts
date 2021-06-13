import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Plant} from "@api/model/plant";

@Component({
    selector: 'app-plant-thumbnail',
    templateUrl: './plant-thumbnail.component.html',
    styleUrls: ['./plant-thumbnail.component.scss']
})
export class PlantThumbnailComponent implements OnInit {

    @Input() plant?: Plant;
    @Output() onEdit: EventEmitter<Plant> = new EventEmitter();

    constructor() {
    }

    ngOnInit(): void {
    }

    onPreviewPlant(): void {

    }

    onEditPlant(): void {
        this.onEdit.emit(this.plant);
    }
}
