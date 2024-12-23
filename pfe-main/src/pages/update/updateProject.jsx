import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Form, Input, Checkbox, Button, message, Row, Col, Radio } from "antd";
import axios from "axios";

const UpdateProject = () => {
  const [loading, setLoading] = useState(false);
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [projectData, setProjectData] = useState(null);
  const [database, setDatabase] = useState(false);
  const [projectName, setProjectName] = useState(false);
  const [databaseType, setDatabaseType] = useState("");
  const [localEntities, setLocalEntities] = useState([]);
  const [localEnums, setLocalEnums] = useState([]);

  const [form] = Form.useForm();

  // useEffect(() => {
  //   axios
  //     .get(`http://localhost:7070/project/update/${projectId}`)
  //     .then((response) => {
  //       console.log("Raw project data:", response.data);

  //       try {
  //         const rawJson = response.data.requestDataJson;

  //         const jsonData =
  //           typeof rawJson === "string" ? JSON.parse(rawJson) : rawJson;

  //         // Update state and form

  //         setProjectData(jsonData);

  //         form.setFieldsValue(jsonData);

  //         if (jsonData.database && jsonData.database.databaseEnabled) {
  //           setDatabase(true);
  //           setDatabaseType(jsonData.database.databaseType);
  //         }
  //         if (jsonData.entities) {
  //           console.log("Fetched entities:", jsonData.entities);
  //         }
  //       } catch (error) {
  //         console.error("Error parsing project data:", error);
  //         message.error(
  //           "Failed to parse project data. Please check the backend."
  //         );
  //       }
  //     })
  //     .catch((error) => {
  //       console.error("Error fetching project data:", error);
  //       message.error(
  //         "Failed to fetch project data. Please check the backend."
  //       );
  //     });
  // }, [projectId, form]);

  useEffect(() => {
    axios
      .get(`http://localhost:7070/project/update/${projectId}`)
      .then((response) => {
        console.log("Raw project data:", response.data);

        try {
          const rawJson = response.data.requestDataJson;

          const jsonData =
            typeof rawJson === "string" ? JSON.parse(rawJson) : rawJson;

          // Update state and form
          setProjectData(jsonData);

          // Set the project name separately
          form.setFieldsValue({ projectName: jsonData.properties.name });

          form.setFieldsValue(jsonData);

          if (jsonData.database && jsonData.database.databaseEnabled) {
            setDatabase(true);
            setDatabaseType(jsonData.database.databaseType);
          }
          if (jsonData.entities) {
            console.log("Fetched entities:", jsonData.entities);
          }
        } catch (error) {
          console.error("Error parsing project data:", error);
          message.error(
            "Failed to parse project data. Please check the backend."
          );
        }
      })
      .catch((error) => {
        console.error("Error fetching project data:", error);
        message.error(
          "Failed to fetch project data. Please check the backend."
        );
      });
  }, [projectId, form]);

  const cancelbutton = () => {
    setLocalEntities([]);
    setLocalEnums([]);

    localStorage.removeItem("entities");
    localStorage.removeItem("enums");

    navigate(`/dashboard`);
  };
  const handleUpdate = () => {
    setLoading(true);
    form
      .validateFields()
      .then((values) => {
        const projectData = localStorage.getItem("projectData");
        console.log("Retrieved project data from localStorage:", projectData);

        let requestDataJson = "";

        const mapEnumsToEntities = (entities, enums) => {
          return entities.map((entity) => {
            const entityEnums = enums.filter((enumItem) =>
              (entity.fields || []).some(
                (field) => field.type === enumItem.name
              )
            );
            return {
              ...entity,
              enums: entityEnums,
              relationships: entity.relationships || [],
            };
          });
        };

        if (projectData) {
          const parsedData = JSON.parse(projectData);

          const updatedEntities = mapEnumsToEntities(
            (parsedData.entities || []).map((entity, index) => {
              const newEntity =
                values.entities && values.entities[index]
                  ? values.entities[index]
                  : {};
              return {
                ...entity,
                ...newEntity,
                relationships: entity.relationships || [],
              };
            }),
            parsedData.enums || []
          );

          // Ensure properties.name is set to projectName
          const updatedProperties = {
            ...parsedData.properties,
            ...values.properties,
            name: values.projectName,
          };

          requestDataJson = {
            properties: updatedProperties,
            swagger: values.swagger || parsedData.swagger,
            enableFrontendReact: values.enableFrontendReact,
            database: values.database || parsedData.database,
            entities: updatedEntities,
            enums: [],
          };
        } else {
          const updatedEntities = mapEnumsToEntities(
            values.entities || [],
            values.enums || []
          );

          // Ensure properties.name is set to projectName
          const updatedProperties = {
            ...values.properties,
            name: values.projectName,
          };

          requestDataJson = {
            properties: updatedProperties,
            swagger: values.swagger,
            enableFrontendReact: values.enableFrontendReact,
            database: values.database,
            entities: updatedEntities,
            enums: [],
          };
        }

        const payload = {
          projectName: values.projectName,
          requestDataJson: requestDataJson,
        };

        console.log("Final payload before sending:", payload);

        axios
          .put(`http://localhost:7070/project/update/${projectId}`, payload, {
            headers: {
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            setLoading(false);
            console.log("Response from backend:", response.data);
            message.success("Project updated successfully");
            navigate(
              `/dashboard/entities?entities=${encodeURIComponent(
                JSON.stringify(values.entities)
              )}&enumValues=${encodeURIComponent(JSON.stringify(values.enums))}`
            );
          })
          .catch((error) => {
            setLoading(false);
            console.error("Error updating project:", error);
            message.error("Failed to update project");
          });
      })
      .catch((errorInfo) => {
        setLoading(false);
        console.log("Validation failed:", errorInfo);
        message.error("Validation failed. Please check your input.");
      });
  };

  const changeDatabaseState = () => setDatabase(!database);
  const changeDatabaseType = (e) => setDatabaseType(e.target.value);

  return (
    <Form
      form={form}
      labelAlign="left"
      layout="vertical"
      size="large"
      onFinish={handleUpdate}
    >
      {projectData && (
        <>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Project Name"
                name="projectName"
                rules={[
                  { required: true, message: "Please enter the project name" },
                ]}
              >
                <Input placeholder="Enter project name" />
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
          <Row gutter={16}>
            <Col span={12}>
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
            </Col>
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
                <Checkbox>Enable</Checkbox>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Enable Lombok"
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
                <Checkbox>Enable</Checkbox>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Enabled docker">
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
            <Row gutter={[16, 16]} justify="end">
              <Col>
                <Button
                  style={{
                    backgroundColor: "#336699",
                    borderColor: "#336699",
                    color: "white",
                  }}
                  htmlType="submit"
                  loading={loading}
                >
                  Update
                </Button>
              </Col>
              <Col>
                <Button type="default" onClick={cancelbutton}>
                  Cancel
                </Button>
              </Col>
            </Row>
          </Form.Item>
        </>
      )}
    </Form>
  );
};

export default UpdateProject;
