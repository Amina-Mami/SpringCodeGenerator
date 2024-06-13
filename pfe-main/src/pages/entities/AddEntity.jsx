import React, { useState, useEffect } from "react";
import {
  Button,
  Form,
  Input,
  Checkbox,
  Space,
  Modal,
  Select,
  Divider,
} from "antd";
import "./Entities.css";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";

function AddEntity({ isOpen, onCancel, onSubmit, entity, enumValues }) {
  const [form] = Form.useForm();
  const [selectedOptions, setSelectedOptions] = useState({});

  useEffect(() => {
    if (entity) {
      form.setFieldsValue({
        name: entity.name,
        primaryKey: {
          name: entity.primaryKey?.name || "",
          type: entity.primaryKey?.type || "",
        },
        fields:
          entity.fields?.map((field, index) => ({
            ...field,
            key: index,
          })) || [],
        restEndpoints: entity.crud || false,
      });
    } else {
      form.resetFields();
    }
  }, [entity, form]);
  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        if (values.primaryKey.isPrimary) {
          values.primaryKey.isPrimary = true;
        }

        form.resetFields();
        onSubmit(values);
      })
      .catch((info) => {
        console.log("Validate Failed:", info);
      });
  };

  const handleCancel = () => {
    form.resetFields();
    onCancel();
  };

  const options = [
    { value: "String", label: "String" },
    { value: "Date", label: "Date" },
    { value: "Integer", label: "Integer" },
    { value: "Long", label: "Long" },
    { value: "Boolean", label: "Boolean" },

    ...(enumValues || []).map((enumData) => ({
      value: enumData.name,
      label: enumData.name,
      enumValues: enumData.values,
    })),
  ];

  const onSelectChange = (value, key) => {
    setSelectedOptions({ ...selectedOptions, [key]: value });

    if (value === "primarykey") {
      form.setFieldsValue({
        primaryKey: {
          primaryKey: true,
        },
      });
    }
  };

  const isDisabled = (value) => {
    return value === "option1" || value === "option3";
  };

  return (
    <Modal
      open={isOpen}
      onCancel={handleCancel}
      onOk={handleOk}
      width={600}
      title={"Add Entity"}
    >
      <Form
        layout="vertical"
        name="addEntityForm"
        className="entities-form"
        form={form}
        oktext="Confirm"
      >
        <Form.Item
          label="Name"
          name="name"
          rules={[{ required: true, message: "Please provide a value." }]}
        >
          <Input placeholder="Entity name" />
        </Form.Item>
        <Space style={{ display: "flex", height: "30px" }} align="baseline">
          <Form.Item valuePropName="checked">
            <Checkbox>Mapped superclass</Checkbox>
          </Form.Item>
          <Form.Item name="restEndpoints" valuePropName="checked">
            <Checkbox>Add REST endpoints</Checkbox>
          </Form.Item>
        </Space>
        <Divider style={{ margin: "10px 0" }} />
        <Space style={{ display: "flex", width: "100%" }} align="baseline">
          <Form.Item
            name={["primaryKey", "type"]}
            rules={[{ required: true, message: "Missing field" }]}
            style={{ width: "267px" }}
          >
            <Select
              placeholder="Primary Key Type"
              size={"middle"}
              style={{ width: "267px" }}
              options={options}
              onChange={(value) => onSelectChange(value, "primaryKeyType")}
            />
          </Form.Item>
          <Form.Item
            name={["primaryKey", "name"]}
            rules={[{ required: true, message: "Missing field" }]}
            style={{ width: "267px" }}
          >
            <Input placeholder="Primary Key Name" />
          </Form.Item>
        </Space>
        <Space style={{ display: "flex", height: "30px" }} align="baseline">
          <Form.Item name={["primaryKey", "isPrimary"]} valuePropName="checked">
            <Checkbox>Primary key</Checkbox>
          </Form.Item>
        </Space>
        <Divider style={{ margin: "10px 0" }} />
        <Form.List name="fields" initialValue={[{}]}>
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name, ...restField }) => (
                <div key={key}>
                  <Space
                    style={{ display: "flex", width: "100%" }}
                    align="baseline"
                  >
                    <Form.Item
                      {...restField}
                      name={[name, "type"]}
                      rules={[{ required: true, message: "Missing field" }]}
                    >
                      <Select
                        placeholder="Field type"
                        size={"middle"}
                        style={{ width: "170px" }}
                        options={options}
                        onChange={(value) => onSelectChange(value, key)}
                      />
                    </Form.Item>
                    {/* Add the enum type dropdown here */}
                    {/* {selectedOptions[key] &&
                      enumValues &&
                      enumValues.find(
                        (enumData) => enumData.name === selectedOptions[key]
                      ) && (
                        <Form.Item
                          {...restField}
                          name={[name, "enumType"]}
                          rules={[{ required: true, message: "Missing field" }]}
                          style={{ width: "170px" }}
                        >
                          <Input placeholder="Enum name" />
                        </Form.Item>
                      )} */}
                    <Form.Item
                      {...restField}
                      name={[name, "field"]}
                      rules={[{ required: true, message: "Missing field" }]}
                      style={{ width: "170px" }}
                    >
                      <Input placeholder="Field name" />
                    </Form.Item>
                    <Form.Item
                      {...restField}
                      name={[name, "size"]}
                      rules={[{ required: false, message: "Missing field" }]}
                      style={{ width: "170px" }}
                    >
                      <Input
                        placeholder="Type size"
                        disabled={isDisabled(selectedOptions[key])}
                      />
                    </Form.Item>
                    <MinusCircleOutlined onClick={() => remove(name)} />
                  </Space>
                  <Space
                    style={{ display: "flex", height: "30px" }}
                    align="baseline"
                  >
                    <Form.Item
                      {...restField}
                      name={[name, "required"]}
                      valuePropName="checked"
                    >
                      <Checkbox value="required"> Required</Checkbox>
                    </Form.Item>
                    <Form.Item
                      {...restField}
                      name={[name, "unique"]}
                      valuePropName="checked"
                    >
                      <Checkbox value="unique">Unique</Checkbox>
                    </Form.Item>
                  </Space>
                  <Divider style={{ margin: "10px 0" }} />
                </div>
              ))}
              <div
                style={{
                  display: "flex",
                  justifyContent: "end",
                }}
              >
                <Form.Item>
                  <Button
                    type="dashed"
                    onClick={() => add()}
                    icon={<PlusOutlined />}
                  >
                    Add field
                  </Button>
                </Form.Item>
              </div>
            </>
          )}
        </Form.List>
      </Form>
    </Modal>
  );
}

export default AddEntity;
