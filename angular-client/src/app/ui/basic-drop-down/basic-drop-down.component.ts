import {Component, Input, OnInit} from '@angular/core';
import {DropdownState} from '../../models/DropdownState';
import {MatSelectChange} from '@angular/material/select';
import {Store} from '@ngrx/store';
import {TableAppState} from '../../reducers/tableReducer';

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
  constructor(private store: Store<TableAppState>) {
  }

  ngOnInit(): void {
  }

  // dispatch dropdown value to redux store based on action type on select event.
  onSelectedValue($event: MatSelectChange): void {
    this.store.dispatch({type: this.dropdownState.actionType, payload: $event.value });
  }
}
