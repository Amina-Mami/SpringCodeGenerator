import { Layout } from 'antd';
import React from 'react'

const { Header } = Layout;
function Navbar() {
    return (
        <Header
            style={{
                position: "sticky",
                top: 0,
                zIndex: 2,
                width: "100%",
                display: "flex",
                alignItems: "center",
                flexDirection: "row-reverse"
            }}
        ></Header>
    );
}

export default Navbar