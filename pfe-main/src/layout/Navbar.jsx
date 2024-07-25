import { Button } from "antd";
import logo from "../images/logo_fininfo.png";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const { logout, isAuthenticated } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = "/login";
    localStorage.removeItem("entities");
    localStorage.removeItem("enums");
  };

  return (
    <div
      style={{
        display: "flex",
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        background: "#f0f1f2",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 24px",
        height: "70px",
        zIndex: 1000,
      }}
    >
      <img src={logo} alt="Logo" style={{ height: 30 }} />
      {isAuthenticated() && (
        <div>
          <Button
            type="primary"
            style={{ backgroundColor: "#336699", borderColor: "#336699" }}
            onClick={handleLogout}
          >
            Sign Out
          </Button>
        </div>
      )}
    </div>
  );
};

export default Navbar;
