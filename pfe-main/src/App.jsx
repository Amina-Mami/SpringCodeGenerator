import "./App.css";
import AppLayout from "./layout/AppLayout";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Entities from "./pages/entities/Entities";
import General from "./pages/general/General";
import Login from "./pages/login/Login";
import Register from "./pages/login/Register";
import Dashboard from "./pages/dashboard/dashboard";
import { EntityProvider } from "./context/EntityContext";
import AddRelationship from "./pages/entities/AddRelationship";
function App() {
  return (
    <EntityProvider>
      <div className="app">
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/" element={<Navigate replace to="/login" />} />
            <Route path="/dashboard" element={<AppLayout />}>
              <Route path="" element={<Dashboard />} />
              <Route path="general" element={<General />} />
              <Route path="entities" element={<Entities />}>
                <Route path=":id/relationships" element={<AddRelationship />} />
              </Route>
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
    </EntityProvider>
  );
}

export default App;
