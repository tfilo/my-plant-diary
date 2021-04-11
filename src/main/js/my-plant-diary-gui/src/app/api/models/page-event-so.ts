/* tslint:disable */
/* eslint-disable */
import { EventSo } from './event-so';
import { Pageable } from './pageable';
import { Sort } from './sort';
export interface PageEventSo {
  content?: Array<EventSo>;
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
