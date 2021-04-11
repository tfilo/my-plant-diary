/* tslint:disable */
/* eslint-disable */
import { EventTypeSo } from './event-type-so';
import { PlantBasicSo } from './plant-basic-so';
export interface CreateEventSo {
  dateTime: string;
  description?: string;
  plant: PlantBasicSo;
  type: EventTypeSo;
}
