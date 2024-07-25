import { Layout, Menu } from "antd";
import { NavLink } from "react-router-dom";
import { GoDatabase } from "react-icons/go";
import { TfiWorld } from "react-icons/tfi";
import { FolderOutlined } from "@ant-design/icons";
import { BarChartOutlined } from "@ant-design/icons";
import { HiOutlineUsers } from "react-icons/hi";

const { Sider } = Layout;

const adminNavItems = [
  {
    key: `AdminDashboard`,
    icon: <BarChartOutlined style={{ fontSize: 18 }} />,
    label: (
      <NavLink className={"activeLink"} to={`/admin`}>
        Dashboard
      </NavLink>
    ),
  },
  {
    key: `AdminUsers`,
    icon: <HiOutlineUsers size={18} />,
    label: (
      <NavLink className={"activeLink"} to={`/admin/users`}>
        Users
      </NavLink>
    ),
  },
  {
    key: `AdminProjects`,
    icon: <FolderOutlined style={{ fontSize: 18 }} />,
    label: (
      <NavLink className={"activeLink"} to={`/admin/projects`}>
        Projects
      </NavLink>
    ),
  },
];

const developerNavItems = [
  {
    key: `Entities`,
    icon: <GoDatabase size={18} />,
    label: (
      <NavLink className={"activeLink"} to={`/dashboard/entities`}>
        Entities
      </NavLink>
    ),
  },
  {
    key: `General`,
    icon: <TfiWorld size={18} />,
    label: (
      <NavLink className={"activeLink"} to={`/dashboard/general`}>
        Create Project
      </NavLink>
    ),
  },
  {
    key: `Dashboard`,
    icon: <FolderOutlined style={{ fontSize: 18 }} />,
    label: (
      <NavLink className={"activeLink"} to={`/dashboard`}>
        My Projects
      </NavLink>
    ),
  },
];

function SideBar({ role }) {
  const navItems = role === "admin" ? adminNavItems : developerNavItems;

  return (
    <Sider
      width={200}
      style={{
        position: "fixed",
        left: 0,
        height: "100vh",
        overflow: "auto",
        paddingTop: 164,

        background: "#f0f1f2",
      }}
    >
      <Menu
        mode="inline"
        defaultSelectedKeys={["1"]}
        defaultOpenKeys={["sub1"]}
        style={{
          height: "100%",
          borderRight: 0,
          background: "#f0f1f2",
          width: "100%",
        }}
      >
        {navItems.map((item) => (
          <Menu.Item
            key={item.key}
            icon={item.icon}
            style={{ textDecoration: "none" }}
          >
            {item.label}
          </Menu.Item>
        ))}
      </Menu>
    </Sider>
  );
}

export default SideBar;
