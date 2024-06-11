// ApiService.js
import axios from "axios";

const BASE_URL = "http://localhost:7070";

const ApiService = {
  getEntities: async () => {
    const response = await axios.get(`${BASE_URL}/api/entities`);
    return response.data;
  },

  createEntity: async (entityData) => {
    const response = await axios.post(`${BASE_URL}/api/entities`, entityData);
    return response.data;
  },

  updateEntity: async (entityId, entityData) => {
    const response = await axios.put(
      `${BASE_URL}/api/entities/${entityId}`,
      entityData
    );
    return response.data;
  },

  deleteEntity: async (entityId) => {
    const response = await axios.delete(`${BASE_URL}/api/entities/${entityId}`);
    return response.data;
  },
};

export default ApiService;
