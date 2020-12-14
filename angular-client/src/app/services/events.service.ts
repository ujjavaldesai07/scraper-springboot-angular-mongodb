import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {EVENTS_API_ROUTE, EVENTS_ATTRIBUTES_API_ROUTE} from '../constants/routes';
import {Observable, throwError} from 'rxjs';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {catchError, retry} from 'rxjs/operators';
import {Store} from '@ngrx/store';
import {IErrorAppState} from '../reducers/errorReducer';

/**
 * Service to handle make events collection APIs REST calls.
 */

@Injectable({
  providedIn: 'root'
})
export class EventsService {
  errorMsg: string;

  constructor(private store: Store<IErrorAppState>, private http: HttpClient) {
  }

  // handle errors
  errorHandler(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this.errorMsg = '503: Server Unreachable';
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      if (error.status === 0) {
        this.errorMsg = '503: Server Unreachable';
      } else if (error.status !== null && error.statusText !== null) {
        this.errorMsg = `${error.status}: ${error.statusText}`;
      }
    }
    // Return an observable with a user-facing error message.
    return throwError(this.errorMsg ||
      'Something bad happened; please try again later.');
  }

  /**
   * Gets all events if queryString is null else it will query based on the parameters
   * @param queryString: default will be null
   */
  getEvents(queryString = null): Observable<IEventsResponseData[]> {
    // @ts-ignore
    // check if we have query parameters and process accordingly
    return this.http
      .get<IEventsResponseData[]>(`${EVENTS_API_ROUTE}${queryString !== null ? `?${queryString}` : ''}`)
      .pipe(
        retry(2), // retry a failed request up to 2 times
        catchError(this.errorHandler) // then handle the error
      );
  }

  /**
   * Gets all the filter options from the backend for eg to get all unique
   * the website names, location etc.
   * @param attrName: takes the filter attribute
   */
  getAttributes(attrName: string): Observable<string[]> {
    if (attrName === null) {
      console.error('queryString is required parameter');
    }
    return this.http
      .get<string[]>(`${(EVENTS_ATTRIBUTES_API_ROUTE)}?attr=${attrName}`)
      .pipe(
        retry(2), // retry a failed request up to 2 times
        catchError(this.errorHandler) // then handle the error
      );
  }

}
