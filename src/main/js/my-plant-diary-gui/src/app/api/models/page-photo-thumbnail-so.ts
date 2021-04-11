/* tslint:disable */
/* eslint-disable */
import { Pageable } from './pageable';
import { PhotoThumbnailSo } from './photo-thumbnail-so';
import { Sort } from './sort';
export interface PagePhotoThumbnailSo {
  content?: Array<PhotoThumbnailSo>;
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
