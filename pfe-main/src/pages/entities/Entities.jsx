import React, { useState, useContext, useEffect } from "react";
import {
  Button,
  Modal,
  Table,
  Card,
  Container,
  Row,
  Col,
} from "react-bootstrap";
import { PlusOutlined, DeleteOutlined, EditOutlined } from "@ant-design/icons";
import AddEntity from "./AddEntity";
import AddRelationship from "./AddRelationship";
import { EntityContext } from "../../context/EntityContext";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./Entities.css";
import AddEnum from "./AddEnum";
import { useNavigate } from "react-router-dom";

const Entities = () => {
  const { entities, setEntities } = useContext(EntityContext);
  const [isOpen, setIsOpen] = useState(false);
  const [selectedEntity, setSelectedEntity] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState(null);
  const [showRelationshipModal, setShowRelationshipModal] = useState(false);
  const [showAddEnumModal, setShowAddEnumModal] = useState(false);
  const [enumValues, setEnumValues] = useState([]);
  const [selectedEnum, setSelectedEnum] = useState(null);
  const navigate = useNavigate();
  const openModal = (entity) => {
    setSelectedEntity(entity);
    setIsOpen(true);
  };

  const closeModal = () => {
    setSelectedEntity(null);
    setIsOpen(false);
  };

  const handleClick = () => {
    navigate("/dashboard/general");
  };
  const handleSaveEntity = (entity) => {
    if (!entity || !entity.fields) {
      console.error("Entity or fields are undefined");
      return;
    }

    const formattedEntity = {
      id: entity.id || new Date().getTime().toString(),
      name: entity.name,
      primaryKey: entity.primaryKey
        ? { name: entity.primaryKey.name, type: entity.primaryKey.type }
        : undefined,
      fields: entity.fields.map((field) => ({
        name: field.field,
        type: field.type,
        size: field.size || undefined,
        required: field.required,
        unique: field.unique,
      })),
      crud: entity.restEndpoints,
      relationships: entity.relationships || [],
      enums: enumValues,
    };

    if (selectedEntity) {
      const updatedEntities = entities.map((existingEntity, index) =>
        index === entities.indexOf(selectedEntity)
          ? formattedEntity
          : existingEntity
      );
      setEntities(updatedEntities);
    } else {
      setEntities([...entities, formattedEntity]);
    }

    closeModal();
    toast.success("Entity saved successfully!");
  };

  const handleDeleteEntity = () => {
    setEntities((prevEntities) =>
      prevEntities.filter((_, index) => index !== deleteIndex)
    );
    setDeleteIndex(null);
    setShowDeleteModal(false);
  };

  const handleSaveEnum = (enumData) => {
    const { name, values } = enumData;

    const formattedValues = values.map((value) => ({ values: value }));

    const updatedEnumValues = [
      ...enumValues,
      { name, values: formattedValues },
    ];
    setEnumValues(updatedEnumValues);
    setShowAddEnumModal(false);
    toast.success("Enum saved successfully!");
  };

  const handleDeleteEnum = (index) => {
    setEnumValues((prevEnums) => prevEnums.filter((_, i) => i !== index));
    toast.success("Enum deleted successfully!");
  };

  const renderField = (fields) => {
    return (
      <ul>
        {fields.map((field, index) => (
          <li key={index}>{`${field.name} (${field.type})`}</li>
        ))}
      </ul>
    );
  };

  const renderCrud = (crud) => {
    return crud ? "Yes" : "No";
  };

  useEffect(() => {
    const handleBeforeUnload = (event) => {
      event.preventDefault();
      event.returnValue = "";
      return "";
    };

    if (entities.length > 0) {
      window.addEventListener("beforeunload", handleBeforeUnload);
    }

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [entities]);

  const handleSaveRelationship = (relationshipData) => {
    console.log("Relationship data:", relationshipData);

    const sourceEntity = entities.find(
      (entity) => entity.id === relationshipData.sourceEntity
    );
    const sourceEntityName = sourceEntity?.name || "";

    const targetEntity = entities.find(
      (entity) => entity.id === relationshipData.targetEntity
    );
    const targetEntityName = targetEntity?.name || "";

    const updatedRelationshipData = {
      sourceEntity: sourceEntityName,
      targetEntity: targetEntityName,
      type: relationshipData.type,
      direction: relationshipData.direction,
    };

    const updatedEntities = entities.map((entity) => {
      if (entity.id === relationshipData.sourceEntity) {
        return {
          ...entity,
          relationships: [...entity.relationships, updatedRelationshipData],
        };
      }
      return entity;
    });

    setEntities(updatedEntities);
    setShowRelationshipModal(false);
    toast.success("Relationship saved successfully!");
  };

  const renderRelationships = (relationships) => {
    return (
      <ul>
        {relationships.map((relationship, index) => (
          <li
            key={index}
          >{`${relationship.sourceEntity} - ${relationship.targetEntity} (${relationship.type})`}</li>
        ))}
      </ul>
    );
  };

  return (
    <>
      <Container fluid className="pt-4">
        <ToastContainer />
        <Row>
          <Col md={12}>
            <Card className="mb-4 shadow">
              <Card.Body className="p-0">
                <div className="d-flex justify-content-between p-3">
                  <h4>Entities</h4>
                  <div>
                    <Button
                      variant="primary"
                      className="me-2"
                      onClick={() => openModal(null)}
                    >
                      <PlusOutlined /> Add Entity
                    </Button>
                    <Button
                      variant="secondary"
                      className="me-2"
                      onClick={() => setShowAddEnumModal(true)}
                    >
                      <PlusOutlined /> Add Enum
                    </Button>
                    <Button
                      variant="warning"
                      onClick={() => {
                        setShowRelationshipModal(true);
                        setShowAddEnumModal(false);
                      }}
                    >
                      <PlusOutlined /> Relationships
                    </Button>
                  </div>
                </div>
                <Table responsive className="mb-0">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Fields</th>
                      <th>CRUD</th>
                      <th>Relationships</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {entities.map((entity, index) => (
                      <tr key={index}>
                        <td>{entity.name}</td>
                        <td>{renderField(entity.fields)}</td>
                        <td>{renderCrud(entity.crud)}</td>
                        <td>{renderRelationships(entity.relationships)}</td>
                        <td>
                          <Button
                            variant="info"
                            size="sm"
                            onClick={() => openModal(entity)}
                          >
                            <EditOutlined />
                          </Button>
                          <Button
                            variant="danger"
                            size="sm"
                            className="ms-2"
                            onClick={() => {
                              setDeleteIndex(index);
                              setShowDeleteModal(true);
                            }}
                          >
                            <DeleteOutlined />
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>

        <Row>
          <Col md={12}>
            <Card className="mb-4 shadow">
              <Card.Body className="p-0">
                <div className="d-flex justify-content-between p-3">
                  <h4>Enums</h4>
                </div>
                <Table responsive className="mb-0">
                  <thead>
                    <tr>
                      <th>Enum Name</th>
                      <th>Values</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {enumValues.map((enumData, index) => (
                      <tr key={index}>
                        <td>{enumData.name}</td>
                        <td>
                          {enumData.values.map((value, idx) => (
                            <span key={idx}>
                              {value.values}
                              {idx < enumData.values.length - 1 ? ", " : ""}
                            </span>
                          ))}
                        </td>
                        <td>
                          <Button
                            variant="info"
                            size="sm"
                            onClick={() => {
                              setSelectedEnum(enumData);
                              setShowAddEnumModal(true);
                            }}
                          >
                            <EditOutlined />
                          </Button>
                          <Button
                            variant="danger"
                            size="sm"
                            className="ms-2"
                            onClick={() => handleDeleteEnum(index)}
                          >
                            <DeleteOutlined />
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
            <Button
              variant="success"
              className="btncreate"
              onClick={handleClick}
            >
              <PlusOutlined /> Create Project
            </Button>
          </Col>
        </Row>
      </Container>

      <AddEntity
        isOpen={isOpen}
        onCancel={closeModal}
        onSubmit={handleSaveEntity}
        entity={selectedEntity}
        enumValues={enumValues}
      />

      <Modal
        show={showDeleteModal}
        onHide={() => setShowDeleteModal(false)}
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header closeButton>
          <Modal.Title>Confirm Delete</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete this entity?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteEntity}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>

      <AddRelationship
        isOpen={showRelationshipModal}
        onClose={() => setShowRelationshipModal(false)}
        entities={entities}
        onSave={handleSaveRelationship}
      />

      <AddEnum
        isOpen={showAddEnumModal}
        onClose={() => setShowAddEnumModal(false)}
        onSubmit={handleSaveEnum}
        enumData={selectedEnum}
      />
    </>
  );
};

export default Entities;
