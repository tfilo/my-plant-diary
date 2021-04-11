/* tslint:disable */
/* eslint-disable */
import { EventTypeSo } from './event-type-so';
import { PlantBasicSo } from './plant-basic-so';
export interface ScheduleSo {
  autoUpdate: boolean;
  id: number;
  next: string;
  plant: PlantBasicSo;
  repeatEvery?: number;
  type: EventTypeSo;
}
