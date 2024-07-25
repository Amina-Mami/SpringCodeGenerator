import React, { useState, useEffect, useContext } from "react";
import axios from "axios";
import { Button, Modal, Row, Col, Card } from "antd";
import { PlusOutlined, EditOutlined, DeleteOutlined } from "@ant-design/icons";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import AddEntity from "./AddEntity";
import AddEnum from "./AddEnum";
import AddRelationship from "./AddRelationship";
import { EntityContext } from "../../context/EntityContext";
import { v4 as uuidv4 } from "uuid";

const Entities = () => {
  const { entities, setEntities, enumValues, setEnumValues } =
    useContext(EntityContext);
  const [selectedEntity, setSelectedEntity] = useState(null);
  const [selectedEnum, setSelectedEnum] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddEnumModal, setShowAddEnumModal] = useState(false);
  const [showDeleteEnumModal, setShowDeleteEnumModal] = useState(false);
  const [localEntities, setLocalEntities] = useState([]);
  const [localEnums, setLocalEnums] = useState([]);
  const [jsonData, setJsonData] = useState(null);
  const [deleteIndex, setDeleteIndex] = useState(null);
  const [showRelationshipModal, setShowRelationshipModal] = useState(false);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const { projectId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    if (projectId) {
      axios
        .get(`http://localhost:7070/project/${projectId}`)
        .then((response) => {
          const data = JSON.parse(response.data.request_data_json);

          const updatedEntities = data.entities.map((entity) =>
            entity.id
              ? { ...entity, relationships: entity.relationships || [] }
              : { ...entity, id: uuidv4(), relationships: [] }
          );

          const extractedEnums = [];
          updatedEntities.forEach((entity) => {
            if (entity.enums) {
              entity.enums.forEach((enumItem) => {
                if (!extractedEnums.find((e) => e.name === enumItem.name)) {
                  extractedEnums.push(enumItem);
                }
              });
              delete entity.enums;
            }
          });

          const updatedData = {
            ...data,
            entities: updatedEntities,
            enums: extractedEnums,
          };

          setJsonData(updatedData);
          setEntities(updatedEntities);
          setEnumValues(extractedEnums);
          setLocalEntities(updatedEntities);
          setLocalEnums(extractedEnums);
        })
        .catch((error) => console.error("Error fetching data:", error));
    } else {
      console.error("Project ID is undefined");
    }
  }, [projectId]);

  useEffect(() => {
    const savedEntities = JSON.parse(localStorage.getItem("entities"));
    const savedEnums = JSON.parse(localStorage.getItem("enums"));

    if (savedEntities) {
      setLocalEntities(savedEntities);
    }
    if (savedEnums) {
      setLocalEnums(savedEnums);
    }
  }, []);

  useEffect(() => {
    localStorage.setItem("entities", JSON.stringify(localEntities));
  }, [localEntities]);

  useEffect(() => {
    localStorage.setItem("enums", JSON.stringify(localEnums));
  }, [localEnums]);

  const handleSaveEntity = (entity) => {
    if (!entity.id) {
      entity.id = uuidv4();
    }

    const newEntity = {
      ...entity,
      relationships: entity.relationships || [],
      fields: entity.fields || [],
      crud: entity.restEndpoints,
    };

    const updatedEntities = selectedEntity
      ? localEntities.map((ent) => (ent.id === newEntity.id ? newEntity : ent))
      : [...localEntities, newEntity];

    setLocalEntities(updatedEntities);
  };

  const handleDeleteEntity = () => {
    const updatedEntities = localEntities.filter(
      (_, index) => index !== deleteIndex
    );
    setLocalEntities(updatedEntities);
    setShowDeleteModal(false);
  };

  const handleSaveEnum = (enumData, index) => {
    const updatedEnums = [...localEnums];
    const formattedValues = enumData.values.map((v) =>
      typeof v === "object" ? v.values : v
    );

    const newEnumData = {
      ...enumData,
      values: formattedValues,
    };

    if (index !== undefined) {
      updatedEnums[index] = newEnumData;
    } else {
      updatedEnums.push(newEnumData);
    }

    setLocalEnums(updatedEnums);
    setShowAddEnumModal(false);
  };

  const handleDeleteEnum = () => {
    const updatedEnums = localEnums.filter((_, index) => index !== deleteIndex);
    setLocalEnums(updatedEnums);
    setShowDeleteEnumModal(false);
  };

  const handleSaveRelationship = (relationshipData) => {
    const sourceEntityIndex = localEntities.findIndex(
      (entity) => entity.id === relationshipData.sourceEntity
    );

    const updatedEntities = [...localEntities];

    if (sourceEntityIndex !== -1) {
      const sourceEntity = updatedEntities[sourceEntityIndex];

      const updatedRelationshipData = {
        sourceEntity: sourceEntity.name,
        targetEntity:
          localEntities.find(
            (entity) => entity.id === relationshipData.targetEntity
          )?.name || "",
        type: relationshipData.type,
        direction: relationshipData.direction,
      };

      if (!sourceEntity.relationships) {
        sourceEntity.relationships = [];
      }

      sourceEntity.relationships = [
        ...sourceEntity.relationships,
        updatedRelationshipData,
      ];

      updatedEntities[sourceEntityIndex] = sourceEntity;
    }

    setLocalEntities(updatedEntities);
  };

  const openModal = (entity) => {
    setSelectedEntity(entity);
    setIsOpen(true);
  };

  const closeModal = () => {
    setSelectedEntity(null);
    setIsOpen(false);
  };

  const openEnumModal = (enumItem, index) => {
    setSelectedEnum({ enumItem, index });
    setShowAddEnumModal(true);
  };

  const closeEnumModal = () => {
    setSelectedEnum(null);
    setShowAddEnumModal(false);
  };
  const handleUpdateProject = () => {
    const projectData = {
      entities: localEntities,
      enums: localEnums,
    };

    localStorage.setItem("projectData", JSON.stringify(projectData));
    console.log("Updated entities stored in localStorage:", projectData);

    navigate(`/dashboard/update-project/${projectId}`, { state: projectData });
  };

  const cancelbutton = () => {
    setLocalEntities([]);
    setLocalEnums([]);

    localStorage.removeItem("entities");
    localStorage.removeItem("enums");

    navigate(`/dashboard`);
  };

  const clearAll = () => {
    setLocalEntities([]);
    setLocalEnums([]);

    localStorage.removeItem("entities");
    localStorage.removeItem("enums");
  };

  const renderField = (fields) => {
    return fields.map((field, index) => (
      <div key={index}>{`${field.name}: ${field.type}`}</div>
    ));
  };

  const renderCrud = (crud) => {
    return crud ? "Yes" : "No";
  };

  const renderRelationships = (relationships = []) =>
    relationships.map((rel, index) => (
      <div key={index}>
        {rel.sourceEntity} - {rel.targetEntity} ({rel.type}, {rel.direction})
      </div>
    ));

  const renderEnumValues = (values) =>
    values.map((value, index) => (
      <div key={index}>{typeof value === "object" ? value.values : value}</div>
    ));
  const CapitalizeFirstLetter = (text) => {
    if (!text) return "";
    return text.charAt(0).toUpperCase() + text.slice(1);
  };
  return (
    <div>
      <Row gutter={[16, 16]} justify="center">
        <Col>
          <Button
            style={{
              backgroundColor: "#E27D60",
              borderColor: "#E27D60",
              color: "white",
            }}
            icon={<PlusOutlined />}
            onClick={() => setIsOpen(true)}
          >
            Add Entity
          </Button>
        </Col>
        <Col>
          <Button
            style={{
              backgroundColor: "#4A6A8C",
              borderColor: "#4A6A8C",
              color: "white",
            }}
            icon={<PlusOutlined />}
            onClick={() => setShowAddEnumModal(true)}
          >
            Add Enum
          </Button>
        </Col>
        <Col>
          <Button
            style={{
              backgroundColor: "#8A9A5B",
              borderColor: "#8A9A5B",
              color: "white",
            }}
            icon={<PlusOutlined />}
            onClick={() => setShowRelationshipModal(true)}
          >
            Add Relationship
          </Button>
        </Col>
      </Row>
      <Row gutter={[16, 16]} justify="center">
        <Button
          type="default"
          style={{ borderColor: "#B0B6B1", marginLeft: "0px" }}
          icon={<DeleteOutlined />}
          onClick={clearAll}
          className="me-3 mt-4"
        >
          Clear All
        </Button>
      </Row>
      <Row gutter={[16, 16]} className="mt-5">
        {localEntities.map((entity, index) => (
          <Col key={entity.id} span={8}>
            <Card
              //title={entity.name}
              title={CapitalizeFirstLetter(entity.name)}
              actions={[
                <EditOutlined key="edit" onClick={() => openModal(entity)} />,
                <DeleteOutlined
                  key="delete"
                  onClick={() => {
                    setDeleteIndex(index);
                    setShowDeleteModal(true);
                  }}
                />,
              ]}
            >
              <div>
                {entity.primaryKey.name}: {entity.primaryKey.type}
                {renderField(entity.fields)}
              </div>
              <div className="mt-3">
                <strong>CRUD:</strong> {renderCrud(entity.crud)}
              </div>
              <div>
                <strong>Relationships:</strong>
                {renderRelationships(entity.relationships)}
              </div>
            </Card>
          </Col>
        ))}
      </Row>

      <Row gutter={[16, 16]} className="mt-5">
        {localEnums.map((enumItem, index) => (
          <Col key={index} span={8}>
            <Card
              // title={enumItem.name}
              title={`"enum" :
                 ${enumItem.name}`}
              actions={[
                <EditOutlined
                  key="edit"
                  onClick={() => openEnumModal(enumItem, index)}
                />,
                <DeleteOutlined
                  key="delete"
                  onClick={() => {
                    setDeleteIndex(index);
                    setShowDeleteEnumModal(true);
                  }}
                />,
              ]}
            >
              {renderEnumValues(enumItem.values)}
            </Card>
          </Col>
        ))}
      </Row>

      <Modal
        title="Delete Entity"
        visible={showDeleteModal}
        onOk={handleDeleteEntity}
        onCancel={() => setShowDeleteModal(false)}
        okText="Delete"
        okButtonProps={{ danger: true }}
      >
        <p>Are you sure you want to delete this entity?</p>
      </Modal>

      <Modal
        title="Delete Enum"
        visible={showDeleteEnumModal}
        onOk={handleDeleteEnum}
        onCancel={() => setShowDeleteEnumModal(false)}
        okText="Delete"
        okButtonProps={{ danger: true }}
      >
        <p>Are you sure you want to delete this enum?</p>
      </Modal>

      <AddEntity
        isOpen={isOpen}
        onClose={closeModal}
        onSubmit={handleSaveEntity}
        entity={selectedEntity}
        enumValues={localEnums}
      />

      <AddEnum
        isOpen={showAddEnumModal}
        onClose={closeEnumModal}
        onSave={handleSaveEnum}
        enumData={selectedEnum}
      />

      <AddRelationship
        isOpen={showRelationshipModal}
        onClose={() => setShowRelationshipModal(false)}
        onSave={handleSaveRelationship}
        entities={localEntities}
      />

      {location.pathname.includes("/dashboard/project") && (
        <Row gutter={[16, 16]} justify="end">
          <Col>
            <Button
              type="primary"
              style={{ backgroundColor: "#336699", borderColor: "#336699" }}
              onClick={handleUpdateProject}
            >
              Update Project Settings
            </Button>
          </Col>
          <Col>
            <Button type="default" onClick={cancelbutton}>
              Cancel
            </Button>
          </Col>
        </Row>
      )}
    </div>
  );
};

export default Entities;
