
import {configureStore } from '@reduxjs/toolkit';

import MemberStaticsSlice from './slices/MemberStaticsSlice';
import PopularProductSlice from "./slices/PopularProductSlice";


const store=configureStore({
    reducer: {
        MemberStaticsSlice,
        PopularProductSlice
    }

});

export default store;
