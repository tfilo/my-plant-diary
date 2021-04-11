/* tslint:disable */
/* eslint-disable */
import { Pageable } from './pageable';
import { ScheduleSo } from './schedule-so';
import { Sort } from './sort';
export interface PageScheduleSo {
  content?: Array<ScheduleSo>;
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
