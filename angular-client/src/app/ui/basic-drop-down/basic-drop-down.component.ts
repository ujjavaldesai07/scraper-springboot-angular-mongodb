import {Component, Input, OnInit} from '@angular/core';
import {DropdownState} from '../../models/DropdownState';
import {MatSelectChange} from '@angular/material/select';
import {Store} from '@ngrx/store';
import {ITableAppState} from '../../reducers/tableReducer';
import {DropdownOption} from '../../models/DropdownOption';
import {EventsService} from '../../services/events.service';
import {SET_ERROR_INFO} from '../../actions/types';

@Component({
  selector: 'app-basic-drop-down',
  templateUrl: './basic-drop-down.component.html',
  styleUrls: ['./basic-drop-down.component.css']
})
export class BasicDropDownComponent implements OnInit {

  // state to use this dropdown
  // parent component has to pass this prop.
  @Input() dropdownState: DropdownState;

  // redux store
  constructor(private store: Store<ITableAppState>, private eventsService: EventsService) {
  }

  ngOnInit(): void {
    if (this.dropdownState.attrName !== null) {
      // get the option list from the backend.
      this.eventsService.getAttributes(this.dropdownState.attrName).subscribe(data => {
          data.forEach(item => this.dropdownState.options.push(new DropdownOption(item, item)));
        },
        error => {
          // if unable to fetch the data then set the empty DropdownOption
          // so that the loading progress is not displayed.
          this.dropdownState.options.push(new DropdownOption('', ''));
          this.dropdownState.errorMsg = error;
          this.store.dispatch({type: SET_ERROR_INFO, payload: {error}});
        });
    }
  }

  // dispatch dropdown value to redux store based on action type on select event.
  onSelectedValue($event: MatSelectChange): void {
    this.store.dispatch({type: this.dropdownState.actionType, payload: $event.value});
  }
}
