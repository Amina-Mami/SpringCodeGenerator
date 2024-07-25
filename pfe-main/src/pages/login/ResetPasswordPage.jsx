import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { Form, Input, Button, Typography, message } from "antd";
import backgroundImage from "../../images/background.jpg";
import "./Login.css";

const ResetPasswordPage = () => {
  const query = new URLSearchParams(useLocation().search);
  const token = query.get("token");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleResetPassword = async () => {
    if (password !== confirmPassword) {
      message.error("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post(
        "http://localhost:7070/user/reset-password",
        null,
        {
          params: {
            token,
            newPassword: password,
          },
        }
      );

      if (response.status === 200) {
        message.success("Password has been reset successfully.");
        setTimeout(() => {
          navigate("/login");
        }, 3000);
      } else {
        message.error("Failed to reset password. Please try again.");
      }
    } catch (error) {
      message.error("Failed to reset password. Please try again.");
      console.error("Error resetting password:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div
        className="loginbg"
        style={{ background: `url(${backgroundImage})` }}
      >
        <Form
          name="normal_login"
          className="login-form"
          initialValues={{ remember: true }}
        >
          <Typography.Title className="text-login">
            Reset Password
          </Typography.Title>
          <Form.Item
            name="password"
            rules={[
              { required: true, message: "Please input your new password!" },
            ]}
          >
            <Input
              type="password"
              placeholder="New Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </Form.Item>
          <Form.Item
            name="confirmPassword"
            rules={[
              { required: true, message: "Please confirm your new password!" },
            ]}
          >
            <Input
              type="password"
              placeholder="Confirm Password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              onClick={handleResetPassword}
              loading={loading}
              style={{ display: "block", margin: "0 auto" }}
            >
              Reset Password
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default ResetPasswordPage;
