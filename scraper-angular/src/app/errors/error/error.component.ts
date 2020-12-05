import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {ErrorAppState} from '../../reducers/errorReducer';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  public errorMsg: string;

  constructor(private store: Store<ErrorAppState>) {
    // @ts-ignore
    store.select('errorReducer').subscribe((state: ErrorAppState) => {
      this.updateErrorMsg(state);
    });
  }

  ngOnInit(): void {
  }

  updateErrorMsg(state: ErrorAppState): void {
    if (state !== undefined) {
      this.errorMsg = state.error;
    }
  }
}
