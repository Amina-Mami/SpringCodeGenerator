// import React, { useEffect, useState } from "react";
// import { Form, Modal, Select } from "antd";

// const AddRelationship = ({ isOpen, onClose, onSave, entities }) => {
//   const [form] = Form.useForm();
//   const [type, setType] = useState(null);

//   useEffect(() => {
//     form.resetFields();
//     setType(null);
//     console.log("Entities inside AddRelationship:", entities);
//   }, [isOpen, form, entities]);

//   const handleSubmit = async () => {
//     try {
//       const values = await form.validateFields();
//       onSave({
//         sourceEntity: values.sourceEntity,
//         targetEntity: values.targetEntity,
//         type: values.type,
//       });
//       onClose();
//     } catch (errorInfo) {
//       console.log("Validation Error:", errorInfo);
//     }
//   };

//   return (
//     <Modal
//       title="Add Relationship"
//       open={isOpen}
//       onCancel={onClose}
//       onOk={handleSubmit}
//     >
//       <Form form={form} layout="vertical">
//         <Form.Item
//           label="Relationship Type"
//           name="type"
//           rules={[
//             { required: true, message: "Please select a relationship type." },
//           ]}
//         >
//           <Select
//             placeholder="Please select a relationship type"
//             onChange={setType}
//             value={type}
//           >
//             <Select.Option value="OneToOne">One to One</Select.Option>
//             <Select.Option value="OneToMany">One to Many</Select.Option>
//             <Select.Option value="ManyToOne">Many to One</Select.Option>
//             <Select.Option value="ManyToMany">Many to Many</Select.Option>
//           </Select>
//         </Form.Item>

//         <Form.Item
//           label="From Entity"
//           name="sourceEntity"
//           rules={[{ required: true, message: "Please select a from entity." }]}
//         >
//           <Select placeholder="Please select an entity">
//             {entities?.map((entity) => (
//               <Select.Option key={entity.id} value={entity.id}>
//                 {entity.name}
//               </Select.Option>
//             ))}
//           </Select>
//         </Form.Item>

//         <Form.Item
//           label="To Entity"
//           name="targetEntity"
//           rules={[{ required: true, message: "Please select a to entity." }]}
//         >
//           <Select placeholder="Please select an entity">
//             {entities.map((entity) => (
//               <Select.Option key={entity.id} value={entity.id}>
//                 {entity.name}
//               </Select.Option>
//             ))}
//           </Select>
//         </Form.Item>
//       </Form>
//     </Modal>
//   );
// };

// export default AddRelationship;
import React, { useEffect, useState } from "react";
import { Form, Modal, Select } from "antd";

const AddRelationship = ({ isOpen, onClose, onSave, entities }) => {
  const [form] = Form.useForm();
  const [relationshipType, setRelationshipType] = useState(null);
  const [direction, setDirection] = useState(null);

  useEffect(() => {
    form.resetFields();
    setRelationshipType(null);
    setDirection(null);
    console.log("Entities inside AddRelationship:", entities);
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
            <Select.Option value="OneToOne">One to One</Select.Option>
            <Select.Option value="OneToMany">One to Many</Select.Option>
            <Select.Option value="ManyToOne">Many to One</Select.Option>
            <Select.Option value="ManyToMany">Many to Many</Select.Option>
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
            <Select.Option value="Unidirectional">Unidirectional</Select.Option>
            <Select.Option value="Bidirectional">Bidirectional</Select.Option>
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
