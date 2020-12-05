import { Component, OnInit } from '@angular/core';
import {DatePickerState} from '../../../models/DatePickerState';
import {SET_END_DATE, SET_START_DATE} from '../../../actions/types';

@Component({
  selector: 'app-end-date-picker',
  templateUrl: './end-date-picker.component.html',
  styleUrls: ['./end-date-picker.component.css']
})
export class EndDatePickerComponent implements OnInit {
  datePickerState: DatePickerState;

  constructor() {
    // init datePickerState
    this.datePickerState = new DatePickerState('End Date', SET_END_DATE);
  }

  ngOnInit(): void {
  }
}
