import {configureStore } from '@reduxjs/toolkit';

import MemberStaticsSlice from './slices/MemberStaticsSlice';
import BestProductSlice from './slices/BestProductSlice';


const store=configureStore({
    reducer: {
        MemberStaticsSlice,
        BestProductSlice,
    }
});

export default store;