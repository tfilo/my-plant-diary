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

import { CreateLocationSo } from '../models/create-location-so';
import { LocationSo } from '../models/location-so';


/**
 * Plant locations endpoint
 */
@Injectable({
  providedIn: 'root',
})
export class LocationService extends BaseService {
  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * Path part for operation getAll2
   */
  static readonly GetAll2Path = '/api/location';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll2()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll2$Response(params?: {
  }): Observable<StrictHttpResponse<Array<LocationSo>>> {

    const rb = new RequestBuilder(this.rootUrl, LocationService.GetAll2Path, 'get');
    if (params) {
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<Array<LocationSo>>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getAll2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll2(params?: {
  }): Observable<Array<LocationSo>> {

    return this.getAll2$Response(params).pipe(
      map((r: StrictHttpResponse<Array<LocationSo>>) => r.body as Array<LocationSo>)
    );
  }

  /**
   * Path part for operation update3
   */
  static readonly Update3Path = '/api/location';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update3()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update3$Response(params: {
    body: LocationSo
  }): Observable<StrictHttpResponse<LocationSo>> {

    const rb = new RequestBuilder(this.rootUrl, LocationService.Update3Path, 'put');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<LocationSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `update3$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update3(params: {
    body: LocationSo
  }): Observable<LocationSo> {

    return this.update3$Response(params).pipe(
      map((r: StrictHttpResponse<LocationSo>) => r.body as LocationSo)
    );
  }

  /**
   * Path part for operation create3
   */
  static readonly Create3Path = '/api/location';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create3()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create3$Response(params: {
    body: CreateLocationSo
  }): Observable<StrictHttpResponse<LocationSo>> {

    const rb = new RequestBuilder(this.rootUrl, LocationService.Create3Path, 'post');
    if (params) {
      rb.body(params.body, 'application/json');
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<LocationSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `create3$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create3(params: {
    body: CreateLocationSo
  }): Observable<LocationSo> {

    return this.create3$Response(params).pipe(
      map((r: StrictHttpResponse<LocationSo>) => r.body as LocationSo)
    );
  }

  /**
   * Path part for operation getOne3
   */
  static readonly GetOne3Path = '/api/location/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOne3()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne3$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<LocationSo>> {

    const rb = new RequestBuilder(this.rootUrl, LocationService.GetOne3Path, 'get');
    if (params) {
      rb.path('id', params.id, {});
    }

    return this.http.request(rb.build({
      responseType: 'blob',
      accept: '*/*'
    })).pipe(
      filter((r: any) => r instanceof HttpResponse),
      map((r: HttpResponse<any>) => {
        return r as StrictHttpResponse<LocationSo>;
      })
    );
  }

  /**
   * This method provides access to only to the response body.
   * To access the full response (for headers, for example), `getOne3$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOne3(params: {
    id: number;
  }): Observable<LocationSo> {

    return this.getOne3$Response(params).pipe(
      map((r: StrictHttpResponse<LocationSo>) => r.body as LocationSo)
    );
  }

  /**
   * Path part for operation delete4
   */
  static readonly Delete4Path = '/api/location/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete4()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4$Response(params: {
    id: number;
  }): Observable<StrictHttpResponse<number>> {

    const rb = new RequestBuilder(this.rootUrl, LocationService.Delete4Path, 'delete');
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
   * To access the full response (for headers, for example), `delete4$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete4(params: {
    id: number;
  }): Observable<number> {

    return this.delete4$Response(params).pipe(
      map((r: StrictHttpResponse<number>) => r.body as number)
    );
  }

}
