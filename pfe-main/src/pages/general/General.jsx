import { useState, useContext } from "react";
import { saveAs } from "file-saver";
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
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
const { Title } = Typography;

function General() {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [database, setDatabase] = useState(false);
  const [databaseType, setDatabaseType] = useState("");
  const [swagger, setSwagger] = useState(false);
  const [front, setFront] = useState(false);
  const { entities, enumValues } = useContext(EntityContext);
  const { user } = useAuth();
  const navigate = useNavigate();

  const changeDatabaseState = () => setDatabase(!database);
  const changeDatabaseType = (e) => setDatabaseType(e.target.value);
  const changeSwaggerState = () => setSwagger(!swagger);
  const changeFrontState = () => setFront(!front);

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        const savedEntities =
          JSON.parse(localStorage.getItem("entities")) || [];
        const savedEnums = JSON.parse(localStorage.getItem("enums")) || [];

        const updatedEntities = savedEntities.map((entity) => ({
          ...entity,
          enums: savedEnums,
        }));

        console.log("Updated Entities with Enums:", updatedEntities);

        const requestData = {
          ...values,
          entities: updatedEntities,
          userId: user?.id,
          date: new Date().toISOString(),
        };
        requestData.properties.name = values.projectName;
        axios
          .post(
            `http://localhost:7070/project/create/${user.id}`,
            requestData,
            {
              responseType: "blob",
            }
          )
          .then((response) => {
            const contentDisposition = response.headers["content-disposition"];
            const filename = contentDisposition
              ? decodeURI(
                  contentDisposition.split("filename=")[1].replace(/"/g, "")
                )
              : "project.zip";

            const file = new Blob([response.data], { type: "application/zip" });
            saveAs(file, filename);
            message.success("Project created successfully");
            form.resetFields();
            navigate(`/dashboard`);
          })
          .catch((error) => {
            console.error("Error:", error);
            message.error("Failed to download project");
          });
      })
      .catch((errorInfo) => {
        console.log("Validation failed:", errorInfo);
        message.error("Validation failed. Please check your input.");
      });
  };

  return (
    <div style={styles.container}>
      <div style={styles.formContainer}>
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
                  name={["projectName"]}
                  rules={[
                    {
                      required: true,
                      message: "Please enter the project name",
                    },
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
                    {
                      required: true,
                      message: "Please enter the project group",
                    },
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

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  label="Enable Swagger"
                  name={["swagger", "isEnabled"]}
                  valuePropName="checked"
                >
                  <Checkbox onChange={changeSwaggerState}>Enable</Checkbox>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  label="Enable Lombok "
                  valuePropName="checked"
                  name={["properties", "isLombokEnabled"]}
                >
                  <Checkbox>Enable</Checkbox>
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  label="Enable frontend React"
                  name={["enableFrontendReact"]}
                  valuePropName="checked"
                >
                  <Checkbox onChange={changeFrontState}>Enable</Checkbox>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label="Enabled docker ">
                  <Form.Item
                    name={["properties", "isDockerEnabled"]}
                    valuePropName="checked"
                  >
                    <Checkbox>Enable</Checkbox>
                  </Form.Item>
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={16}>
              <Form.Item
                label="Enable Database"
                name={["database", "databaseEnabled"]}
                valuePropName="checked"
              >
                <Checkbox onChange={changeDatabaseState}>Enable</Checkbox>
              </Form.Item>
            </Row>

            {database && (
              <>
                <Row gutter={16}>
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
                </Row>
                {databaseType && (
                  <>
                    <Row gutter={16}>
                      <Col span={12}>
                        <Form.Item
                          label="Port"
                          name={["database", "port"]}
                          rules={[
                            {
                              required: true,
                              message: "Please enter the port",
                            },
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
                    {(databaseType === "mysql" ||
                      databaseType === "postgresql") && (
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
              </>
            )}

            <Form.Item>
              <div style={styles.buttonContainer}>
                <Button
                  style={{
                    backgroundColor: "#336699",
                    borderColor: "#336699",
                    color: "white",
                  }}
                  htmlType="submit"
                  loading={loading}
                >
                  {loading ? "Creating Project..." : "Create Project"}
                </Button>
              </div>
            </Form.Item>
          </Form>
        </Card>
      </div>
    </div>
  );
}

const styles = {
  container: {
    paddingTop: "100px",
    padding: "0px",
  },
  formContainer: {
    position: "relative",
    zIndex: 900,
    background: "#fff",
    padding: "20px",
    boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
  },
  card: {
    marginBottom: "20px",
  },
  title: {
    textAlign: "center",
  },
  description: {
    textAlign: "center",
    marginBottom: "20px",
  },
  buttonContainer: {
    display: "flex",
    justifyContent: "center",
    marginTop: "30px",
  },
};

export default General;
