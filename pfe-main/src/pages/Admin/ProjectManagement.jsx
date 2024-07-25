
import { useState, useEffect } from "react";
import axios from "axios";
import ProjectTable from "../../components/Admin/ProjectTable";
import { Container, Typography, Paper } from "@mui/material";
import "./ProjectManagement.css";

const ProjectManagement = () => {
  const [projects, setProjects] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:7070/project/projects")
      .then((response) => {
        console.log("Projects fetched:", response.data);
        if (Array.isArray(response.data)) {
          setProjects(response.data);
        } else {
          setProjects([]); 
        }
      })
      .catch((error) => {
        console.error("Error fetching projects:", error);
      });
  }, []);

  const handleDeleteProject = (projectId) => {
    axios
      .delete(`http://localhost:7070/project/delete/${projectId}`)
      .then((response) => {
        console.log("Project deleted successfully:", projectId);
        setProjects((prevProjects) =>
          prevProjects.filter((project) => project.id !== projectId)
        );
      })
      .catch((error) => {
        console.error("Error deleting project:", error);
      });
  };

  return (
    <Container className="project-management">
      <Typography variant="h3" component="h1" gutterBottom>
        Projects Management
      </Typography>
      <Paper elevation={3} style={{ padding: "20px" }}>
        <ProjectTable
          projects={projects}
          onDeleteProject={handleDeleteProject}
        />
      </Paper>
    </Container>
  );
};

export default ProjectManagement;
