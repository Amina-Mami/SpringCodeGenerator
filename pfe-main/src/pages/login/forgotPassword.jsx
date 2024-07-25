
import React, { useState } from "react";
import { Form, Input, Button, Typography, message } from "antd";
import axios from "axios";
import "./ForgotPassword.css"; 
import backgroundImage from "../../images/background.jpg";

const ForgotPassword = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSendResetLink = async (values) => {
    setLoading(true);
    setError(""); 
    try {
      const response = await axios.post(
        "http://localhost:7070/user/forgot-password",
        { email: values.email } 
      );
      if (response.status === 200) {
        message.success("Reset link sent to your email!");
      } else {
        setError("Failed to send reset link. Please try again.");
      }
    } catch (error) {
      console.error("Error sending reset link:", error);
      if (error.response && error.response.data) {
        setError(
          error.response.data || "An error occurred. Please try again later."
        );
      } else {
        setError("An error occurred. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="loginbg" style={{ background: `url(${backgroundImage})` }}>
      <div className="forgot-password-container">
        <Typography.Title className="text-login">
          Forgot Password
        </Typography.Title>
        <Form
          name="forgot_password"
          className="forgot-password-form"
          onFinish={handleSendResetLink}
        >
          <Form.Item
            name="email"
            rules={[{ required: true, message: "Please input your email!" }]}
          >
            <Input placeholder="Email" />
          </Form.Item>

          {error && (
            <Typography.Text type="danger" style={{ textAlign: "center" }}>
              {error}
            </Typography.Text>
          )}

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              style={{ display: "block", margin: "0 auto" }}
            >
              Reset Link
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default ForgotPassword;
