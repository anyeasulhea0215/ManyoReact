import reduxHelper from "../helpers/ReduxHelper";

const API_URL = "/api/stat/popular-products";

export const getPopularProducts = reduxHelper.get("PopularProductSlice/getList", API_URL);

const PopularProductSlice = reduxHelper.getDefaultSlice("PopularProductSlice", [getPopularProducts]);

export default PopularProductSlice.reducer;
