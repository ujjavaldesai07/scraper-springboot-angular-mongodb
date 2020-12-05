import {SET_END_DATE, SET_LOCATION_FILTER, SET_SORT_TYPE, SET_START_DATE, SET_WEBSITE_FILTER} from '../actions/types';

export interface AppState {
  website: string;
  location: string;
  startDate: string;
  endDate: string;
  sort: string;
}

const INITIAL_TABLE_STATE: AppState = {
  website: null,
  location: null,
  startDate: null,
  endDate: null,
  sort: null
};

export function tableReducer(state: AppState = INITIAL_TABLE_STATE, {type, payload}): AppState {
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
  }
}
