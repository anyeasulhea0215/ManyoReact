/**
 * /src/helpers/ReduxHelper.js
 *
 * ReduxSlice를 작업하면서 반복되는 중복코드의 모듈화
 */
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import fetchHelper from './FetchHelper';

// 리덕스가 로딩 상태를 관리하는 상태값을 생성하는 함수
const pending = (state, { meta, payload }) => {
    return { ...state, loading: true };
};

// 리덕스가 성공 상태를 관리하는 상태값을 생성하는 함수
const fulfilled = (state, { meta, payload }) => {
    return { ...payload, loading: false };
};

// 리덕스가 실패 상태를 관리하는 상태값을 생성하는 함수
const rejected = (state, { meta, payload }) => {
    return {
        ...state,
        loading: false,
        status: payload?.status || 0,
        message: payload?.message || "Unknown Error",
    };
};

const reduxHelper = {

    /**
     * Redux Slice 객체를 생성하는 함수
     * 1) sliceName: Slice 객체 이름
     * 2) : 비동기 액션함수 배열
     * 3) callback: 비동기 처리 성공 시 호출할 콜백함수. 없으면 기본 fulfilled 함수 호출
     */
    getDefaultSlice: (sliceName, extraReducers = [], callback = {}, reducers = {}) => {
        console.log(extraReducers);
        return createSlice({
            name: sliceName,
            initialState: {
                // 백엔드에서 내려주는 데이터 구조와 동일하게 정의
                status: 200,
                message: "OK",
                item: [],
                timestamp: null,
                // 로딩 상태를 감지하기 위한 변수
                loading: false,
            },
            reducers: reducers,
            extraReducers: (builder) => {
                extraReducers.forEach((v, i) => {
                    builder.addCase(v.pending, pending);
                    builder.addCase(v.fulfilled, callback[v.fulfilled] || fulfilled);
                    builder.addCase(v.rejected, rejected);
                });
            },
        });
    },

    get: (alias, url, callback = (payload) => { return { url: url, params: payload }; }) => {
        return createAsyncThunk(alias, async (payload, { rejectWithValue }) => {
            let result = null;
            const { url, params } = callback(payload);

            try {
                result = await fetchHelper.get(url, params);
            } catch (err) {
                console.group("[ReduxHelper.get] Redux Action Error");
                console.error(err);
                console.groupEnd();
                result = rejectWithValue(err);
            }

            return result;
        });
    },

    post: (alias, url, callback = (payload) => { return { url: url, params: payload }; }) => {
        return createAsyncThunk(alias, async (payload, { rejectWithValue }) => {
            let result = null;
            const { url, params } = callback(payload);

            try {
                result = await fetchHelper.post(url, params);
            } catch (err) {
                console.group("[ReduxHelper.post] Redux Action Error");
                console.error(err);
                console.groupEnd();
                result = rejectWithValue(err);
            }

            return result;
        });
    },

    put: (alias, url, callback = (payload) => { return { url: url, params: payload }; }) => {
        return createAsyncThunk(alias, async (payload, { rejectWithValue }) => {
            let result = null;
            const { url, params } = callback(payload);

            try {
                result = await fetchHelper.put(url, params);
            } catch (err) {
                console.group("[ReduxHelper.put] Redux Action Error");
                console.error(err);
                console.groupEnd();
                result = rejectWithValue(err);
            }

            return result;
        });
    },

    delete:  (alias, url, callback = (payload) => { return { url: url, params: payload }; }) => {
        return createAsyncThunk(alias, async (payload, { rejectWithValue }) => {
            let result = null;
            const { url, params } = callback(payload);

            try {
                result = await fetchHelper.delete(url, params);
            } catch (err) {
                console.group("[ReduxHelper.delete] Redux Action Error");
                console.error(err);
                console.groupEnd();
                result = rejectWithValue(err);
            }

            return result;
        });
    },
};

export default reduxHelper;
