import {Component, OnInit} from '@angular/core';
import {DropdownOption} from '../../models/DropdownOption';
import {DropdownState} from '../../models/DropdownState';
import {SET_SORT_TYPE} from '../../actions/types';
import {EVENTS_COLLECTION_SCHEMA} from '../../constants/constants';

@Component({
  selector: 'app-sort-events',
  templateUrl: './sort-events.component.html',
  styleUrls: ['./sort-events.component.css']
})
export class SortEventsComponent implements OnInit {
  dropdownState: DropdownState;

  constructor() {
    // push the dropdown options.
    this.dropdownState = new DropdownState([], 'Sort By', SET_SORT_TYPE);

    // set the text and value from the schema which will be found in the object,
    EVENTS_COLLECTION_SCHEMA.forEach(
      item => this.dropdownState.options.push(new DropdownOption(item.displayName, item.attributeName))
    );
  }

  ngOnInit(): void {

  }

}
