import "../../pages/Admin/DashboardAdmin.css";
const StatCard = ({ title, value }) => (
  <div className="stat-card">
    <h2>{title}</h2>
    <p>{value}</p>
  </div>
);

export default StatCard;
