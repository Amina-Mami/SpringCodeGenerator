import React from "react";
// import { NotificationOutlined } from "@ant-design/icons";
import { HiOutlineUsers } from "react-icons/hi";
import { Layout, Menu, theme } from "antd";
import { NavLink } from "react-router-dom";
import { GoDatabase } from "react-icons/go";
import { TfiWorld } from "react-icons/tfi";
import logo from '../images/logo_fininfo.png';
import { FolderOutlined } from '@ant-design/icons';

const { Sider } = Layout;

const navItems = [
    {
        key: `General`,
        icon: <TfiWorld size={18} />,
        label: (
            <NavLink className={"activeLink"} to={`/dashboard/general`}>
                General
            </NavLink>
        ),
    },
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
        key: `Dashboard`,
        icon: <FolderOutlined style={{ fontSize: 18 }} />,
        label: (
            <NavLink className={"activeLink"} to={`/dashboard`}>
                All Projects
            </NavLink>
        ),
    },
];

function SideBar() {
    return (
        <Sider
            width={"250px"}
            style={{
                minHeight: "100vh",
            }}
        >
            <div style={{backgroundColor:"white"}}><img src={logo} width={200}/></div>
            <Menu
                mode="inline"
                defaultSelectedKeys={["1"]}
                defaultOpenKeys={["sub1"]}
                style={{
                    height: "100%",
                    borderRight: 0,
                    // backgroundColor: "#316999"
                }}
                items={navItems}
            />
        </Sider>
    );
}

export default SideBar;
