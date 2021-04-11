/* tslint:disable */
/* eslint-disable */
import { EventTypeSo } from './event-type-so';
import { PlantBasicSo } from './plant-basic-so';
export interface CreateScheduleSo {
  autoUpdate: boolean;
  next: string;
  plant: PlantBasicSo;
  repeatEvery?: number;
  type: EventTypeSo;
}
