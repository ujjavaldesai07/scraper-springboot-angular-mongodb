import {SET_ERROR_INFO, RESET_ERROR_INFO} from '../actions/types';

export interface ErrorAppState {
  error: string;
}

const INITIAL_ERROR_STATE: ErrorAppState = {
  error: null,
};

export function errorReducer(state: ErrorAppState = INITIAL_ERROR_STATE, {type, payload}): ErrorAppState {
  switch (type) {
    case SET_ERROR_INFO:
      return {...state, error: payload.error};

    case RESET_ERROR_INFO:
      return {...state, error: null};
  }
}
