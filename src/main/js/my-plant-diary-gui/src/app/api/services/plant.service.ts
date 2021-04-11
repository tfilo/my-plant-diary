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

import { CreatePlantSo } from '../models/create-plant-so';
import { PagePlantSo } from '../models/page-plant-so';
import { PlantSo } from '../models/plant-so';
import { PlantTypeSo } from '../models/plant-type-so';


/**
 * Plant endpoint
 */
@Injectable({
  providedIn: 'root',
})
export class PlantService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation getAll1
   */
  static readonly GetAll1Path = '/api/plant';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll1$Response(params: {
    deleted?: boolean;
    page: number;
    pageSize: number;
  }): Observable<StrictHttpResponse<PagePlantSo>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.GetAll1Path, 'get');
    if (params) {
      rb.query('deleted', params.deleted, {});
      rb.query('page', params.page, {});
      rb.query('pageSize', params.pageSize, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PagePlantSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAll1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll1(params: {
    deleted?: boolean;
    page: number;
    pageSize: number;
  }): Observable<PagePlantSo> {

    return this.getAll1$Response(params).pipe(
      map((r: StrictHttpResponse<PagePlantSo>) => r.body as PagePlantSo)
    );
  }

  /**
   * Path part for operation update1
   */
  static readonly Update1Path = '/api/plant';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1$Response(params: {
    body: PlantSo
  }): Observable<StrictHttpResponse<PlantSo>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.Update1Path, 'put');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PlantSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `update1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1(params: {
    body: PlantSo
  }): Observable<PlantSo> {

    return this.update1$Response(params).pipe(
      map((r: StrictHttpResponse<PlantSo>) => r.body as PlantSo)
    );
  }

  /**
   * Path part for operation create1
   */
  static readonly Create1Path = '/api/plant';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1$Response(params: {
    body: CreatePlantSo
  }): Observable<StrictHttpResponse<PlantSo>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.Create1Path, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PlantSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1(params: {
    body: CreatePlantSo
  }): Observable<PlantSo> {

    return this.create1$Response(params).pipe(
      map((r: StrictHttpResponse<PlantSo>) => r.body as PlantSo)
    );
  }

  /**
   * Path part for operation getOne1
   */
  static readonly GetOne1Path = '/api/plant/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOne1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne1$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<PlantSo>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.GetOne1Path, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PlantSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getOne1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne1(params: {
    id: number;
  }): Observable<PlantSo> {

    return this.getOne1$Response(params).pipe(
      map((r: StrictHttpResponse<PlantSo>) => r.body as PlantSo)
    );
  }

  /**
   * Path part for operation delete2
   */
  static readonly Delete2Path = '/api/plant/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete2()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<number>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.Delete2Path, 'delete');
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
   * To access the full response (for headers, for example), `delete2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2(params: {
    id: number;
  }): Observable<number> {

    return this.delete2$Response(params).pipe(
      map((r: StrictHttpResponse<number>) => r.body as number)
    );
  }

  /**
   * Path part for operation getAllTypes
   */
  static readonly GetAllTypesPath = '/api/plant/type';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllTypes()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTypes$Response(params?: {
  }): Observable<StrictHttpResponse<Array<PlantTypeSo>>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.GetAllTypesPath, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<PlantTypeSo>>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAllTypes$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTypes(params?: {
  }): Observable<Array<PlantTypeSo>> {

    return this.getAllTypes$Response(params).pipe(
      map((r: StrictHttpResponse<Array<PlantTypeSo>>) => r.body as Array<PlantTypeSo>)
    );
  }

  /**
   * Path part for operation getAllByLocation
   */
  static readonly GetAllByLocationPath = '/api/plant/by-location';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllByLocation()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByLocation$Response(params: {
    locationId?: number;
    deleted?: boolean;
    page: number;
    pageSize: number;
  }): Observable<StrictHttpResponse<PagePlantSo>> {

    const rb = new RequestBuilder(this.rootUrl, PlantService.GetAllByLocationPath, 'get');
    if (params) {
      rb.query('locationId', params.locationId, {});
      rb.query('deleted', params.deleted, {});
      rb.query('page', params.page, {});
      rb.query('pageSize', params.pageSize, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PagePlantSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAllByLocation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByLocation(params: {
    locationId?: number;
    deleted?: boolean;
    page: number;
    pageSize: number;
  }): Observable<PagePlantSo> {

    return this.getAllByLocation$Response(params).pipe(
      map((r: StrictHttpResponse<PagePlantSo>) => r.body as PagePlantSo)
    );
  }

}
