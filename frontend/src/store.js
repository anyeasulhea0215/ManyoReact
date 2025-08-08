import { configureStore } from '@reduxjs/toolkit';
import popularProductReducer from './slices/PopularProductSlice';


const store = configureStore({
  reducer: {
    popularProductReducer,

  },
});

export default store;
