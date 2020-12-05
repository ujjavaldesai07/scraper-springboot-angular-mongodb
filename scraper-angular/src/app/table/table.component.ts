import {Component, OnInit} from '@angular/core';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {EventsService} from '../services/events.service';
import {Store} from '@ngrx/store';
import {AppState} from '../reducers/tableReducer';
import {DEFAULT_DROPDOWN_VALUE, EVENTS_COLLECTION_SCHEMA} from '../constants/constants';
import {SET_ERROR_INFO} from '../actions/types';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css'],
})
export class TableComponent implements OnInit {
  displayedColumns: string[] = ['title', 'location', 'website', 'startDate', 'endDate'];
  colsInfo = EVENTS_COLLECTION_SCHEMA;
  dataSource: IEventsResponseData[];

  constructor(private store: Store<AppState>, private eventsService: EventsService) {
    // @ts-ignore
    store.select('tableReducer').subscribe((state: AppState) => {
      this.getEventsFromProperties(state);
    });
  }

  ngOnInit(): void {
    this.eventsService.getEvents().subscribe(data => this.dataSource = data,
      error => this.store.dispatch({type: SET_ERROR_INFO, payload: {error}}));
  }

  getQueryString(state: AppState): string {

    if(state.startDate != null && state.endDate) {
      const sDate: Date = new Date(state.startDate);
      const eDate: Date = new Date(state.endDate);

      if (sDate > eDate) {
        alert('Start Date must not be after End Date!');
      }
    }

    let queryString = '';
    for (const [key, value] of Object.entries(state)) {
      if (value !== null && value !== DEFAULT_DROPDOWN_VALUE) {
        queryString += `${key}=${value}&`;
      }
    }
    return queryString;
  }

  getEventsFromProperties(state): void {
    if (state === undefined) {
      return;
    }

    const queryString: string = this.getQueryString(state);

    this.eventsService.getEvents(queryString).subscribe(data => this.dataSource = data,
      error => this.store.dispatch({type: SET_ERROR_INFO, payload: {error}}));
    window.scrollTo(0, 0);
  }

  checkIfDataIsEmpty(): void {
    if (this.dataSource !== null && this.dataSource !== undefined && this.dataSource.length === 0) {
      this.store.dispatch({type: SET_ERROR_INFO, payload: {error: 'Data Not Found'}});
    }
  }

}
