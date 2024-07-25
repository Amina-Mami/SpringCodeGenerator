
import { useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  styled,
  IconButton,
  TextField,
  Typography,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { DeleteOutlined } from "@ant-design/icons";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";

const PAGE_SIZE = 10;

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  backgroundColor: "#4c7aaf",
  color: "#fff",
  fontWeight: "bold",
}));

const ProjectTable = ({ projects = [], onDeleteProject }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [deleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [projectToDelete, setProjectToDelete] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");

  const filteredProjects = projects.filter((project) =>
    project.projectName.toLowerCase().includes(searchQuery.toLowerCase())
  );
  const totalPages = Math.ceil(filteredProjects.length / PAGE_SIZE);

  const startIndex = (currentPage - 1) * PAGE_SIZE;
  const endIndex = startIndex + PAGE_SIZE;

  const displayedProjects = filteredProjects.slice(startIndex, endIndex);

  const handleOpenDeleteConfirmation = (projectId) => {
    setProjectToDelete(projectId);
    setDeleteConfirmationOpen(true);
  };

  const handleCloseDeleteConfirmation = () => {
    setDeleteConfirmationOpen(false);
  };

  const handleDeleteProject = () => {
    onDeleteProject(projectToDelete);
    handleCloseDeleteConfirmation();
  };

  const handlePageChange = (pageNumber) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
    }
  };

  const handleSearchChange = (event) => {
    setSearchQuery(event.target.value);
    setCurrentPage(1);
  };

  return (
    <div>
      <TextField
        label="Search Projects"
        variant="outlined"
        size="small"
        sx={{ width: "150px" }}
        value={searchQuery}
        onChange={handleSearchChange}
        margin="normal"
      />

      {filteredProjects.length === 0 ? (
        <Typography variant="h6" align="center" margin="normal">
          No data available
        </Typography>
      ) : (
        <>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <StyledTableCell>Project</StyledTableCell>
                  <StyledTableCell>User</StyledTableCell>
                  <StyledTableCell>Date</StyledTableCell>
                  <StyledTableCell>Actions</StyledTableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {displayedProjects.map((project) => (
                  <TableRow key={project?.id}>
                    <TableCell>{project.projectName}</TableCell>
                    <TableCell>{project?.user?.username || "N/A"}</TableCell>
                    <TableCell>{project?.date || "N/A"}</TableCell>
                    <TableCell>
                      <Button
                        variant="contained"
                        color="error"
                        startIcon={<DeleteIcon />}
                        onClick={() => handleOpenDeleteConfirmation(project.id)}
                      >
                        Delete
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>

          <div className="pagination">
            <IconButton
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
              size="small"
            >
              <ArrowBackIcon fontSize="small" />
            </IconButton>
            {Array.from({ length: totalPages }, (_, index) => (
              <Button
                key={index + 1}
                variant="outlined"
                size="small"
                color={currentPage === index + 1 ? "primary" : "primary"}
                onClick={() => handlePageChange(index + 1)}
              >
                {index + 1}
              </Button>
            ))}
            <IconButton
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
              size="small"
            >
              <ArrowForwardIcon fontSize="small" />
            </IconButton>
          </div>
        </>
      )}

      <Dialog
        open={deleteConfirmationOpen}
        onClose={handleCloseDeleteConfirmation}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Delete Project?"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to delete this project?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDeleteConfirmation} color="primary">
            Cancel
          </Button>
          <Button
            onClick={handleDeleteProject}
            color="error"
            startIcon={<DeleteOutlined />}
          >
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default ProjectTable;
