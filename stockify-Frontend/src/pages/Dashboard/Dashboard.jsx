import { useState, useEffect } from "react";
import { getDashboardAnalytics } from "../../services/ApiService";
import { Bar, Line, Pie } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend } from "chart.js";
import "./Dashboard.css";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend);

export default function Dashboard({ totalProducts, totalStock, totalValue }) {
  const [stats, setStats] = useState({
    totalProducts: 0,
    lowStockCount: 0,
    totalSales: 0,
    totalPurchases: 0,
  });
  const [analytics, setAnalytics] = useState({
    salesOverTime: {},
    categoryDistribution: {},
    stockStatus: {},
  });

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await getDashboardAnalytics();
        const data = res.data.data || {};
        setStats({
          totalProducts: data.totalProducts || 0,
          lowStockCount: data.lowStockItems || 0,
          totalSales: data.totalSales || 0,
          totalPurchases: data.totalPurchases || 0,
        });
        setAnalytics({
          salesOverTime: data.salesOverTime || {},
          categoryDistribution: data.categoryDistribution || {},
          stockStatus: data.stockStatus || {},
        });
      } catch (err) {
        console.error("Error fetching products:", err);
      }
    };

    fetchProducts();
  }, []);

  return (
    <div className="dashboard">
      <h3 className="dashboard-title">INVENTORY STATS</h3>
      <div className="cards-contain">
        <div className="row">
          <div className="col-md-3">
            <div className="cards cards-teal">
              <div className="cards-info">
                <h3>TOTAL PRODUCTS</h3>
                <p>{stats.totalProducts}</p>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="cards cards-purple">
              <div className="cards-info">
                <h3>TOTAL SALES</h3>
                <p>{stats.totalSales}</p>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="cards cards-blue">
              <div className="cards-info">
                <h3>TOTAL PURCHASES</h3>
                <p>{stats.totalPurchases}</p>
              </div>
            </div>
          </div>

          <div className="col-md-3">
            <div className="cards cards-red">
              <div className="cards-info">
                <h3>LOW STOCK</h3>
                <p>{stats.lowStockCount}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="chart-grid">
        <div className="chart-card">
          <h4>Sales Over Time</h4>
          <div style={{ position: "relative", flexGrow: 1, height: "100%", width: "100%" }}>
            <Line options={{ maintainAspectRatio: false }} data={{ labels: Object.keys(analytics.salesOverTime), datasets: [{ label: "Sales", data: Object.values(analytics.salesOverTime), borderColor: "#1d4ed8", backgroundColor: "rgba(29,78,216,0.2)" }] }} />
          </div>
        </div>
        <div className="chart-card">
          <h4>Category Distribution</h4>
          <div style={{ position: "relative", flexGrow: 1, height: "100%", width: "100%" }}>
            <Bar options={{ maintainAspectRatio: false }} data={{ labels: Object.keys(analytics.categoryDistribution), datasets: [{ label: "Products", data: Object.values(analytics.categoryDistribution), backgroundColor: "#7c3aed" }] }} />
          </div>
        </div>
        <div className="chart-card">
          <h4>Stock Status</h4>
          <div style={{ position: "relative", flexGrow: 1, height: "100%", width: "100%" }}>
            <Pie options={{ maintainAspectRatio: false }} data={{ labels: ["Available", "Low", "Out"], datasets: [{ data: [analytics.stockStatus.available || 0, analytics.stockStatus.low || 0, analytics.stockStatus.out || 0], backgroundColor: ["#16a34a", "#f59e0b", "#dc2626"] }] }} />
          </div>
        </div>
      </div>
    </div>
  );
}
