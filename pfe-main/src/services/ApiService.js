import axios from "axios";

const API_URL = "http://localhost:7070/project";

export const getEntitiesByProjectId = async (projectId) => {
  try {
    const response = await axios.get(`${API_URL}/${projectId}/entities`);
    return response.data;
  } catch (error) {
    console.error("Error fetching entities:", error);
    throw error;
  }
};
