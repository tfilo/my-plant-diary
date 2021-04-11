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

import { CreateScheduleSo } from '../models/create-schedule-so';
import { PageScheduleSo } from '../models/page-schedule-so';
import { ScheduleSo } from '../models/schedule-so';


/**
 * Schedule for plant care endpoint
 */
@Injectable({
  providedIn: 'root',
})
export class ScheduleService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation getAll
   */
  static readonly GetAllPath = '/api/schedule';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll$Response(params: {
    plantId?: number;
    page: number;
    pageSize: number;
  }): Observable<StrictHttpResponse<PageScheduleSo>> {

    const rb = new RequestBuilder(this.rootUrl, ScheduleService.GetAllPath, 'get');
    if (params) {
      rb.query('plantId', params.plantId, {});
      rb.query('page', params.page, {});
      rb.query('pageSize', params.pageSize, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<PageScheduleSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll(params: {
    plantId?: number;
    page: number;
    pageSize: number;
  }): Observable<PageScheduleSo> {

    return this.getAll$Response(params).pipe(
      map((r: StrictHttpResponse<PageScheduleSo>) => r.body as PageScheduleSo)
    );
  }

  /**
   * Path part for operation update
   */
  static readonly UpdatePath = '/api/schedule';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update$Response(params: {
    body: ScheduleSo
  }): Observable<StrictHttpResponse<ScheduleSo>> {

    const rb = new RequestBuilder(this.rootUrl, ScheduleService.UpdatePath, 'put');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<ScheduleSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `update$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update(params: {
    body: ScheduleSo
  }): Observable<ScheduleSo> {

    return this.update$Response(params).pipe(
      map((r: StrictHttpResponse<ScheduleSo>) => r.body as ScheduleSo)
    );
  }

  /**
   * Path part for operation create
   */
  static readonly CreatePath = '/api/schedule';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create$Response(params: {
    body: CreateScheduleSo
  }): Observable<StrictHttpResponse<ScheduleSo>> {

    const rb = new RequestBuilder(this.rootUrl, ScheduleService.CreatePath, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<ScheduleSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create(params: {
    body: CreateScheduleSo
  }): Observable<ScheduleSo> {

    return this.create$Response(params).pipe(
      map((r: StrictHttpResponse<ScheduleSo>) => r.body as ScheduleSo)
    );
  }

  /**
   * Path part for operation getOne
   */
  static readonly GetOnePath = '/api/schedule/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOne()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<ScheduleSo>> {

    const rb = new RequestBuilder(this.rootUrl, ScheduleService.GetOnePath, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<ScheduleSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getOne$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne(params: {
    id: number;
  }): Observable<ScheduleSo> {

    return this.getOne$Response(params).pipe(
      map((r: StrictHttpResponse<ScheduleSo>) => r.body as ScheduleSo)
    );
  }

  /**
   * Path part for operation delete1
   */
  static readonly Delete1Path = '/api/schedule/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete1()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<number>> {

    const rb = new RequestBuilder(this.rootUrl, ScheduleService.Delete1Path, 'delete');
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
   * To access the full response (for headers, for example), `delete1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1(params: {
    id: number;
  }): Observable<number> {

    return this.delete1$Response(params).pipe(
      map((r: StrictHttpResponse<number>) => r.body as number)
    );
  }

}
