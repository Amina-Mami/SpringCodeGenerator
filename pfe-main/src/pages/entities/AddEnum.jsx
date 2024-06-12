import React, { useEffect, useState } from "react";
import { Form, Modal, Input, Button, Alert } from "antd";

const AddEnum = ({ isOpen, onClose, onSubmit }) => {
  const [form] = Form.useForm();
  const [valuesError, setValuesError] = useState(null);
  const [enumValues, setEnumValues] = useState([""]);

  useEffect(() => {
    form.resetFields();
    setEnumValues([""]);
  }, [isOpen, form]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const formattedValues = enumValues.filter((v) => v.trim() !== "");
      if (formattedValues.length === 0) {
        setValuesError("Enum values cannot be empty.");
        return;
      }
      onSubmit({ name: values.name, values: formattedValues });
      onClose();
    } catch (errorInfo) {
      console.log("Validation Error:", errorInfo);
    }
  };

  const validateValues = (rule, value) => {
    const values = value.split(",").map((v) => v.trim());
    const pattern = /^[A-Z0-9_]+$/;
    const invalidValues = values.filter((v) => !pattern.test(v));
    if (invalidValues.length > 0) {
      setValuesError(
        "Please only use capital letters, numbers, and underscores for enum values."
      );
    } else {
      setValuesError(null);
    }
    return Promise.resolve();
  };

  const addInput = () => {
    setEnumValues([...enumValues, ""]);
  };

  const removeInput = (index) => {
    const updatedValues = [...enumValues];
    updatedValues.splice(index, 1);
    setEnumValues(updatedValues);
  };

  const handleInputChange = (value, index) => {
    const updatedValues = [...enumValues];
    updatedValues[index] = value;
    setEnumValues(updatedValues);
  };

  return (
    <Modal
      title="Add Enum"
      open={isOpen}
      onCancel={onClose}
      footer={[
        <Button key="cancel" onClick={onClose}>
          Cancel
        </Button>,
        <Button
          key="submit"
          type="primary"
          onClick={handleSubmit}
          disabled={valuesError}
        >
          Save
        </Button>,
      ]}
    >
      {valuesError && (
        <Alert
          message={valuesError}
          type="error"
          showIcon
          style={{ marginBottom: "10px" }}
        />
      )}
      <Form form={form} layout="vertical">
        <Form.Item
          label="Enum Name"
          name="name"
          rules={[
            {
              required: true,
              message: "Please enter the enum name.",
            },
          ]}
        >
          <Input placeholder="Enter enum name" />
        </Form.Item>
        {enumValues.map((value, index) => (
          <Form.Item
            key={index}
            label={index === 0 ? "Enum Values" : ""}
            name={`values[${index}]`}
            rules={[
              {
                required: true,
                message: "Please enter an enum value.",
              },
              { validator: validateValues },
            ]}
          >
            <Input
              placeholder="Enter enum value"
              value={value}
              onChange={(e) => handleInputChange(e.target.value, index)}
              addonAfter={
                index > 0 && (
                  <Button type="link" danger onClick={() => removeInput(index)}>
                    Remove
                  </Button>
                )
              }
            />
          </Form.Item>
        ))}
      </Form>
      <Button onClick={addInput}>Add Another</Button>
    </Modal>
  );
};

export default AddEnum;
