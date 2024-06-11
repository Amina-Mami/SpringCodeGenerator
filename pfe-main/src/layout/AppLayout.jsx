import React from "react";
import { Layout } from "antd";
import SideBar from "./Sidebar";
import Container from "./Container";
import Navbar from "./Navbar";

const AppLayout = () => {
    return (
        <Layout>
            <SideBar />
            <Container />
        </Layout>
    );
};
export default AppLayout;