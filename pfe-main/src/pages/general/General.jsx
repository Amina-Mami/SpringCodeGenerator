// import React, { useState, useContext } from "react";
// import {
//   Button,
//   Card,
//   Checkbox,
//   Form,
//   Input,
//   Radio,
//   Typography,
//   message,
// } from "antd";
// import axios from "axios";
// import { EntityContext } from "../../context/EntityContext";

// const { Title } = Typography;

// function General() {
//   const [form] = Form.useForm();
//   const [database, setDatabase] = useState(false);
//   const [swagger, setSwagger] = useState(false);
//   const [lombok, setLombok] = useState(false);
//   const { entities } = useContext(EntityContext);

//   const changeDatabaseState = () => setDatabase(!database);
//   const changeSwaggerState = () => setSwagger(!swagger);
//   const changeLombokState = () => setLombok(!lombok);

//   const handleOk = () => {
//     form
//       .validateFields()
//       .then((values) => {
//         const requestData = {
//           ...values,
//           entities: entities,
//         };
//         console.log(requestData);

//         axios
//           .post("http://localhost:7070/project/create/4", requestData)
//           .then((response) => {
//             console.log("Response:", response.data);

//             message.success("Project created successfully");
//             form.resetFields();
//           })
//           .catch((error) => {
//             console.error("Error:", error);
//             message.error("Failed to create project");
//           });
//       })
//       .catch((errorInfo) => {
//         console.log("Validation failed:", errorInfo);
//       });
//   };

//   return (
//     <div>
//       <Card
//         style={{
//           borderRadius: "20px",
//           backgroundColor: "#316999",
//           color: "white",
//           marginBottom: 20,
//         }}
//       >
//         <Title style={{ color: "white" }}>General Informations</Title>
//         <p className="card-description">
//           In this section, you'll select the project name, project group, and
//           optional configurations to kickstart your Spring Boot project creation
//           process
//         </p>
//       </Card>
//       <Form
//         labelCol={{ span: 6 }}
//         wrapperCol={{ span: 14 }}
//         layout="horizontal"
//         size="large"
//         form={form}
//       >
//         <Card title="Project Settings">
//           <Form.Item
//             label="Project name"
//             name={["properties", "name"]}
//             rules={[
//               { required: true, message: "Please enter the project name" },
//             ]}
//           >
//             <Input placeholder="Enter application name" />
//           </Form.Item>
//           <Form.Item
//             label="Project group"
//             name={["properties", "groupId"]}
//             rules={[
//               { required: true, message: "Please enter the project group" },
//             ]}
//           >
//             <Input placeholder="Enter project group : com.example" />
//           </Form.Item>
//           <Form.Item
//             label="Artifact ID"
//             name={["properties", "artifactId"]}
//             rules={[
//               { required: true, message: "Please enter the artifact ID" },
//             ]}
//           >
//             <Input placeholder="Enter artifact ID : myapplication" />
//           </Form.Item>
//           <Form.Item
//             label="Description"
//             name={["properties", "description"]}
//             rules={[
//               { required: true, message: "Please enter the description" },
//             ]}
//           >
//             <Input placeholder="Enter description : This is my Spring Boot application" />
//           </Form.Item>
//           <Form.Item
//             label="Application port"
//             name={["properties", "applicationPort"]}
//             rules={[
//               { required: true, message: "Please enter the application port" },
//             ]}
//           >
//             <Input placeholder="Enter application port : 8080" />
//           </Form.Item>
//           <Form.Item
//             label="Developer name"
//             name={["properties", "developerName"]}
//             rules={[
//               { required: true, message: "Please enter the developer name" },
//             ]}
//           >
//             <Input placeholder="Enter developer name : Developer" />
//           </Form.Item>
//           <Form.Item
//             label="Developer email"
//             name={["properties", "email"]}
//             rules={[
//               { required: true, message: "Please enter the developer email" },
//             ]}
//           >
//             <Input placeholder="Enter developer name : Developer" />
//           </Form.Item>
//           {/* <Form.Item
//           label="Spring version"
//           name={["properties", "springVersion"]}
//           rules={[
//             { required: true, message: "Please select the Spring version" },
//           ]}
//         >
//           <Radio.Group>
//             <Radio value="2.6.3">2.6.3</Radio>
//             <Radio value="2.6.4">2.6.4</Radio>
//           </Radio.Group>
//         </Form.Item> */}
//           {/* <Form.Item
//           label="Constant file"
//           valuePropName="checked"
//           name={["properties", "isConstantFile"]}
//         >
//           <Checkbox>Enable</Checkbox>
//         </Form.Item> */}
//           {/* <Form.Item
//           label="Global exception enabled"
//           valuePropName="checked"
//           name={["properties", "isGlobalExceptionEnabled"]}
//         >
//           <Checkbox>Enable</Checkbox>
//         </Form.Item> */}

