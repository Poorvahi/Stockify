import { useEffect, useState } from "react";
import DataTable from "../../components/Datatable/Datatable";
import Modal from "../../components/Modal/Modal";
import SalesForm from "../../components/Form/Sales/SalesForm";
import { getSales, downloadSalesReport } from "../../services/ApiService";
import { toast } from "react-toastify";
import { Bar, Line, Pie } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend } from "chart.js";
import "./SalesPage.css";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend);

export default function SalesPage() {
  const [sales, setSales] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [analytics, setAnalytics] = useState({
    salesDateTrend: {},
    productSalesQty: {},
    productSalesRev: {},
    totalRevenue: 0,
    totalQtySold: 0
  });

  const fetchProducts = async () => {
    try {
      const res = await getSales();
      const allSales = res.data.data || [];
      setSales(allSales);

      // Compute analytics
      const trend = {};
      const qtyDict = {};
      const revDict = {};
      let totRev = 0;
      let totQty = 0;

      allSales.forEach(s => {
        const date = s.createdOn ? s.createdOn.split('T')[0] : 'Unknown';
        const pName = s.product?.productName || 'Unknown';
        const total = parseFloat(s.total) || 0;
        const qty = parseInt(s.quantity) || 0;

        trend[date] = (trend[date] || 0) + total;
        qtyDict[pName] = (qtyDict[pName] || 0) + qty;
        revDict[pName] = (revDict[pName] || 0) + total;

        totRev += total;
        totQty += qty;
      });

      // Sort trend by date
      const sortedTrend = Object.keys(trend).sort().reduce((acc, key) => {
        acc[key] = trend[key];
        return acc;
      }, {});

      setAnalytics({
        salesDateTrend: sortedTrend,
        productSalesQty: qtyDict,
        productSalesRev: revDict,
        totalRevenue: totRev,
        totalQtySold: totQty
      });

    } catch (err) {
      console.error(err);
      toast.error(err?.response?.data?.message || err?.message || "Error fetching sales");
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleModalSuccess = () => {
    setShowModal(false);
    fetchProducts();
  };

  const handleModalClose = () => {
    setShowModal(false);
  };

  const handleAddClick = () => {
    setShowModal(true);
  };

  const columns = [
    {
      header: "Sr.No.",
      accessor: (_, index) => index + 1,
    },
    {
      header: "Name",
      accessor: (row) => row.product?.productName,
    },
    {
      header: "Quantity",
      accessor: (row) => row.quantity,
    },
    {
      header: "Total Price",
      accessor: (row) => row.total,
    },
    {
      header: "Purchased On",
      accessor: (row) => row.createdOn,
    },
    {
      header: "Report",
      accessor: (row) => (
        <button
          className="btn-edit"
          onClick={async () => {
            try {
              const blob = await downloadSalesReport(row.salesId);
              const url = window.URL.createObjectURL(new Blob([blob], { type: "application/pdf" }));
              const link = document.createElement("a");
              link.href = url;
              link.download = `sales_${row.salesId}.pdf`;
              document.body.appendChild(link);
              link.click();
              link.remove();
              window.URL.revokeObjectURL(url);
            } catch (e) {
              toast.error(e?.message || "Failed to download report");
            }
          }}
        >
          Download Report (PDF)
        </button>
      ),
    },
  ];

  return (
    <div className="sales-page-container">
      <div className="sales-header">
        <h1 className="sales-title">Sales Overview</h1>
        <button className="creative-add-btn" onClick={handleAddClick}>
          + Add Sales
        </button>
      </div>

      <div className="kpi-grid">
        <div className="kpi-card glass-blue">
          <h3>Total Revenue</h3>
          <p>₹{analytics.totalRevenue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</p>
        </div>
        <div className="kpi-card glass-purple">
          <h3>Units Sold</h3>
          <p>{analytics.totalQtySold.toLocaleString()}</p>
        </div>
        <div className="kpi-card glass-pink">
          <h3>Total Transactions</h3>
          <p>{sales.length}</p>
        </div>
      </div>

      <div className="sales-chart-grid">
        <div className="chart-wrapper line-chart">
          <h4>Revenue Trend (Over Time)</h4>
          <div className="chart-container">
            <Line 
              data={{ 
                labels: Object.keys(analytics.salesDateTrend), 
                datasets: [{ 
                  label: "Revenue (₹)", 
                  data: Object.values(analytics.salesDateTrend), 
                  borderColor: "#3b82f6", 
                  backgroundColor: "rgba(59, 130, 246, 0.15)",
                  fill: true,
                  tension: 0.4,
                  pointBackgroundColor: "#3b82f6",
                  pointRadius: 4,
                  pointHoverRadius: 6
                }] 
              }}
              options={{ 
                responsive: true, 
                maintainAspectRatio: false,
                plugins: {
                  legend: { display: false }
                },
                scales: {
                  y: { beginAtZero: true, grid: { borderDash: [5, 5] } },
                  x: { grid: { display: false } }
                }
              }}
            />
          </div>
        </div>
        
        <div className="chart-wrapper pie-chart">
          <h4>Revenue by Product</h4>
          <div className="chart-container">
            <Pie 
              data={{ 
                labels: Object.keys(analytics.productSalesRev), 
                datasets: [{ 
                  data: Object.values(analytics.productSalesRev), 
                  backgroundColor: ["#3b82f6", "#ec4899", "#8b5cf6", "#14b8a6", "#f59e0b", "#10b981", "#ef4444", "#6366f1"],
                  borderWidth: 0,
                  hoverOffset: 4
                }] 
              }}
              options={{ 
                responsive: true, 
                maintainAspectRatio: false,
                plugins: {
                  legend: { position: 'bottom', labels: { usePointStyle: true, padding: 20 } }
                }
              }} 
            />
          </div>
        </div>

        <div className="chart-wrapper bar-chart">
          <h4>Quantity Sold by Product</h4>
          <div className="chart-container">
            <Bar 
              data={{ 
                labels: Object.keys(analytics.productSalesQty), 
                datasets: [{ 
                  label: "Units Sold", 
                  data: Object.values(analytics.productSalesQty), 
                  backgroundColor: "rgba(168, 85, 247, 0.85)",
                  hoverBackgroundColor: "rgba(168, 85, 247, 1)",
                  borderRadius: 6,
                  barThickness: 'flex',
                  maxBarThickness: 50
                }] 
              }}
              options={{ 
                responsive: true, 
                maintainAspectRatio: false,
                plugins: {
                  legend: { display: false }
                },
                scales: {
                  y: { beginAtZero: true, grid: { borderDash: [5, 5] } },
                  x: { grid: { display: false } }
                }
              }}
            />
          </div>
        </div>
      </div>

      <div className="sales-table-wrapper">
        <DataTable columns={columns} data={sales} />
      </div>

      <Modal isOpen={showModal} onClose={handleModalClose} title="Add Sales">
        <SalesForm onSuccess={handleModalSuccess} />
      </Modal>
    </div>
  );
}
