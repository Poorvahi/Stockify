import { useEffect, useState } from "react";
import { createPortal } from "react-dom";
import DataTable from "../../components/Datatable/Datatable";
import { getWarehouse, deleteWarehouse, getWarehouseStockOverview, getStockMovements } from "../../services/ApiService";  
import Modal from "../../components/Modal/Modal";
import WarehouseModalForm from "../../components/Form/Warehouse/WarehouseForm";
import { toast } from "react-toastify";
import Swal from "sweetalert2";

export default function WarehousePage() {
  const [warehouse, setWarehouse] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editWarehouse, setEditWarehouse] = useState(null); 
  const [stockOverview, setStockOverview] = useState([]);
  const [movements, setMovements] = useState([]);
  const fetchWarehouse = async () => {
    try {
      const res = await getWarehouse();
      setWarehouse(res.data.data || []);
      const overviewRes = await getWarehouseStockOverview();
      setStockOverview(overviewRes.data.data || []);
      const moveRes = await getStockMovements();
      setMovements(moveRes.data.data || []);
    } catch (err) {
      console.error(err);
      toast.error(
        err?.response?.data?.message ||
          err?.message ||
          "Error fetching warehouse data",
      );
    }
  };

  useEffect(() => {
    fetchWarehouse();
  }, []);

  const handleModalSuccess = () => {
    setEditWarehouse(null);
    setShowModal(false);
    fetchWarehouse();
  };

  const handleModalClose = () => {
    setEditWarehouse(null);
    setShowModal(false);
  };

  const handleEditClick = (warehouse) => {
    setEditWarehouse(warehouse);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    });

    if (!result.isConfirmed) return;

    try {
      const response = await deleteWarehouse(id);
      toast.success(response.data.message || "Warehouse deleted successfully");
      fetchWarehouse();
    } catch (err) {
      toast.error(
        err?.response?.data?.message ||
          err?.message ||
          "Error deleting warehouse",
      );
    }
  };

  const columns = [
    { header: "Sr.No.", accessor: (_, index) => index + 1 },
    { header: "Name", accessor: (row) => row.warehouseName },
    { header: "Code", accessor: (row) => row.warehouseCode },
    {
      header: "Actions",
      accessor: (row) => (
        <>
          <button className="btn-edit" onClick={() => handleEditClick(row)}>
            Edit
          </button>
          <button
            className="btn-delete"
            onClick={() => handleDelete(row.warehouseId)}
          >
            Delete
          </button>
        </>
      ),
    },
  ];

  return (
    <>
      <h1>Warehouse List</h1>

      <button className="btn-add-warehouse" onClick={() => setShowModal(true)} style={{backgroundColor: "blue", color: "white", padding: "10px 12px", width:"15%", border:"2px", borderRadius:"9px" }} >
        Add Warehouse
      </button>

      <DataTable columns={columns} data={warehouse} />
      
      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 my-8">
        <h2 className="text-xl font-bold mb-4 text-gray-800">Product Quantity Per Warehouse</h2>
        <DataTable
          columns={[
            { header: "Warehouse", accessor: (r) => r.warehouseName || r.WAREHOUSENAME || r.warehouse_name || r.warehousename },
            { header: "Product", accessor: (r) => r.productName || r.PRODUCTNAME || r.product_name || r.productname },
            { header: "Quantity", accessor: (r) => r.quantity || r.QUANTITY || r.product_quantity || r.productquantity },
          ]}
          data={stockOverview}
        />
      </div>

      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 my-8">
        <h2 className="text-xl font-bold mb-4 text-gray-800">Recent Stock Movements</h2>
        <DataTable
          columns={[
            { header: "Type", accessor: (r) => r.movementType },
            { header: "Product", accessor: (r) => r.product?.productName },
            { header: "Warehouse", accessor: (r) => r.warehouse?.warehouseName },
            { header: "Qty", accessor: (r) => r.quantity },
            { header: "Ref", accessor: (r) => `${r.referenceType} #${r.referenceId}` },
          ]}
          data={movements}
        />
      </div>

      {createPortal(
        <Modal
          isOpen={showModal}
          onClose={handleModalClose}
          title={editWarehouse ? "Edit Warehouse" : "Add Warehouse"}
        >
          <WarehouseModalForm
            onSuccess={handleModalSuccess}
            editWarehouse={editWarehouse}
          />
        </Modal>,
        document.body,
      )}
    </>
  );
}
