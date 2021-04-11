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

import { CreateEventSo } from '../models/create-event-so';
import { EventSo } from '../models/event-so';
import { EventTypeSo } from '../models/event-type-so';
import { PageEventSo } from '../models/page-event-so';
import { UpdateEventSo } from '../models/update-event-so';


/**
 * Plant events endpoint
 */
@Injectable({
  providedIn: 'root',
})
export class EventService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation update4
   */
  static readonly Update4Path = '/api/event';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update4()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update4$Response(params: {
    body: UpdateEventSo
  }): Observable<StrictHttpResponse<EventSo>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.Update4Path, 'put');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<EventSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `update4$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update4(params: {
    body: UpdateEventSo
  }): Observable<EventSo> {

    return this.update4$Response(params).pipe(
      map((r: StrictHttpResponse<EventSo>) => r.body as EventSo)
    );
  }

  /**
   * Path part for operation create4
   */
  static readonly Create4Path = '/api/event';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create4()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create4$Response(params: {
    body: CreateEventSo
  }): Observable<StrictHttpResponse<EventSo>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.Create4Path, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<EventSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create4$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create4(params: {
    body: CreateEventSo
  }): Observable<EventSo> {

    return this.create4$Response(params).pipe(
      map((r: StrictHttpResponse<EventSo>) => r.body as EventSo)
    );
  }

  /**
   * Path part for operation getOne4
   */
  static readonly GetOne4Path = '/api/event/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOne4()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne4$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<EventSo>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.GetOne4Path, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<EventSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getOne4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne4(params: {
    id: number;
  }): Observable<EventSo> {

    return this.getOne4$Response(params).pipe(
      map((r: StrictHttpResponse<EventSo>) => r.body as EventSo)
    );
  }

  /**
   * Path part for operation delete5
   */
  static readonly Delete5Path = '/api/event/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete5()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<string>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.Delete5Path, 'delete');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<string>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `delete5$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete5(params: {
    id: number;
  }): Observable<string> {

    return this.delete5$Response(params).pipe(
      map((r: StrictHttpResponse<string>) => r.body as string)
    );
  }

  /**
   * Path part for operation getAllTypes1
   */
  static readonly GetAllTypes1Path = '/api/event/type';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllTypes1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTypes1$Response(params?: {
  }): Observable<StrictHttpResponse<Array<EventTypeSo>>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.GetAllTypes1Path, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<EventTypeSo>>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAllTypes1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllTypes1(params?: {
  }): Observable<Array<EventTypeSo>> {

    return this.getAllTypes1$Response(params).pipe(
      map((r: StrictHttpResponse<Array<EventTypeSo>>) => r.body as Array<EventTypeSo>)
    );
  }

  /**
   * Path part for operation getAllByPlantId1
   */
  static readonly GetAllByPlantId1Path = '/api/event/all/{plantId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllByPlantId1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByPlantId1$Response(params: {
    plantId: number;
    page: number;
    pageSize: number;
  }): Observable<StrictHttpResponse<PageEventSo>> {

    const rb = new RequestBuilder(this.rootUrl, EventService.GetAllByPlantId1Path, 'get');
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
        return r as StrictHttpResponse<PageEventSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAllByPlantId1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllByPlantId1(params: {
    plantId: number;
    page: number;
    pageSize: number;
  }): Observable<PageEventSo> {

    return this.getAllByPlantId1$Response(params).pipe(
      map((r: StrictHttpResponse<PageEventSo>) => r.body as PageEventSo)
    );
  }

}
