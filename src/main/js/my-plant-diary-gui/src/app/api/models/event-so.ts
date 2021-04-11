/* tslint:disable */
/* eslint-disable */
import { EventTypeSo } from './event-type-so';
import { PlantBasicSo } from './plant-basic-so';
export interface EventSo {
  dateTime: string;
  description?: string;
  id: number;
  plant: PlantBasicSo;
  type: EventTypeSo;
}
