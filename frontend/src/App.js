import React,{memo} from "react";

import {Routes,Route} from "react-router-dom";
import GlobalStyles from "./components/GlobalStyles";

import Main from "./pages/Main";

const App=memo(() => {
return(
     <>
        <GlobalStyles />
        <h1> Manyo-dashboard</h1>
        <hr />

        <Routes>
            <Route  path="/"  element={<Main />} />
        </Routes>
     </>
)
});


export default App;
