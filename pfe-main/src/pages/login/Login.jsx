import { useState } from "react";
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Checkbox, Form, Input, Typography, message } from "antd";
import "./Login.css";
import backgroundImage from "../../images/background.jpg";
import { Link } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../context/AuthContext"; 

const Login = () => {
  const { login } = useAuth(); 
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState(false);

 
  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await axios.post(
        "http://localhost:7070/user/login",
        values
      );
      if (response.status === 200) {
        const userData = response.data; 
        console.log("User data from login:", userData);
        if (userData.id) {
          const role = userData.role;
          login(userData, role); 
          message.success("Login successful!");
          if (role === "admin") {
            window.location.href = "/admin"; 
          } else {
            window.location.href = "/dashboard"; 
          }
        } else {
          message.error("Invalid user data received. Please try again.");
        }
      } else {
        message.error("Invalid username or password. Please try again.");
        setLoginError(true);
      }
    } catch (error) {
      console.error("Error during login:", error);
      message.error("An error occurred. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="loginbg" style={{ background: `url(${backgroundImage})` }}>
      <Form
        name="normal_login"
        className="login-form"
        initialValues={{ remember: true }}
        onFinish={onFinish}
      >
        <Typography.Title className="text-login">Login</Typography.Title>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Please input your Username!" }]}
        >
          <Input
            prefix={<UserOutlined className="site-form-item-icon" />}
            placeholder="Username"
          />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[{ required: true, message: "Please input your Password!" }]}
        >
          <Input
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Password"
          />
        </Form.Item>
        {loginError && (
          <Typography.Text type="danger">
            Wrong username or password. Please try again.
          </Typography.Text>
        )}
        <Form.Item>
          <Form.Item name="remember" valuePropName="checked" noStyle>
            <Checkbox>Remember me</Checkbox>
          </Form.Item>
          <Link className="login-form-forgot" to="/forgot-password">
            Forgot password
          </Link>
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="login-form-button"
            loading={loading}
          >
            Log in
          </Button>
          Or <Link to="/register">register now!</Link>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;
