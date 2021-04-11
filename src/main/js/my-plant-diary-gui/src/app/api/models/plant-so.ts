/* tslint:disable */
/* eslint-disable */
import { LocationBasicSo } from './location-basic-so';
import { PlantTypeSo } from './plant-type-so';
export interface PlantSo {
  deleted: boolean;
  description?: string;
  id: number;
  location?: LocationBasicSo;
  name: string;
  type?: PlantTypeSo;
}
