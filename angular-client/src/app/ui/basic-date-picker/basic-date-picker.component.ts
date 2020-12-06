import {Component, Input, OnInit} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {DatePickerState} from '../../models/DatePickerState';
import {Store} from '@ngrx/store';
import {TableAppState} from '../../reducers/tableReducer';

@Component({
  selector: 'app-basic-date-picker',
  templateUrl: './basic-date-picker.component.html',
  styleUrls: ['./basic-date-picker.component.css']
})
export class BasicDatePickerComponent implements OnInit {
  @Input() datePickerState: DatePickerState;

  constructor(private store: Store<TableAppState>) {
  }

  ngOnInit(): void {
  }

  // this event is to capture dates and dispatch it to the redux based on
  // the action type.
  // tslint:disable-next-line:typedef
  onDateChange(type: string, event: MatDatepickerInputEvent<unknown>) {
    // I am pretty sure that I will not get the incorrect date
    // as user as to select the date from the form instead of typing it.
    // @ts-ignore
    const date = new Date(event.value);

    // reformat the date in mm/dd/yyyy format and dispatch
    this.store.dispatch({type: this.datePickerState.actionType,
      payload: `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}` });
  }

}
