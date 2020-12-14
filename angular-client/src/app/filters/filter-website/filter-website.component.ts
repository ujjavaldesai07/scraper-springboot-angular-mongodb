import {Component, OnInit} from '@angular/core';
import {DropdownOption} from '../../models/DropdownOption';
import {DropdownState} from '../../models/DropdownState';
import {SET_WEBSITE_FILTER} from '../../actions/types';
import {DEFAULT_COMPONENT_VALUE} from '../../constants/constants';

@Component({
  selector: 'app-filter-website',
  templateUrl: './filter-website.component.html',
  styleUrls: ['./filter-website.component.css']
})
export class FilterWebsiteComponent implements OnInit {
  dropdownState: DropdownState;

  constructor() {
    // init dropdownState
    this.dropdownState = new DropdownState(
      [new DropdownOption('All Websites', DEFAULT_COMPONENT_VALUE)],
      'Filter By Website', SET_WEBSITE_FILTER, 'website');
  }

  ngOnInit(): void {
  }
}
