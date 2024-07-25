import { Breadcrumb, Layout, theme } from "antd";
import { Outlet, useLocation } from "react-router-dom";
import React from "react";
import { HomeOutlined } from "@ant-design/icons";
const { Content } = Layout;
function Container() {
  const location = useLocation();
  const { pathname } = location;
  const pathArray = pathname.split("/").filter(Boolean);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  return (
    <Layout
      style={{
        padding: "0 24px 24px",
      }}
    >
      <Breadcrumb
        style={{
          margin: "16px 0",
        }}
      >
        <Breadcrumb.Item>
          <HomeOutlined />
        </Breadcrumb.Item>
        {pathArray.map((item) => (
          <Breadcrumb.Item key={item}>{item}</Breadcrumb.Item>
        ))}
      </Breadcrumb>
      <Content
        style={{
          padding: 24,
          margin: 0,
          minHeight: 280,
          background: colorBgContainer,
          borderRadius: borderRadiusLG,
        }}
      >
        <Outlet />
      </Content>
    </Layout>
  );
}

export default Container;
