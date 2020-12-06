import {
  SET_END_DATE, SET_LOCATION_FILTER,
  SET_SORT_TYPE, SET_START_DATE, SET_WEBSITE_FILTER
} from '../actions/types';

export interface TableAppState {
  website: string;
  location: string;
  startDate: string;
  endDate: string;
  sort: string;
}

const INITIAL_TABLE_STATE: TableAppState = {
  website: null,
  location: null,
  startDate: null,
  endDate: null,
  sort: null
};

/**
 * Redux store to maintain state of the table by setting filter and sort options.
 * Data will be displayed based on this options.
 *
 * @param state: contains all the supported filter and sort options
 * @param type: action type of filter/sort invoked
 * @param payload: data of that attribute to set the state.
 */

export function tableReducer(state: TableAppState = INITIAL_TABLE_STATE, {type, payload}): TableAppState {
  switch (type) {
    case SET_LOCATION_FILTER:
      return {...state, location: payload};

    case SET_WEBSITE_FILTER:
      return {...state, website: payload};

    case SET_START_DATE:
      return {...state, startDate: payload};

    case SET_END_DATE:
      return {...state, endDate: payload};

    case SET_SORT_TYPE:
      return {...state, sort: payload};

    default:
      return state;
  }
}
