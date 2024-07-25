
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import DashboardAdmin from "./pages/Admin/DashboardAdmin";
import UserManagement from "./pages/Admin/userManagement";
import ProjectManagement from "./pages/Admin/ProjectManagement";
import Login from "./pages/login/Login";
import Register from "./pages/login/Register";
import AppLayout from "./layout/AppLayout";
import ProjectTable from "./pages/dashboard/dashboard";
import General from "./pages/general/General";
import Entities from "./pages/entities/Entities";
import AddRelationship from "./pages/entities/AddRelationship";
import { EntityProvider } from "./context/EntityContext";
import { AuthProvider } from "./context/AuthContext";
import ForgotPassword from "./pages/login/forgotPassword";
import ResetPasswordPage from "./pages/login/ResetPasswordPage";
import UpdateProject from "./pages/update/updateProject"; 

function App() {
  return (
    <EntityProvider>
      <AuthProvider>
        <div className="app">
          <BrowserRouter>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/reset-password" element={<ResetPasswordPage />} />
              <Route path="/register" element={<Register />} />
              <Route path="/" element={<Navigate replace to="/login" />} />
              <Route path="/admin" element={<AppLayout />}>
                <Route index element={<DashboardAdmin />} />
                <Route path="users" element={<UserManagement />} />
                <Route path="projects" element={<ProjectManagement />} />
              </Route>
              <Route path="/dashboard" element={<AppLayout />}>
                <Route index element={<ProjectTable />} />
                <Route path="general" element={<General />} />
                <Route path="entities" element={<Entities />}>
                  <Route
                    path=":id/relationships"
                    element={<AddRelationship />}
                  />
                </Route>
                <Route
                  path="update-project/:projectId"
                  element={<UpdateProject />}
                />{" "}
                <Route path="project/:projectId" element={<Entities />} />
              </Route>
            </Routes>
          </BrowserRouter>
        </div>
      </AuthProvider>
    </EntityProvider>
  );
}

export default App;