//           {/* <Form.Item
//           label="Actuator enabled"
//           valuePropName="checked"
//           name={["properties", "isActuatorEnabled"]}
//         >
//           <Checkbox>Enable</Checkbox>
//         </Form.Item> */}
//         </Card>

//         <Card
//           title="Database"
//           style={{ marginTop: 20 }}
//           extra={
//             <Checkbox onChange={changeDatabaseState} checked={database}>
//               Enable
//             </Checkbox>
//           }
//         >
//           {database && (
//             <>
//               <Form.Item
//                 label="Database Type"
//                 name={["database", "databaseType"]}
//                 rules={[
//                   {
//                     required: true,
//                     message: "Please select the database type",
//                   },
//                 ]}
//               >
//                 <Radio.Group>
//                   <Radio value="mysql">mysql</Radio>
//                   <Radio value="postgresql">postgresql</Radio>
//                   <Radio value="mongodb">mongodb</Radio>
//                 </Radio.Group>
//               </Form.Item>
//               {/* Add other database form items */}
//             </>
//           )}
//         </Card>

//         <Card
//           title="Swagger"
//           style={{ marginTop: 20 }}
//           extra={
//             <Checkbox onChange={changeSwaggerState} checked={swagger}>
//               Enable
//             </Checkbox>
//           }
//         >
//           {swagger && (
//             <Form.Item
//               label="Enable Swagger"
//               name={["swagger", "isEnabled"]}
//               valuePropName="checked"
//             >
//               <Checkbox>Enable</Checkbox>
//             </Form.Item>
//           )}
//         </Card>
//         <Card
//           title="Lombok"
//           style={{ marginTop: 20 }}
//           extra={
//             <Checkbox onChange={changeLombokState} checked={lombok}>
//               Enable
//             </Checkbox>
//           }
//         >
//           {lombok && (
//             <Form.Item
//               label="Enable Lombok"
//               name={["properties", "isEnabled"]}
//               valuePropName="checked"
//             >
//               <Checkbox>Enable</Checkbox>
//             </Form.Item>
//           )}
//         </Card>

//         <Card title="Deployment" style={{ marginTop: 20 }}>
//           <Form.Item
//             label="Is docker enabled"
//             name={["deployment", "isDockerEnabled"]}
//             valuePropName="checked"
//           >
//             <Checkbox>Enable</Checkbox>
//           </Form.Item>
//         </Card>

//         <Button type="primary" onClick={handleOk} style={{ marginTop: 20 }}>
//           Submit
//         </Button>
//       </Form>
//     </div>
//   );
// }

// export default General;

import React, { useState, useContext } from "react";
import {
  Button,
  Card,
  Checkbox,
  Col,
  Form,
  Input,
  Radio,
  Row,
  Typography,
  message,
} from "antd";
import axios from "axios";
import { EntityContext } from "../../context/EntityContext";

const { Title } = Typography;

