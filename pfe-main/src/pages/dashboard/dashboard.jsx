import { useEffect, useState } from "react";
import { Table, Button, Space, Popconfirm, message } from "antd";
import {
  DeleteOutlined,
  EditOutlined,
  DownloadOutlined,
} from "@ant-design/icons";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation

const ProjectTable = () => {
  const { user, isAuthenticated } = useAuth();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Initialize useNavigate

  useEffect(() => {
    console.log("isAuthenticated:", isAuthenticated());
    console.log("user:", user);
    if (isAuthenticated() && user && user.id) {
      fetchProjects(user.id);
    } else {
      setLoading(false);
    }
  }, [user, isAuthenticated]);

  const fetchProjects = async (userId) => {
    try {
      const response = await axios.get(
        `http://localhost:7070/project/projects/${userId}`
      );
      console.log("Response from API:", response.data);
      setProjects(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching projects:", error);
      setError(error.message);
      setLoading(false);
    }
  };

  const handleDelete = async (projectId) => {
    try {
      const response = await axios.delete(
        `http://localhost:7070/project/delete/${projectId}`
      );
      if (response.status === 200) {
        message.success("Project deleted successfully");
        fetchProjects(user.id);
      } else {
        throw new Error("Failed to delete project");
      }
    } catch (error) {
      setError(error.message);
    }
  };

  const handleDownload = async (projectId, projectName) => {
    try {
      const response = await axios.get(
        `http://localhost:7070/project/download/${user.id}/${projectName}`,
        {
          responseType: "blob",
        }
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `${projectName}.zip`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      message.success("Project downloaded successfully");
    } catch (error) {
      console.error("Error downloading project:", error);
      setError(error.message);
    }
  };

  const handleUpdate = (projectId) => {
    navigate(`project/${projectId}`);
  };

  const columns = [
    {
      title: "Project",
      dataIndex: "projectName",
      key: "projectName",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="middle">
          <Button
            style={{
              backgroundColor: "#8A9A5B",
              borderColor: "#8A9A5B",
              color: "white",
            }}
            icon={<EditOutlined />}
            onClick={() => handleUpdate(record.id)}
          >
            Update
          </Button>

          <Popconfirm
            title="Are you sure you want to delete this project?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button
              style={{
                backgroundColor: "#E27D60",
                borderColor: "#E27D60",
                color: "white",
              }}
              icon={<DeleteOutlined />}
            >
              Delete
            </Button>
          </Popconfirm>

          <Button
            style={{
              backgroundColor: "#4A6A8C",
              borderColor: "#4A6A8C",
              color: "white",
            }}
            icon={<DownloadOutlined />}
            onClick={() => handleDownload(record.id, record.projectName)}
          >
            Download
          </Button>
        </Space>
      ),
    },
  ];

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  console.log("Projects state:", projects);

  return <Table columns={columns} dataSource={projects} rowKey="id" />;
};

export default ProjectTable;
