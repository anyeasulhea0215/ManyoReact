import reduxHelper from '../helpers/ReduxHelper';

const API_URL='/titanic';

export const getList=reduxHelper.get("TitanicSlice/getList", API_URL);

const TitanicSlice=reduxHelper.getDefaultSlice("TitanicSlice", [getList]);

export default TitanicSlice.reducer;