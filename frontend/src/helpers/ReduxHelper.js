
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import fetchHelper from "./FetchHelper";

// Redux가 로딩 상태를 관리하는 상태값을 생성하는 함수
const pending = (state, { meta, payload }) => {
  return { ...state, loading: true };
};

// Redux가 성공 상태를 관리하는 상태값을 생성하는 함수
const fulfilled = (state, { meta, payload }) => {
  return { ...payload, loading: false };
};

// Redux가 실패 상태를 관리하는 상태값을 생성하는 함수
const rejected = (state, { meta, payload }) => {
  return {
    ...state,
    loading: false,
    status: payload?.status || 0,
    message: payload?.message || "Unknown Error",
  };
};

// reduxHelper 모듈 생성
const reduxHelper = {
  // 1) createSlice를 생성하는 함수
  // slideName: slice 이름
  // extraReducers: 비동기 액션함수 목록
  // callback: fulfilled 성공 시 호출할 콜백함수. 없으면 기본 fulfilled 호출
  getDefaultSlice: (slideName, extraReducers = [], callback = {}, reducers = {}) => {
    return createSlice({
      name: slideName,
      initialState: {
        // 백엔드에서 내려주는 데이터 구조와 동일하게 정의
        status: 200,
        message: "OK",
        item: null,
        timestamp: null,
        // 로딩 상태를 감지하기 위한 변수
        loading: false,
      },
      reducers: reducers,
      extraReducers: (builder) => {
        extraReducers.forEach((v) => {
          if (!v || !v.pending || !v.fulfilled || !v.rejected) {
            console.warn(`[ReduxHelper] Invalid thunk detected in extraReducers:`, v);
            return;
          }

          builder.addCase(v.pending, pending);
          builder.addCase(v.fulfilled, callback[v.fulfilled] || fulfilled);
          builder.addCase(v.rejected, rejected);
        });
      },
    });
  },

  // 2) createAsyncThunk + get 요청 처리
  get: (alias, url, callback = (payload) => ({ url: url, params: payload })) => {
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

  // 3) post 요청 처리
  post: (alias, url, callback = (payload) => ({ url: url, params: payload })) => {
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

  // 4) put 요청 처리
  put: (alias, url, callback = (payload) => ({ url: url, params: payload })) => {
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

  // 5) delete 요청 처리
  delete: (alias, url, callback = (payload) => ({ url: url, params: payload })) => {
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
