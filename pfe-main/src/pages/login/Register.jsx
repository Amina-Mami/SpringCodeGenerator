import React from "react";
import { LockOutlined, UserOutlined, MailOutlined } from "@ant-design/icons";
import { Button, Form, Input, Typography, message } from "antd";
import "./Login.css";
import backgroundImage from "../../images/background.jpg";
import axios from "axios";

const Register = () => {
  const onFinish = async (values) => {
    try {
      const response = await axios.post(
        "http://localhost:7070/project/user",
        values
      );
      if (response.status === 201) {
        message.success(
          "Registration successful! Redirecting to login page..."
        );

        setTimeout(() => {
          window.location.href = "/login";
        }, 3000);
      } else {
        message.error(
          "An error occurred during registration. Please try again."
        );
      }
    } catch (error) {
      console.error("Error during registration:", error);
      message.error("An error occurred during registration. Please try again.");
    }
  };

  return (
    <div className="loginbg" style={{ background: `url(${backgroundImage})` }}>
      <Form
        name="register"
        className="login-form"
        initialValues={{ remember: true }}
        onFinish={onFinish}
      >
        <Typography.Title className="text-login">Register</Typography.Title>
        <Form.Item
          name="email"
          rules={[{ required: true, message: "Please input your email!" }]}
        >
          <Input
            prefix={<MailOutlined className="site-form-item-icon" />}
            type="email"
            placeholder="Email"
          />
        </Form.Item>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Please input your username!" }]}
        >
          <Input
            prefix={<UserOutlined className="site-form-item-icon" />}
            placeholder="Username"
          />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[{ required: true, message: "Please input your password!" }]}
        >
          <Input
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Password"
          />
        </Form.Item>
        <Form.Item
          name="confirm"
          dependencies={["password"]}
          hasFeedback
          rules={[
            { required: true, message: "Please confirm your password!" },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error("The two passwords that you entered do not match!")
                );
              },
            }),
          ]}
        >
          <Input
            prefix={<LockOutlined className="site-form-item-icon" />}
            type="password"
            placeholder="Confirm Password"
          />
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            className="login-form-button"
          >
            Register
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Register;
