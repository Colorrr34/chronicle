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
        <Sidebar />
        <div className="content-body">
          <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/api" element={<ApiDocumentation />} />
          </Routes>
        </div>
      </div>
    </>
  );
}

export default App;
