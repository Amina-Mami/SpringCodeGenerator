import { Layout } from "antd";
import SideBar from "./Sidebar";
import Container from "./Container";
import { useAuth } from "../context/AuthContext";
import NavBar from "./Navbar";
const { Header, Content } = Layout;

const AppLayout = () => {
  const { logout, isAuthenticated, user } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = "/login";
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <NavBar style={{ marginTop: "0px" }} />
      <Layout
        style={{ marginLeft: isAuthenticated() ? 200 : 0, marginTop: 50 }}
      >
        {isAuthenticated() && <SideBar role={user.role} />}
        <Layout style={{ padding: "24px", backgroundColor: "#f0f2f5" }}>
          <Content
            style={{ padding: "24px", minHeight: 280, background: "#fff" }}
          >
            <Container />
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};

export default AppLayout;
