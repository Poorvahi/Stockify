import { useEffect, useState } from "react";
import DataTable from "../../components/Datatable/Datatable";
import Modal from "../../components/Modal/Modal";
import PurchaseForm from "../../components/Form/Purchase/PurchaseForm";
import { getPurchase, downloadPurchaseReport } from "../../services/ApiService";
import { toast } from "react-toastify";
import { Bar, Line, Pie } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend } from "chart.js";
import "./PurchasePage.css";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, ArcElement, Tooltip, Legend);

export default function PurchasePage() {
  const [purchase, setPurchase] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [analytics, setAnalytics] = useState({
    purchaseDateTrend: {},
    productPurchaseQty: {},
    productPurchaseRev: {},
    totalExpense: 0,
    totalQtyPurchased: 0
  });

  const fetchProducts = async () => {
    try {
      const res = await getPurchase();
      const allPurchases = res.data.data || [];
      setPurchase(allPurchases);

      // Compute analytics
      const trend = {};
      const qtyDict = {};
      const revDict = {};
      let totExp = 0;
      let totQty = 0;

      allPurchases.forEach(p => {
        const date = p.createdOn ? p.createdOn.split('T')[0] : 'Unknown';
        const pName = p.product?.productName || 'Unknown';
        const total = parseFloat(p.total) || 0;
        const qty = parseInt(p.quantity) || 0;

        trend[date] = (trend[date] || 0) + total;
        qtyDict[pName] = (qtyDict[pName] || 0) + qty;
        revDict[pName] = (revDict[pName] || 0) + total;

        totExp += total;
        totQty += qty;
      });

      // Sort trend by date
      const sortedTrend = Object.keys(trend).sort().reduce((acc, key) => {
        acc[key] = trend[key];
        return acc;
      }, {});

      setAnalytics({
        purchaseDateTrend: sortedTrend,
        productPurchaseQty: qtyDict,
        productPurchaseRev: revDict,
        totalExpense: totExp,
        totalQtyPurchased: totQty
      });

    } catch (err) {
      console.error(err);
      toast.error(err?.response?.data?.message || err?.message || "Error fetching purchases");
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
              const blob = await downloadPurchaseReport(row.purchaseId);
              const url = window.URL.createObjectURL(new Blob([blob], { type: "application/pdf" }));
              const link = document.createElement("a");
              link.href = url;
              link.download = `purchase_${row.purchaseId}.pdf`;
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
    <div className="purchase-page-container">
      <div className="purchase-header">
        <h1 className="purchase-title">Purchase Overview</h1>
        <button className="creative-add-btn purchase-btn" onClick={handleAddClick}>
          + Add Purchase
        </button>
      </div>

      <div className="kpi-grid">
        <div className="kpi-card glass-emerald">
          <h3>Total Expenses</h3>
          <p>₹{analytics.totalExpense.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</p>
        </div>
        <div className="kpi-card glass-amber">
          <h3>Units Purchased</h3>
          <p>{analytics.totalQtyPurchased.toLocaleString()}</p>
        </div>
        <div className="kpi-card glass-teal">
          <h3>Total Transactions</h3>
          <p>{purchase.length}</p>
        </div>
      </div>

      <div className="purchase-chart-grid">
        <div className="chart-wrapper line-chart">
          <h4>Expense Trend (Over Time)</h4>
          <div className="chart-container">
            <Line 
              data={{ 
                labels: Object.keys(analytics.purchaseDateTrend), 
                datasets: [{ 
                  label: "Expense (₹)", 
                  data: Object.values(analytics.purchaseDateTrend), 
                  borderColor: "#10b981", 
                  backgroundColor: "rgba(16, 185, 129, 0.15)",
                  fill: true,
                  tension: 0.4,
                  pointBackgroundColor: "#10b981",
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
          <h4>Expenses by Product</h4>
          <div className="chart-container">
            <Pie 
              data={{ 
                labels: Object.keys(analytics.productPurchaseRev), 
                datasets: [{ 
                  data: Object.values(analytics.productPurchaseRev), 
                  backgroundColor: ["#10b981", "#f59e0b", "#14b8a6", "#3b82f6", "#8b5cf6", "#ec4899", "#ef4444", "#6366f1"],
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
          <h4>Quantity Purchased by Product</h4>
          <div className="chart-container">
            <Bar 
              data={{ 
                labels: Object.keys(analytics.productPurchaseQty), 
                datasets: [{ 
                  label: "Units Purchased", 
                  data: Object.values(analytics.productPurchaseQty), 
                  backgroundColor: "rgba(20, 184, 166, 0.85)",
                  hoverBackgroundColor: "rgba(20, 184, 166, 1)",
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

      <div className="purchase-table-wrapper">
        <DataTable columns={columns} data={purchase} />
      </div>

      <Modal isOpen={showModal} onClose={handleModalClose} title="Add purchase">
        <PurchaseForm onSuccess={handleModalSuccess} />
      </Modal>
    </div>
  );
}
