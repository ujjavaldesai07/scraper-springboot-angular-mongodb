import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {BACKEND_SERVER_URL, EVENTS_API_ROUTE, EVENTS_ATTRIBUTES_API_ROUTE} from '../constants/routes';
import {Observable, throwError} from 'rxjs';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {catchError, retry} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  constructor(private http: HttpClient) {
  }

  errorHandler(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred during processing the request.:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${JSON.stringify(error.error)}`);
    }
    // Return an observable with a user-facing error message.
    return throwError(error.message ||
      'Something bad happened; please try again later.');
  }

  getEvents(queryString = null): Observable<IEventsResponseData[]> {
    // @ts-ignore
    return this.http
      .get<IEventsResponseData[]>(`${EVENTS_API_ROUTE}${queryString !== null ? `?${queryString}` : ''}`)
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.errorHandler) // then handle the error
      );
  }

  getAttributes(queryString = null): Observable<string[]> {
    return this.http
      .get<string[]>(`${(EVENTS_ATTRIBUTES_API_ROUTE)}${queryString !== null ? `?attr=${queryString}` : ''}`)
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.errorHandler) // then handle the error
      );
  }

}
