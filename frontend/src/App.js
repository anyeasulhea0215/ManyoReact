import React from "react";
import { Routes, Route } from "react-router-dom";
import Dashboard from "./pages/Dashboard";

const App = () => {
  return (
    <div className="App">
      <Routes>
        {/* 대시보드 메인 페이지 */}
        <Route path="/" element={<Dashboard />} />
      </Routes>
    </div>
  );
};

export default App;
