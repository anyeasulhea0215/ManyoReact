import {configureStore } from '@reduxjs/toolkit';

import ManyoSlice from './slices/ManyoSlice';


const store=configureStore({
    reducer: {
        TitanicSlice
    }
});

export default store;