import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {EVENTS_API_ROUTE, EVENTS_ATTRIBUTES_API_ROUTE} from '../constants/routes';
import {Observable, throwError} from 'rxjs';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {catchError, retry} from 'rxjs/operators';
import {Store} from '@ngrx/store';
import {ErrorAppState} from '../reducers/errorReducer';

@Injectable({
  providedIn: 'root'
})
export class EventsService {
  errorMsg: string;

  constructor(private store: Store<ErrorAppState>, private http: HttpClient) {
  }

  errorHandler(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this.errorMsg = '503: Server Unreachable';
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      if (error.status === 0) {
        this.errorMsg = '503: Server Unreachable';
      } else {
        this.errorMsg = `${error.status}: ${error.statusText}`;
      }
    }
    // Return an observable with a user-facing error message.
    return throwError(this.errorMsg ||
      'Something bad happened; please try again later.');
  }

  getEvents(queryString = null): Observable<IEventsResponseData[]> {
    // @ts-ignore
    return this.http
      .get<IEventsResponseData[]>(`${EVENTS_API_ROUTE}${queryString !== null ? `?${queryString}` : ''}`)
      .pipe(
        retry(2), // retry a failed request up to 2 times
        catchError(this.errorHandler) // then handle the error
      );
  }

  getAttributes(queryString = null): Observable<string[]> {
    return this.http
      .get<string[]>(`${(EVENTS_ATTRIBUTES_API_ROUTE)}${queryString !== null ? `?attr=${queryString}` : ''}`)
      .pipe(
        retry(2), // retry a failed request up to 2 times
        catchError(this.errorHandler) // then handle the error
      );
  }

}
