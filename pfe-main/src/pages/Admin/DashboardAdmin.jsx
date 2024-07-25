import { useState, useEffect } from "react";
import StatCard from "../../components/Admin/StatCard";
import { Bar } from "react-chartjs-2";
import axios from "axios";
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  Title,
  Tooltip,
  Legend,
  LinearScale,
} from "chart.js";
import "./DashboardAdmin.css";
ChartJS.register(
  BarElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
);

const DashboardAdmin = () => {
  const [stats, setStats] = useState({
    userCount: 0,
    projectCount: 0,
    projectsPerDay: [],
    optionsUsage: {},
  });

  useEffect(() => {
    axios
      .get("http://localhost:7070/user/stats")
      .then((response) => {
        setStats(response.data);
      })
      .catch((error) => {
        console.error("Error fetching statistics:", error);
      });
  }, []);

  return (
    <div className="admin-dashboard">
      <h1>Statistics</h1>
      <div className="stats-overview">
        <StatCard title="Total of Users" value={stats.userCount} />
        <StatCard title="Total of Projects" value={stats.projectCount} />
      </div>
      <div className="charts">
        {stats.projectsPerDay && stats.projectsPerDay.length > 0 ? (
          <Bar
            data={{
              labels: stats.projectsPerDay.map((item) => item.date),
              datasets: [
                {
                  label: "Project Per Day",
                  data: stats.projectsPerDay.map((item) => item.count),
                  backgroundColor: "rgba(54, 162, 235, 0.2)",
                  borderColor: "rgba(54, 162, 235, 1)",
                  borderWidth: 1,
                },
              ],
            }}
            options={{
              scales: {
                y: {
                  beginAtZero: true,
                },
              },
            }}
          />
        ) : (
          <p>No data available for projects per day.</p>
        )}
      </div>
    </div>
  );
};

export default DashboardAdmin;
