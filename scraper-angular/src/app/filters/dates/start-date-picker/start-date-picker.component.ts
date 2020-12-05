import {Component, OnInit} from '@angular/core';
import {SET_START_DATE} from '../../../actions/types';
import {DatePickerState} from '../../../models/DatePickerState';

@Component({
  selector: 'app-start-date-picker',
  templateUrl: './start-date-picker.component.html',
  styleUrls: ['./start-date-picker.component.css']
})
export class StartDatePickerComponent implements OnInit {
  datePickerState: DatePickerState;

  constructor() {
    // init datePickerState
    this.datePickerState = new DatePickerState('Start Date', SET_START_DATE);
  }

  ngOnInit(): void {
  }

}
