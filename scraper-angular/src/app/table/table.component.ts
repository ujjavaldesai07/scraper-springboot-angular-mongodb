import {Component, OnInit} from '@angular/core';
import {IEventsResponseData} from '../models/IEventsResponseData';
import {EventsService} from '../services/events.service';
import {Store} from '@ngrx/store';
import {AppState} from '../reducers/tableReducer';
import {DEFAULT_DROPDOWN_VALUE, EVENTS_COLLECTION_SCHEMA} from '../constants/constants';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css'],
})
export class TableComponent implements OnInit {
  displayedColumns: string[] = ['title', 'location', 'website', 'startDate', 'endDate'];
  errorMsg: string;
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
      error => this.errorMsg = error);
  }

  getQueryString(state: AppState): string {
    let queryString = '';
    for (const [key, value] of Object.entries(state)) {
      if (value !== null && value !== DEFAULT_DROPDOWN_VALUE) {
        queryString += `${key}=${value}&`;
      }
    }
    return queryString;
  }

  getEventsFromProperties(state): void {
    console.log('state ==> ' + JSON.stringify(state));
    if (state === undefined) {
      return;
    }

    const queryString: string = this.getQueryString(state);

    this.eventsService.getEvents(queryString).subscribe(data => this.dataSource = data,
      error => this.errorMsg = error);
    window.scrollTo(0, 0);
  }

}
