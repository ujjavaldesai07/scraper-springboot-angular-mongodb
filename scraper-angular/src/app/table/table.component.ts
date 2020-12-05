import {Component, OnInit} from '@angular/core';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {EventsService} from '../services/events.service';
import {Store} from '@ngrx/store';
import {TableAppState} from '../reducers/tableReducer';
import {DEFAULT_COMPONENT_VALUE, EVENTS_COLLECTION_SCHEMA, TABLE_REDUCER} from '../constants/constants';
import {RESET_ERROR_INFO, SET_ERROR_INFO} from '../actions/types';
import {DropdownOption} from '../models/DropdownOption';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css'],
})
export class TableComponent implements OnInit {
  // ids is used to render the content of the mat-table
  displayedColumnIds: string[] = [];

  // this contains the display name of column and attribute name
  // to fetch the value of that attribute from the response data object.
  colsInfo = EVENTS_COLLECTION_SCHEMA;

  // response events data which will be given to mat-table
  dataSource: IEventsResponseData[];

  // inject redux store and event service
  constructor(private store: Store<TableAppState>, private eventsService: EventsService) {
    // set the column ids i.e. property of the object we will receive
    // from the backend server.
    EVENTS_COLLECTION_SCHEMA.forEach(
      item => this.displayedColumnIds.push(item.attributeName)
    );

    // select the state from the store and subscribe for the event
    // on table reducer changes, getEventsFromProperties will get fired.
    // @ts-ignore
    store.select(TABLE_REDUCER).subscribe((state: TableAppState) => {
      this.getEventsFromProperties(state);
    });
  }

  ngOnInit(): void {
    // by default we will get all the events if no query parameters are supplied.
    this.eventsService.getEvents().subscribe(data => {

        // set the response data
        this.dataSource = data;

        // if data is empty display 'data not found'.
        this.checkIfDataIsEmpty();
      },
      // upon error dispatch the error msg to errorReducer
      error => this.store.dispatch({type: SET_ERROR_INFO, payload: {error}}));
  }

  /**
   * This function is prepare the query string based on the
   * parameters set in the redux store.
   * @param state: takes the TableAppState from redux.
   */
  getQueryString(state: TableAppState): [string, boolean] {

    // check if the start date is before end data
    if (state.startDate !== null && state.endDate !== null) {
      const sDate: Date = new Date(state.startDate);
      const eDate: Date = new Date(state.endDate);

      if (sDate > eDate) {
        alert('Start Date must not be after End Date!');
        return [null, true];
      }
    } else if (state.startDate !== null || state.endDate !== null) {
      // if user has not input both startDate & endDate then dont query this.
      return [null, true];
    }

    // prepare query
    let queryString = '';
    for (const [key, value] of Object.entries(state)) {

      // include only those parameters which are not null and not set to DEFAULT_COMPONENT_VALUE
      // if parameter is set DEFAULT_COMPONENT_VALUE means user doesn't wants
      // to filter that parameter
      // eg query string => location=some location&website=name
      if (value !== null && value !== DEFAULT_COMPONENT_VALUE) {
        queryString += `${key}=${value}&`;
      }
    }

    // remove last & character
    if (queryString !== '') {
      queryString = queryString.substring(0, queryString.length - 1);
    }

    // return status
    return [queryString, false];
  }

  getEventsFromProperties(state): void {
    // I will get state as undefined during initial render
    // as the redux store is getting initializing
    if (state === undefined) {
      return;
    }

    // check if we the query is processed correctly
    const [queryString, isError] = this.getQueryString(state);

    // on error we dont want to make REST call.
    if (isError) {
      return;
    }

    // get events based on the query parameters
    this.eventsService.getEvents(queryString).subscribe(data => {
        this.dataSource = data;
        this.checkIfDataIsEmpty();
      },
      error => this.store.dispatch({type: SET_ERROR_INFO, payload: {error}}));

    // scroll to top for fresh data
    window.scrollTo(0, 0);
  }

  /**
   * Function to check whether we got any data or not.
   */
  checkIfDataIsEmpty(): void {
    if (this.dataSource !== null && this.dataSource !== undefined && this.dataSource.length === 0) {
      this.store.dispatch({type: SET_ERROR_INFO, payload: {error: 'Data Not Found'}});
    } else {
      this.store.dispatch({type: RESET_ERROR_INFO});
    }
  }

}
