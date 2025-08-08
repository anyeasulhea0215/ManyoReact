import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const fetchPopularProducts = createAsyncThunk(
  "popularProduct/fetch",
  async () => {
    const response = await axios.get("/api/stat/popular-products");
    return response.data;
  }
);

const popularProductSlice = createSlice({
  name: "popularProduct",
  initialState: { items: [], loading: false, error: null },
  extraReducers: (builder) => {
    builder
      .addCase(fetchPopularProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPopularProducts.fulfilled, (state, action) => {
        state.items = action.payload;
        state.loading = false;
      })
      .addCase(fetchPopularProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export default popularProductSlice.reducer;
