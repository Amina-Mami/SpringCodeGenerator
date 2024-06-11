// import React, { useEffect, useState } from "react";
// import { Table, Button, Space, Popconfirm } from "antd";
// import {
//   DownloadOutlined,
//   EditOutlined,
//   DeleteOutlined,
// } from "@ant-design/icons";

// const ProjectTable = () => {
//   const [projects, setProjects] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   useEffect(() => {
//     fetchProjects();
//   }, []);

//   const fetchProjects = async () => {
//     try {
//       const response = await fetch("http://localhost:7070/project/projects");

//       if (!response.ok) {
//         throw new Error("Failed to fetch projects");
//       }
//       const data = await response.json();
//       setProjects(data);
//       setLoading(false);
//     } catch (error) {
//       setError(error.message);
//       setLoading(false);
//     }
//   };

//   const columns = [
//     {
//       title: "Project Name",
//       dataIndex: "projectName",
//       key: "projectName",
//     },
//     {
//       title: "Path",
//       dataIndex: "filePath",
//       key: "path",
//     },
//     {
//       title: "Action",
//       key: "action",
//       render: (_, record) => (
//         <Space size="middle">
//           <Button
//             type="primary"
//             icon={<EditOutlined />}
//             onClick={() => {
//               /* Handle update logic */
//             }}
//           >
//             Update
//           </Button>
//           <Popconfirm
//             title="Are you sure you want to delete this project?"
//             onConfirm={() => {
//               /* Handle delete logic */
//             }}
//             okText="Yes"
//             cancelText="No"
//           >
//             <Button type="primary" danger icon={<DeleteOutlined />} ghost>
//               Delete
//             </Button>
//           </Popconfirm>
//           <Button
//             href={record.downloadUrl}
//             target="_blank"
//             download
//             icon={<DownloadOutlined />}
//           >
//             Download
//           </Button>
//         </Space>
//       ),
//     },
//   ];

//   if (loading) {
//     return <div>Loading...</div>;
//   }

//   if (error) {
//     return <div>Error: {error}</div>;
//   }

//   return <Table columns={columns} dataSource={projects} />;
// };

// export default ProjectTable;

import React, { useEffect, useState } from "react";
import { Table, Button, Space, Popconfirm } from "antd";
import {
  DownloadOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";

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
  const handleDownload = (projectId) => {
    const url = `http://localhost:7070/project/download/${projectId}`;
    fetch(url)
      .then((response) => response.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.style.display = "none";
        a.href = url;
        a.download = "project.zip";
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      })
      .catch((error) => console.error("Error downloading project:", error));
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
      // If successful, fetch projects again to update the table
      fetchProjects();
    } catch (error) {
      setError(error.message);
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
            onClick={() => handleDownload(record.id, false)}
          >
            Download
          </Button>
          {/* <Button
            type="primary"
            icon={<DownloadOutlined />}
            onClick={() => handleDownload(record.id, true)}
          >
            Download JSON
          </Button> */}
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
