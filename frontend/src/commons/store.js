import { createStore } from 'redux';

const initialState = {
  alertMessages: [],
};

const rootReducer = (state = initialState, action) => {
  switch (action.type) {
    case 'ADD_ALERT_MESSAGE':
      return {
        ...state,
        alertMessages: [...state.alertMessages, action.payload],
      };
    case 'REMOVE_ALERT_MESSAGE':
      return {
        ...state,
        alertMessages: state.alertMessages.filter((_, index) => index !== action.payload),
      };
    default:
      return state;
  }
};

const store = createStore(rootReducer);

export default store;