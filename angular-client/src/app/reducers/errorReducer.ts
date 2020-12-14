import {SET_ERROR_INFO, RESET_ERROR_INFO} from '../actions/types';

export interface IErrorAppState {
  error: string;
}

const INITIAL_ERROR_STATE: IErrorAppState = {
  error: null,
};

/**
 * Redux store to maintain state of the errors so that we can customize.
 * the error msg styles and templates.
 * @param state: contains error which takes error msg
 * @param type: action type for which operation to perform for manipulating the state
 * @param payload: error msg to display
 */
export function errorReducer(state: IErrorAppState = INITIAL_ERROR_STATE, {type, payload}): IErrorAppState {
  switch (type) {
    case SET_ERROR_INFO:
      return {...state, error: payload.error};

    case RESET_ERROR_INFO:
      if (state.error !== null) {
        return INITIAL_ERROR_STATE;
      }
      return state;

    default:
      return state;
  }
}
