import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {IErrorAppState} from '../../reducers/errorReducer';
import {ERROR_REDUCER} from '../../constants/constants';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  public errorMsg: string;

  // get redux store
  constructor(private store: Store<IErrorAppState>) {
    // subscribe for the state. Upon change updateErrorMsg will get fired.
    // @ts-ignore
    store.select(ERROR_REDUCER).subscribe((state: IErrorAppState) => {
      this.updateErrorMsg(state);
    });
  }

  ngOnInit(): void {
  }

  updateErrorMsg(state: IErrorAppState): void {
    // check whether redux is initializing state.
    if (state !== undefined) {
      this.errorMsg = state.error;
    }
  }
}
