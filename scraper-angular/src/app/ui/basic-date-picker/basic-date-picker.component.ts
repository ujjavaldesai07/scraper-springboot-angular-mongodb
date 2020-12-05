import {Component, Input, OnInit} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {DatePickerState} from '../../models/DatePickerState';
import {Store} from '@ngrx/store';
import {AppState} from '../../reducers/tableReducer';

@Component({
  selector: 'app-basic-date-picker',
  templateUrl: './basic-date-picker.component.html',
  styleUrls: ['./basic-date-picker.component.css']
})
export class BasicDatePickerComponent implements OnInit {
  @Input() datePickerState: DatePickerState;

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
  }

  // tslint:disable-next-line:typedef
  onDateChange(type: string, event: MatDatepickerInputEvent<unknown>) {
    // @ts-ignore
    const date = new Date(event.value);
    this.store.dispatch({type: this.datePickerState.actionType,
      payload: `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}` });
  }

}
