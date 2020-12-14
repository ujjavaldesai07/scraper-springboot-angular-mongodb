import {Component, OnInit} from '@angular/core';
import {DropdownOption} from '../../models/DropdownOption';
import {DropdownState} from '../../models/DropdownState';
import {SET_LOCATION_FILTER} from '../../actions/types';
import {DEFAULT_COMPONENT_VALUE} from '../../constants/constants';

@Component({
  selector: 'app-filter-location',
  templateUrl: './filter-location.component.html',
  styleUrls: ['./filter-location.component.css']
})
export class FilterLocationComponent implements OnInit {
  dropdownState: DropdownState;
  errorMsg: string;

  constructor() {
    // init dropdownState
    this.dropdownState = new DropdownState(
      [new DropdownOption('All Locations', DEFAULT_COMPONENT_VALUE)],
      'Filter By Location', SET_LOCATION_FILTER, 'location');
  }

  ngOnInit(): void {
  }

}