function General() {
  const [form] = Form.useForm();
  const [database, setDatabase] = useState(false);
  const [databaseType, setDatabaseType] = useState("");
  const [swagger, setSwagger] = useState(false);
  const { entities } = useContext(EntityContext);

  const changeDatabaseState = () => setDatabase(!database);
  const changeDatabaseType = (e) => setDatabaseType(e.target.value);
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
          .post("http://localhost:7070/project/create/4", requestData)
          .then((response) => {
            console.log("Response:", response.data);
            message.success("Project created successfully");
            form.resetFields();
          })
          .catch((error) => {
            console.error("Error:", error);
            message.error("Failed to create project");
          });
      })
      .catch((errorInfo) => {
        console.log("Validation failed:", errorInfo);
      });
  };

  return (
    <div style={styles.container}>
      <Card style={styles.card}>
        <Title level={3} style={styles.title}>
          General Information
        </Title>
        <p style={styles.description}>
          Fill in the details to kickstart your project creation process.
        </p>
      </Card>
      <Card style={styles.card}>
        <Form
          form={form}
          labelAlign="left"
          layout="vertical"
          size="large"
          onFinish={handleOk}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Project Name"
                name={["properties", "name"]}
                rules={[
                  { required: true, message: "Please enter the project name" },
                ]}
              >
                <Input placeholder="Enter application name" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Project Group"
                name={["properties", "groupId"]}
                rules={[
                  { required: true, message: "Please enter the project group" },
                ]}
              >
                <Input placeholder="Enter project group (e.g., com.example)" />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Artifact ID"
                name={["properties", "artifactId"]}
                rules={[
                  { required: true, message: "Please enter the artifact ID" },
                ]}
              >
                <Input placeholder="Enter artifact ID (e.g., myapplication)" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Application Port"
                name={["properties", "applicationPort"]}
                rules={[
                  {
                    required: true,
                    message: "Please enter the application port",
                  },
                ]}
              >
                <Input placeholder="Enter application port (e.g., 8080)" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item
            label="Description"
            name={["properties", "description"]}
            rules={[
              { required: true, message: "Please enter the description" },
            ]}
          >
            <Input.TextArea
              rows={4}
              placeholder="Enter description (e.g., This is my Spring Boot application)"
            />
          </Form.Item>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Developer Name"
                name={["properties", "developerName"]}
                rules={[
                  {
                    required: true,
                    message: "Please enter the developer name",
                  },
                ]}
              >
                <Input placeholder="Enter developer name" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Developer Email"
                name={["properties", "email"]}
                rules={[
                  {
                    required: true,
                    message: "Please enter the developer email",
                  },
                ]}
              >
                <Input placeholder="Enter developer email" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item
            label="Enable Database"
            name={["database", "databaseEnabled"]}
            valuePropName="checked"
          >
            <Checkbox onChange={changeDatabaseState}>Enable</Checkbox>
          </Form.Item>
          {database && (
            <>
              <Title level={4}>Database Configuration</Title>
              <Form.Item
                label="Database Type"
                name={["database", "databaseType"]}
                rules={[
                  {
                    required: true,
                    message: "Please select the database type",
                  },
                ]}
              >
                <Radio.Group onChange={changeDatabaseType}>
                  <Radio value="mysql">MySQL</Radio>
                  <Radio value="postgresql">PostgreSQL</Radio>
                  <Radio value="mongodb">MongoDB</Radio>
                </Radio.Group>
              </Form.Item>
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item
                    label="Port"
                    name={["database", "port"]}
                    rules={[
                      { required: true, message: "Please enter the port" },
                    ]}
                  >
                    <Input placeholder="Enter port (e.g., 3306)" />
                  </Form.Item>
                </Col>
                <Col span={12}>
                  <Form.Item
                    label="Database Name"
                    name={["database", "databaseName"]}
                    rules={[
                      {
                        required: true,
                        message: "Please enter the database name",
                      },
                    ]}
                  >
                    <Input placeholder="Enter database name" />
                  </Form.Item>
                </Col>
              </Row>
              {(databaseType === "mysql" || databaseType === "postgresql") && (
                <Row gutter={16}>
                  <Col span={12}>
                    <Form.Item
                      label="User Name"
                      name={["database", "userName"]}
                      rules={[
                        {
                          required: true,
                          message: "Please enter the user name",
                        },
                      ]}
                    >
                      <Input placeholder="Enter user name" />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      label="Password"
                      name={["database", "password"]}
                      rules={[
                        {
                          required: true,
                          message: "Please enter the password",
                        },
                      ]}
                    >
                      <Input.Password placeholder="Enter password" />
                    </Form.Item>
                  </Col>
                </Row>
              )}
            </>
          )}
          <Form.Item label="Enable Swagger" valuePropName="checked">
            <Checkbox onChange={changeSwaggerState}>Enable</Checkbox>
          </Form.Item>
          <div style={styles.footer}>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: "20px",
    backgroundColor: "#f0f2f5",
    minHeight: "100vh",
  },
  card: {
    width: "100%",
    maxWidth: "800px",
    margin: "20px 0",
    padding: "20px",
    background: "#fff",
    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
    borderRadius: "8px",
  },
  title: {
    textAlign: "center",
    color: "#1890ff",
  },
  description: {
    textAlign: "center",
    marginBottom: "20px",
    color: "#555",
  },
  footer: {
    display: "flex",
    justifyContent: "center",
    marginTop: "20px",
  },
};

export default General;
