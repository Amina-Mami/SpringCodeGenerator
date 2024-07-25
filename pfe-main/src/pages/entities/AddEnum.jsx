import { useEffect, useState, useContext } from "react";
import { Form, Modal, Input, Button, Alert } from "antd";
import { EntityContext } from "../../context/EntityContext";

const AddEnum = ({ isOpen, onClose, onSave, enumData }) => {
  const [form] = Form.useForm();
  const [valuesError, setValuesError] = useState(null);
  const [enumValues, setEnumValues] = useState([""]);
  const { updateEnumValues } = useContext(EntityContext);

  useEffect(() => {
    form.resetFields();
    if (enumData) {
      const { enumItem } = enumData;
      form.setFieldsValue({ name: enumItem.name });
      setEnumValues(enumItem.values.map((v) => v.values));
    } else {
      setEnumValues([""]);
    }
  }, [isOpen, form, enumData]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const formattedValues = enumValues.filter((v) => v.trim() !== "");
      if (formattedValues.length === 0) {
        setValuesError("Enum values cannot be empty.");
        return;
      }
      const duplicates = formattedValues.filter(
        (item, index) => formattedValues.indexOf(item) !== index
      );
      if (duplicates.length > 0) {
        setValuesError("Enum values must be unique.");
        return;
      }
      onSave({ name: values.name, values: formattedValues }, enumData?.index);
      updateEnumValues(enumData?.index, formattedValues);
      onClose();
    } catch (errorInfo) {
      console.log("Validation Error:", errorInfo);
    }
  };

  const validateValues = (rule, value) => {
    const values = value.split(",").map((v) => v.trim());
    const pattern = /^[A-Z]+$/;
    const invalidValues = values.filter((v) => !pattern.test(v));
    const duplicates = values.filter(
      (item, index) => values.indexOf(item) !== index
    );
    if (invalidValues.length > 0) {
      setValuesError("Please only use capitalized letters for enum values.");
    } else if (duplicates.length > 0) {
      setValuesError("Enum values must be unique.");
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
    updatedValues[index] = value.toUpperCase();
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
