/* tslint:disable */
/* eslint-disable */
import { Pageable } from './pageable';
import { PlantSo } from './plant-so';
import { Sort } from './sort';
export interface PagePlantSo {
  content?: Array<PlantSo>;
  empty?: boolean;
  first?: boolean;
  last?: boolean;
  number?: number;
  numberOfElements?: number;
  pageable?: Pageable;
  size?: number;
  sort?: Sort;
  totalElements?: number;
  totalPages?: number;
}
