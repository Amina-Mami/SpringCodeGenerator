import React, { useEffect, useState } from "react";
import { Form, Modal, Select } from "antd";

const AddRelationship = ({ isOpen, onClose, onSave, entities }) => {
  const [form] = Form.useForm();
  const [type, setType] = useState(null);

  useEffect(() => {
    form.resetFields();
    setType(null);
    console.log("Entities inside AddRelationship:", entities);
  }, [isOpen, form, entities]);

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      onSave({
        sourceEntity: values.sourceEntity,
        targetEntity: values.targetEntity,
        type: values.type,
      });
      onClose();
    } catch (errorInfo) {
      console.log("Validation Error:", errorInfo);
    }
  };

  return (
    <Modal
      title="Add Relationship"
      open={isOpen}
      onCancel={onClose}
      onOk={handleSubmit}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          label="Relationship Type"
          name="type"
          rules={[
            { required: true, message: "Please select a relationship type." },
          ]}
        >
          <Select
            placeholder="Please select a relationship type"
            onChange={setType}
            value={type}
          >
            <Select.Option value="OneToOne">One to One</Select.Option>
            <Select.Option value="OneToMany">One to Many</Select.Option>
            <Select.Option value="ManyToOne">Many to One</Select.Option>
            <Select.Option value="ManyToMany">Many to Many</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="From Entity"
          name="sourceEntity"
          rules={[{ required: true, message: "Please select a from entity." }]}
        >
          <Select placeholder="Please select an entity">
            {entities?.map((entity) => (
              <Select.Option key={entity.id} value={entity.id}>
                {entity.name}
              </Select.Option>
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
              <Select.Option key={entity.id} value={entity.id}>
                {entity.name}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default AddRelationship;
