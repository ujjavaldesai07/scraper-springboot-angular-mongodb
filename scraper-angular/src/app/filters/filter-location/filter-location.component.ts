import {Component, OnInit} from '@angular/core';
import {DropdownOption} from '../../models/DropdownOption';
import {DropdownState} from '../../models/DropdownState';
import {SET_LOCATION_FILTER, SET_WEBSITE_FILTER} from '../../actions/types';
import {DEFAULT_DROPDOWN_VALUE} from '../../constants/constants';
import {EventsService} from '../../services/events.service';

@Component({
  selector: 'app-filter-location',
  templateUrl: './filter-location.component.html',
  styleUrls: ['./filter-location.component.css']
})
export class FilterLocationComponent implements OnInit {
  dropdownState: DropdownState;
  errorMsg: string;

  constructor(private eventsService: EventsService) {
    this.dropdownState = new DropdownState(
      [new DropdownOption('All Locations', DEFAULT_DROPDOWN_VALUE)],
      'Filter By Location', SET_LOCATION_FILTER);
  }

  ngOnInit(): void {
    this.eventsService.getAttributes('location').subscribe(data => {
        data.forEach(item => this.dropdownState.options.push({text: item, value: item}));
      },
      error => this.errorMsg = error);
  }

}
