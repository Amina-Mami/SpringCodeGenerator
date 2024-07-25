import React, { useEffect, useState } from "react";
import { Form, Modal, Select, Button } from "antd";

const { Option } = Select;

const AddRelationship = ({ isOpen, onClose, onSave, entities }) => {
  const [form] = Form.useForm();
  const [relationshipType, setRelationshipType] = useState(null);
  const [direction, setDirection] = useState(null);

  useEffect(() => {
    form.resetFields();
    setRelationshipType(null);
    setDirection(null);
  }, [isOpen, form, entities]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      onSave({
        sourceEntity: values.sourceEntity,
        targetEntity: values.targetEntity,
        type: values.relationshipType,
        direction: values.direction,
      });
      onClose();
    } catch (errorInfo) {
      console.log("Validation Error:", errorInfo);
    }
  };

  return (
    <Modal
      title="Add Relationship"
      visible={isOpen}
      onCancel={onClose}
      onOk={handleSubmit}
      footer={[
        <Button key="back" onClick={onClose}>
          Cancel
        </Button>,
        <Button key="submit" type="primary" onClick={handleSubmit}>
          Save
        </Button>,
      ]}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          label="Relationship Type"
          name="relationshipType"
          rules={[
            { required: true, message: "Please select a relationship type." },
          ]}
        >
          <Select
            placeholder="Please select a relationship type"
            onChange={(value) => setRelationshipType(value)}
            value={relationshipType}
          >
            <Option value="OneToOne">One to One</Option>
            <Option value="OneToMany">One to Many</Option>
            <Option value="ManyToOne">Many to One</Option>
            <Option value="ManyToMany">Many to Many</Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Direction"
          name="direction"
          rules={[{ required: true, message: "Please select a direction." }]}
        >
          <Select
            placeholder="Please select a direction"
            onChange={(value) => setDirection(value)}
            value={direction}
          >
            <Option value="Unidirectional">Unidirectional</Option>
            <Option value="Bidirectional">Bidirectional</Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="From Entity"
          name="sourceEntity"
          rules={[{ required: true, message: "Please select a from entity." }]}
        >
          <Select placeholder="Please select an entity">
            {entities.map((entity) => (
              <Option key={entity.id} value={entity.id}>
                {entity.name}
              </Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item
          label="To Entity"
          name="targetEntity"
          rules={[{ required: true, message: "Please select a to entity." }]}
        >
          <Select placeholder="Please select an entity">
            {entities.map((entity) => (
              <Option key={entity.id} value={entity.id}>
                {entity.name}
              </Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddRelationship;
