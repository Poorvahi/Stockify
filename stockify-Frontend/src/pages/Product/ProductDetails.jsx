import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProductById, getPurchase, getSales, getStockMovements, getWarehouseStockOverview } from "../../services/ApiService";
import DataTable from "../../components/Datatable/Datatable";
import { toast } from "react-toastify";

export default function ProductDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const productId = Number(id);

  const [product, setProduct] = useState(null);
  const [purchases, setPurchases] = useState([]);
  const [sales, setSales] = useState([]);
  const [movements, setMovements] = useState([]);
  const [stock, setStock] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        // 1. Fetch Basic Product Info
        const prodRes = await getProductById(productId);
        if (prodRes.data?.success && prodRes.data?.data) {
          setProduct(prodRes.data.data);
        } else {
          toast.error("Could not load product details");
          navigate("/products");
          return;
        }

        // 2. Fetch all Purchases and filter
        try {
          const purchRes = await getPurchase();
          const allPurchases = purchRes.data?.data || [];
          setPurchases(allPurchases.filter(p => p.product?.productId === productId));
        } catch(e) { console.warn("Failed to load purchases", e); }

        // 3. Fetch all Sales and filter
        try {
          const salesRes = await getSales();
          const allSales = salesRes.data?.data || [];
          setSales(allSales.filter(s => s.product?.productId === productId));
        } catch(e) { console.warn("Failed to load sales", e); }

        // 4. Fetch Stock Movements and Overview
        try {
          const moveRes = await getStockMovements();
          const allMovements = moveRes.data?.data || [];
          setMovements(allMovements.filter(m => m.product?.productId === productId));

          const stockRes = await getWarehouseStockOverview();
          const allStock = stockRes.data?.data || [];
          // Handle native query field mapping differences flexibly
          setStock(allStock.filter(s => {
             const stockProdName = s.productName || s.PRODUCTNAME || s.product_name || s.productname;
             return stockProdName === prodRes.data.data.productName;
          }));
        } catch(e) { console.warn("Failed to load stock data", e); }

      } catch (err) {
        console.error(err);
        toast.error("Error loading product details");
      } finally {
        setLoading(false);
      }
    };

    fetchAllData();
  }, [productId, navigate]);

  if (loading) return <div className="p-10 text-center">Loading Details...</div>;
  if (!product) return null;

  // Calculate some basic statistics
  const totalPurchased = purchases.reduce((acc, curr) => acc + (curr.quantity || 0), 0);
  const totalSold = sales.reduce((acc, curr) => acc + (curr.quantity || 0), 0);
  const totalRevenue = sales.reduce((acc, curr) => acc + (curr.total || 0), 0);

  return (
    <div className="max-w-7xl mx-auto py-6">
      <button 
        onClick={() => navigate("/products")}
        className="mb-6 flex items-center text-indigo-600 hover:text-indigo-800 font-semibold"
      >
        &larr; Back to Products
      </button>

      {/* Main Product Stat Card */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-8 flex flex-col md:flex-row gap-6 items-start md:items-center justify-between">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 mb-2">{product.productName}</h1>
          <p className="text-gray-500 text-lg">{product.productDescription}</p>
        </div>
        <div className="flex gap-4">
          <div className="bg-blue-50 p-4 rounded-lg min-w-[120px] text-center">
            <p className="text-sm text-blue-600 font-bold mb-1">PRICE</p>
            <p className="text-2xl font-black text-blue-900">${product.productPrice}</p>
          </div>
          <div className="bg-green-50 p-4 rounded-lg min-w-[120px] text-center">
            <p className="text-sm text-green-600 font-bold mb-1">IN STOCK</p>
            <p className="text-2xl font-black text-green-900">{product.quantity}</p>
          </div>
        </div>
      </div>

      {/* Statistics Row */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-gray-500 text-sm font-semibold uppercase mb-2">Total Units Purchased</h3>
          <p className="text-3xl font-bold text-gray-800">{totalPurchased}</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-gray-500 text-sm font-semibold uppercase mb-2">Total Units Sold</h3>
          <p className="text-3xl font-bold text-gray-800">{totalSold}</p>
        </div>
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 className="text-gray-500 text-sm font-semibold uppercase mb-2">Total Revenue Generated</h3>
          <p className="text-3xl font-bold text-gray-800">${totalRevenue.toFixed(2)}</p>
        </div>
      </div>

      {/* Tables Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Sales History */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4 border-b pb-2">Sales History</h2>
          <DataTable 
            columns={[
              { header: "Date", accessor: (r) => new Date(r.createdOn).toLocaleDateString() },
              { header: "Qty", accessor: (r) => r.quantity },
              { header: "Price", accessor: (r) => `$${r.price}` },
              { header: "Total", accessor: (r) => `$${r.total}` },
            ]}
            data={sales}
          />
        </div>

        {/* Purchase History */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4 border-b pb-2">Purchase History</h2>
          <DataTable 
            columns={[
              { header: "Date", accessor: (r) => new Date(r.createdOn).toLocaleDateString() },
              { header: "Qty", accessor: (r) => r.quantity },
              { header: "Price", accessor: (r) => `$${r.price}` },
              { header: "Total", accessor: (r) => `$${r.total}` },
            ]}
            data={purchases}
          />
        </div>

        {/* Stock Info Per Warehouse */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4 border-b pb-2">Stock per Warehouse</h2>
          <DataTable 
            columns={[
              { header: "Warehouse", accessor: (r) => r.warehouseName || r.WAREHOUSENAME || r.warehouse_name || r.warehousename },
              { header: "Quantity", accessor: (r) => r.quantity || r.QUANTITY || r.product_quantity || r.productquantity },
            ]}
            data={stock}
          />
        </div>

        {/* Movements */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4 border-b pb-2">Recent Movements</h2>
          <DataTable 
            columns={[
              { header: "Date", accessor: (r) => new Date(r.createdOn).toLocaleDateString() },
              { header: "Type", accessor: (r) => r.movementType },
              { header: "Qty", accessor: (r) => r.quantity },
              { header: "Warehouse", accessor: (r) => r.warehouse?.warehouseName },
            ]}
            data={movements}
          />
        </div>
      </div>
    </div>
  );
}
