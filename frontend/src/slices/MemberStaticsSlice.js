import reduxHelper from '../helpers/ReduxHelper';

const API_URL = "/api/statistics/members/daily";

export const getList = reduxHelper.get("MemberStatisticsSlice/getList", API_URL);

const MemberStatisticsSlice = reduxHelper.getDefaultSlice("MemberStatisticsSlice", [getList]);

export default MemberStatisticsSlice.reducer;
