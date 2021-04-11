/* tslint:disable */
/* eslint-disable */
import { LocationBasicSo } from './location-basic-so';
import { PlantTypeSo } from './plant-type-so';
export interface CreatePlantSo {
  description?: string;
  location?: LocationBasicSo;
  name: string;
  type?: PlantTypeSo;
}
