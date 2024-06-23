const { exec } = require("child_process");
const fs = require("fs");
const path = require("path");

function createReactApp(projectName) {
  return new Promise((resolve, reject) => {
    exec(`npx create-react-app ${projectName}`, (error, stdout, stderr) => {
      if (error) {
        reject(`Error creating React app: ${error}`);
      } else {
        resolve(stdout);
      }
    });
  });
}

function installDependencies(projectPath) {
  return new Promise((resolve, reject) => {
    exec(
      `npm install axios react-router-dom`,
      { cwd: projectPath },
      (error, stdout, stderr) => {
        if (error) {
          reject(`Error installing dependencies: ${error}`);
        } else {
          resolve(stdout);
        }
      }
    );
  });
}

function createApiService(projectPath) {
  const apiServiceContent = `
    import axios from 'axios';

    const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

    export const getUsers = async () => {
        try {
            const response = await axios.get(\`\${API_URL}/users\`);
            return response.data;
        } catch (error) {
            console.error('Error fetching users:', error);
            throw error;
        }
    };
    `;

  return new Promise((resolve, reject) => {
    const filePath = path.join(projectPath, "src", "services", "api.js");
    fs.writeFile(filePath, apiServiceContent, (error) => {
      if (error) {
        reject(`Error creating API service: ${error}`);
      } else {
        resolve("API service created successfully");
      }
    });
  });
}

async function generateReactProject(projectName) {
  try {
    console.log("Creating React app...");
    await createReactApp(projectName);
    console.log("React app created successfully.");

    const projectPath = path.join(process.cwd(), projectName);
    console.log("Installing dependencies...");
    await installDependencies(projectPath);
    console.log("Dependencies installed successfully.");

    console.log("Creating API service...");
    await createApiService(projectPath);
    console.log("API service created successfully.");
  } catch (error) {
    console.error(error);
  }
}

// Call the function with your project name
generateReactProject(process.argv[2]);
