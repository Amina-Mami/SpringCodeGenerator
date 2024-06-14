import React, { useState, useContext } from "react";
import {
  Button,
  Card,
  Checkbox,
  Form,
  Input,
  Radio,
  Typography,
  message,
} from "antd";
import axios from "axios";
import { EntityContext } from "../../context/EntityContext";

const { Title } = Typography;

function General() {
  const [form] = Form.useForm();
  const [database, setDatabase] = useState(false);
  const [swagger, setSwagger] = useState(false);
  const { entities } = useContext(EntityContext);

  const changeDatabaseState = () => setDatabase(!database);
  const changeSwaggerState = () => setSwagger(!swagger);

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        const requestData = {
          ...values,
          entities: entities,
        };
        console.log(requestData);

        axios
          .post("http://localhost:7070/project/create/1", requestData)
          .then((response) => {
            console.log("Response:", response.data);

            message.success("Project created successfully");
            form.resetFields();
            setTimeout(() => {
              window.location.href = "/dashboard";
            }, 2000);
          })
          .catch((error) => {
            console.error("Error:", error);
          });
      })
      .catch((errorInfo) => {
        console.log("Validation failed:", errorInfo);
      });
  };

  return (
    <div>
      <Card
        style={{
          borderRadius: "20px",
          backgroundColor: "#316999",
          color: "white",
        }}
      >
        <Title style={{ color: "white" }}>General Informations</Title>
        <p className="card-description">
          In this section, you'll select the project name, project group, and
          database type to kickstart your Spring Boot project creation process
        </p>
      </Card>
      <Form
        labelAlign="left"
        labelCol={{ span: 4 }}
        layout="horizontal"
        size="large"
        form={form}
      >
        <Title>Project settings</Title>
        <Form.Item
          label="Project name"
          name={["properties", "name"]}
          rules={[{ required: true, message: "Please enter the project name" }]}
        >
          <Input placeholder="Enter application name" />
        </Form.Item>
        <Form.Item
          label="Project group"
          name={["properties", "groupId"]}
          rules={[
            { required: true, message: "Please enter the project group" },
          ]}
        >
          <Input placeholder="Enter project group : com.example" />
        </Form.Item>
        <Form.Item
          label="Artifact ID"
          name={["properties", "artifactId"]}
          rules={[{ required: true, message: "Please enter the artifact ID" }]}
        >
          <Input placeholder="Enter artifact ID : myapplication" />
        </Form.Item>
        <Form.Item
          label="Description"
          name={["properties", "description"]}
          rules={[{ required: true, message: "Please enter the description" }]}
        >
          <Input placeholder="Enter description : This is my Spring Boot application" />
        </Form.Item>
        <Form.Item
          label="Application port"
          name={["properties", "applicationPort"]}
          rules={[
            { required: true, message: "Please enter the application port" },
          ]}
        >
          <Input placeholder="Enter application port : 8080" />
        </Form.Item>
        <Form.Item
          label="Developer name"
          name={["properties", "developerName"]}
          rules={[
            { required: true, message: "Please enter the developer name" },
          ]}
        >
          <Input placeholder="Enter developer name : Developer" />
        </Form.Item>
        <Form.Item
          label="Spring version"
          name={["properties", "springVersion"]}
          rules={[
            { required: true, message: "Please select the Spring version" },
          ]}
        >
          <Radio.Group>
            <Radio value="2.6.3">2.6.3</Radio>
            <Radio value="2.6.4">2.6.4</Radio>
          </Radio.Group>
        </Form.Item>
        <Form.Item
          label="Constant file"
          valuePropName="checked"
          name={["properties", "isConstantFile"]}
        >
          <Checkbox>Enable</Checkbox>
        </Form.Item>
        <Form.Item
          label="Global exception enabled"
          valuePropName="checked"
          name={["properties", "isGlobalExceptionEnabled"]}
        >
          <Checkbox>Enable</Checkbox>
        </Form.Item>
        <Form.Item
          label="Lombok enabled"
          valuePropName="checked"
          name={["properties", "isLombokEnabled"]}
        >
          <Checkbox>Enable</Checkbox>
        </Form.Item>
        <Form.Item
          label="Actuator enabled"
          valuePropName="checked"
          name={["properties", "isActuatorEnabled"]}
        >
          <Checkbox>Enable</Checkbox>
        </Form.Item>
        <Title>Database</Title>
        <Form.Item
          label="database "
          name={["database", "isDatabaseEnabled"]}
          valuePropName="checked"
        >
          <Checkbox onChange={changeDatabaseState}>Enable</Checkbox>
        </Form.Item>
        {database && (
          <>
            <Form.Item
              label="Database Type"
              name={["database", "databaseType"]}
              rules={[
                { required: true, message: "Please select the database type" },
              ]}
            >
              <Radio.Group>
                <Radio value="mysql">mysql</Radio>
                <Radio value="postgresql">postgresql</Radio>
                <Radio value="mongodb">mongodb</Radio>
              </Radio.Group>
            </Form.Item>
            <Form.Item
              label="User Name"
              name={["database", "userName"]}
              rules={[
                { required: true, message: "Please enter the user name" },
              ]}
            >
              <Input placeholder="Enter user name : root" />
            </Form.Item>
            <Form.Item
              label="Password"
              name={["database", "password"]}
              rules={[{ required: true, message: "Please enter the password" }]}
            >
              <Input.Password placeholder="Enter password" />
            </Form.Item>
          </>
        )}
        <Title>Swagger</Title>
        <Form.Item label="Enable Swagger">
          <Form.Item name={["swagger", "isEnabled"]} valuePropName="checked">
            <Checkbox onChange={changeSwaggerState}>Enable</Checkbox>
          </Form.Item>
        </Form.Item>
        {swagger && (
          <>
            <Form.Item label="API Doc Name">
              <Form.Item name={["swagger", "apiDocName"]}>
                <Input />
              </Form.Item>
            </Form.Item>
            <Form.Item label="Developer Email">
              <Form.Item name={["swagger", "developerEmailId"]}>
                <Input />
              </Form.Item>
            </Form.Item>
          </>
        )}
        <Title>Deployment</Title>
        <Form.Item label="Is docker enabled">
          <Form.Item
            name={["deployment", "isDockerEnabled"]}
            valuePropName="checked"
          >
            <Checkbox>Enable</Checkbox>
          </Form.Item>
        </Form.Item>
        <Button onClick={handleOk}>Submit</Button>
      </Form>
    </div>
  );
}

export default General;
