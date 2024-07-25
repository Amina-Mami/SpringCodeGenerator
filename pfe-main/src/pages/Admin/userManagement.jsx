import { useState, useEffect } from "react";
import UserTable from "../../components/Admin/UserTable";
import axios from "axios";
import { Container, Typography, Paper } from "@mui/material";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./ProjectManagement.css";

const UserManagement = () => {
  const [users, setUsers] = useState([]);

  const fetchUsers = () => {
    axios
      .get("http://localhost:7070/user/getAllUsers")
      .then((response) => {
        console.log("users fetched:", response.data);
        if (Array.isArray(response.data)) {
          setUsers(response.data);
        } else {
          setUsers([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching users:", error);
      });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleDeleteUser = async (userId) => {
    try {
      const response = await axios.delete(
        `http://localhost:7070/user/${userId}`
      );

      if (response.status === 200) {
        toast.success("User successfully deleted!");
        fetchUsers(); 
      } else {
       
        toast.error("Unexpected response status: " + response.status);
        console.error("Failed to delete user. Status:", response.status);
      }
    } catch (error) {
      
      toast.error(
        "Error deleting user: " + (error.response?.data || error.message)
      );
      console.error("Error deleting user:", error);
    }
  };

  return (
    <Container className="user-management">
      <Typography variant="h3" component="h1" gutterBottom>
        Users Management
      </Typography>
      <Paper elevation={3} style={{ padding: "20px" }}>
        <UserTable
          users={users}
          fetchUsers={fetchUsers}
          onDeleteUser={handleDeleteUser}
        />
      </Paper>
    </Container>
  );
};

export default UserManagement;
