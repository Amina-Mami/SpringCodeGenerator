import { useEffect, useState } from "react";
import { Table, Button, Space, Popconfirm, message } from "antd";
import {
  DownloadOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import axios from "axios";

const ProjectTable = () => {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const response = await fetch("http://localhost:7070/project/projects");
      if (!response.ok) {
        throw new Error("Failed to fetch projects");
      }
      const data = await response.json();
      setProjects(data);
      setLoading(false);
    } catch (error) {
      setError(error.message);
      setLoading(false);
    }
  };

  const handleDelete = async (projectId) => {
    try {
      const response = await fetch(
        `http://localhost:7070/project/delete/${projectId}`,
        {
          method: "DELETE",
        }
      );
      if (!response.ok) {
        throw new Error("Failed to delete project");
      }

      fetchProjects();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleDownload = async (projectId, projectName) => {
    try {
      const response = await axios.get(
        `http://localhost:7070/project/download/${projectId}`,
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
      link.parentNode.removeChild(link);
    } catch (error) {
      console.error("Error downloading project:", error);
      message.error("Failed to download project. Please try again later.");
    }
  };

  const columns = [
    {
      title: "Project Name",
      dataIndex: "projectName",
      key: "projectName",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="middle">
          <Button
            type="primary"
            style={{ backgroundColor: "green", borderColor: "green" }}
            icon={<EditOutlined />}
            onClick={() => {}}
          >
            Update
          </Button>
          <Popconfirm
            title="Are you sure you want to delete this project?"
            onConfirm={() => handleDelete(record.id)}
            okText="Yes"
            cancelText="No"
          >
            <Button type="primary" danger icon={<DeleteOutlined />}>
              Delete
            </Button>
          </Popconfirm>
          <Button
            type="primary"
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

  return <Table columns={columns} dataSource={projects} rowKey="id" />;
};

export default ProjectTable;
