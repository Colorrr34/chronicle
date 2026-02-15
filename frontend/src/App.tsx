import "./App.css";
import { Routes, Route } from "react-router";
import ApiDocumentation from "./Pages/ApiDocumentation";
import Header from "./Components/Header";
import MainPage from "./Pages/MainPage";
import Sidebar from "./Components/Sidebar";

function App() {
  return (
    <>
      <Header />
      <div className="body-container">
        <Routes>
          <Sidebar />
          <div className="content-body">
            <Route path="/" element={<MainPage />} />
            <Route path="/api" element={<ApiDocumentation />} />
          </div>
        </Routes>
      </div>
    </>
  );
}

export default App;
