import {Component, Input, OnInit} from '@angular/core';
import {DropdownState} from '../../models/DropdownState';
import {MatSelectChange} from '@angular/material/select';
import {Store} from '@ngrx/store';
import {AppState} from '../../reducers/tableReducer';

@Component({
  selector: 'app-basic-drop-down',
  templateUrl: './basic-drop-down.component.html',
  styleUrls: ['./basic-drop-down.component.css']
})
export class BasicDropDownComponent implements OnInit {
  @Input() dropdownState: DropdownState;

  constructor(private store: Store<AppState>) {
  }

  ngOnInit(): void {
  }

  onSelectedValue($event: MatSelectChange): void {
    this.store.dispatch({type: this.dropdownState.actionType, payload: $event.value });
  }
}
