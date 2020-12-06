import {Component, OnInit} from '@angular/core';
import {DropdownOption} from '../../models/DropdownOption';
import {DropdownState} from '../../models/DropdownState';
import {SET_WEBSITE_FILTER} from '../../actions/types';
import {DEFAULT_COMPONENT_VALUE} from '../../constants/constants';
import {EventsService} from '../../services/events.service';

@Component({
  selector: 'app-filter-website',
  templateUrl: './filter-website.component.html',
  styleUrls: ['./filter-website.component.css']
})
export class FilterWebsiteComponent implements OnInit {
  dropdownState: DropdownState;
  errorMsg: string;

  constructor(private eventsService: EventsService) {
    // init dropdownState
    this.dropdownState = new DropdownState(
      [new DropdownOption('All Websites', DEFAULT_COMPONENT_VALUE)],
      'Filter By Website', SET_WEBSITE_FILTER);
  }

  ngOnInit(): void {
    // get the option list from the backend.
    this.eventsService.getAttributes('website').subscribe(data => {
        data.forEach(item => this.dropdownState.options.push(new DropdownOption(item, item)));
      },
      error => this.errorMsg = error);
  }
}
