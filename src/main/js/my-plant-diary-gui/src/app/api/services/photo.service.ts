/* tslint:disable */
/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';
import { RequestBuilder } from '../request-builder';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { CreatePhotoSo } from '../models/create-photo-so';
import { PagePhotoThumbnailSo } from '../models/page-photo-thumbnail-so';
import { PhotoSo } from '../models/photo-so';
import { PhotoThumbnailSo } from '../models/photo-thumbnail-so';
import { UpdatePhotoSo } from '../models/update-photo-so';


/**
 * Photo for plants endpoint
 */
@Injectable({
  providedIn: 'root',
})
export class PhotoService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation update2
   */
  static readonly Update2Path = '/api/photo';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2$Response(params: {
    body: UpdatePhotoSo
  }): Observable<StrictHttpResponse<PhotoThumbnailSo>> {

    const rb = new RequestBuilder(this.rootUrl, PhotoService.Update2Path, 'put');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PhotoThumbnailSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `update2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2(params: {
    body: UpdatePhotoSo
  }): Observable<PhotoThumbnailSo> {

    return this.update2$Response(params).pipe(
      map((r: StrictHttpResponse<PhotoThumbnailSo>) => r.body as PhotoThumbnailSo)
    );
  }

  /**
   * Path part for operation create2
   */
  static readonly Create2Path = '/api/photo';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create2$Response(params: {
    body: { 'photo': Blob, 'createPhotoSO': CreatePhotoSo }
  }): Observable<StrictHttpResponse<PhotoThumbnailSo>> {

    const rb = new RequestBuilder(this.rootUrl, PhotoService.Create2Path, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PhotoThumbnailSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create2(params: {
    body: { 'photo': Blob, 'createPhotoSO': CreatePhotoSo }
  }): Observable<PhotoThumbnailSo> {

    return this.create2$Response(params).pipe(
      map((r: StrictHttpResponse<PhotoThumbnailSo>) => r.body as PhotoThumbnailSo)
    );
  }

  /**
   * Path part for operation getOne2
   */
  static readonly GetOne2Path = '/api/photo/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOne2()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne2$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<PhotoSo>> {

    const rb = new RequestBuilder(this.rootUrl, PhotoService.GetOne2Path, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PhotoSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getOne2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne2(params: {
    id: number;
  }): Observable<PhotoSo> {

    return this.getOne2$Response(params).pipe(
      map((r: StrictHttpResponse<PhotoSo>) => r.body as PhotoSo)
    );
  }

  /**
   * Path part for operation delete3
   */
  static readonly Delete3Path = '/api/photo/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete3()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete3$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<number>> {

    const rb = new RequestBuilder(this.rootUrl, PhotoService.Delete3Path, 'delete');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return (r as HttpResponse<any>).clone({ body: parseFloat(String((r as HttpResponse<any>).body)) }) as StrictHttpResponse<number>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `delete3$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete3(params: {
    id: number;
  }): Observable<number> {

    return this.delete3$Response(params).pipe(
      map((r: StrictHttpResponse<number>) => r.body as number)
    );
  }

  /**
   * Path part for operation getAllByPlantId
   */
  static readonly GetAllByPlantIdPath = '/api/photo/all/{plantId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllByPlantId()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByPlantId$Response(params: {
    plantId: number;
    page: number;
    pageSize: number;
  }): Observable<StrictHttpResponse<PagePhotoThumbnailSo>> {

    const rb = new RequestBuilder(this.rootUrl, PhotoService.GetAllByPlantIdPath, 'get');
    if (params) {
      rb.path('plantId', params.plantId, {});
      rb.query('page', params.page, {});
      rb.query('pageSize', params.pageSize, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PagePhotoThumbnailSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAllByPlantId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByPlantId(params: {
    plantId: number;
    page: number;
    pageSize: number;
  }): Observable<PagePhotoThumbnailSo> {

    return this.getAllByPlantId$Response(params).pipe(
      map((r: StrictHttpResponse<PagePhotoThumbnailSo>) => r.body as PagePhotoThumbnailSo)
    );
  }

}
