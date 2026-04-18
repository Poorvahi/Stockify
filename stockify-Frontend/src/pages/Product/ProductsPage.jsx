import { useEffect, useState, useRef } from "react";
import { Link } from "react-router-dom";
import { createPortal } from "react-dom";
import DataTable from "../../components/Datatable/Datatable";
import {
  getProduct,
  getProductById,
  deleteProduct,
  uploadExcel,
  downloadExcel,
  downloadCsv,
  downloadBarcode,
} from "../../services/ApiService";
import Modal from "../../components/Modal/Modal";
import ProductModalForm from "../../components/Form/Product/ProductModalForm";
import "./Product.css";
import { toast } from "react-toastify";
import Swal from "sweetalert2";
import CommentBox from "../../components/CommentBox/CommentBox";
import Button from "../../components/Button/Button";

export default function ProductsPage({ role }) {
  const [products, setProducts] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editProduct, setEditProduct] = useState(null);
  const [showCommentModal, setShowCommentModal] = useState(false);
  const [commentProduct, setCommentProduct] = useState(null);
  const [viewProduct, setViewProduct] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [downloading, setDownloading] = useState(false);
  const [downloadingCsv, setDownloadingCsv] = useState(false);
  const fileInputRef = useRef(null);

  const isAdmin = role && (role.toUpperCase() === "ROLE_ADMIN" || role.toUpperCase() === "ADMIN");
  console.log("ProductsPage: role is", role, "isAdmin:", isAdmin);

  const fetchProducts = async () => {
    try {
      const res = await getProduct();
      const payload = res.data;
      const list = Array.isArray(payload?.data) ? payload.data : [];
      if (payload?.success === false) {
        toast.error(payload?.message || "Could not load products");
        setProducts([]);
        return;
      }
      setProducts(list);
    } catch (err) {
      console.error(err);
      toast.error(
        err?.response?.data?.message || err?.message || "Error fetching products"
      );
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleModalSuccess = () => {
    setEditProduct(null);
    setShowModal(false);
    fetchProducts();
  };

  const handleModalClose = () => {
    setEditProduct(null);
    setShowModal(false);
  };

  const handleEditClick = (product) => {
    setEditProduct(product);
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
      const response = await deleteProduct(id);
      const body = response.data;
      if (body?.success === false) {
        toast.error(body?.message || "Delete failed");
        return;
      }
      toast.success(body?.message || "Product deleted successfully");
      fetchProducts();
    } catch (err) {
      toast.error(
        err?.response?.data?.message || err?.message || "Error deleting product"
      );
    }
  };

  const handleUploadClick = () => {
    if (fileInputRef.current) fileInputRef.current.click();
  };

  const handleFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setUploading(true);
    try {
      const res = await uploadExcel(file);
      if (res.data?.status === true) {
        toast.success("Products uploaded successfully");
        await fetchProducts();
      } else {
        const errs = res.data?.data;
        toast.error(
          Array.isArray(errs) && errs.length ? errs.join("; ") : "Upload failed",
        );
      }
    } catch (err) {
      toast.error(
        err?.response?.data?.message || err?.message || "Failed to upload file"
      );
    } finally {
      setUploading(false);
      e.target.value = "";
    }
  };

  const handleDownloadClick = async () => {
    setDownloading(true);
    try {
      const res = await downloadExcel();
      const blob = new Blob([res.data], {
        type:
          res.headers["content-type"] ||
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      const disposition = res.headers["content-disposition"];
      let filename = "products.xlsx";
      if (disposition && disposition.includes("filename=")) {
        filename = disposition.split("filename=")[1].replace(/"/g, "").trim();
      }
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      toast.error(
        err?.response?.data?.message || err?.message || "Failed to download file"
      );
    } finally {
      setDownloading(false);
    }
  };

  const handleDownloadCsv = async () => {
    setDownloadingCsv(true);
    try {
      const res = await downloadCsv();
      const blob = new Blob([res.data], {
        type: res.headers["content-type"] || "text/csv;charset=utf-8",
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "products.csv";
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || "Failed to download CSV");
    } finally {
      setDownloadingCsv(false);
    }
  };

  const columns = [
    { header: "Sr.No.", accessor: (_, index) => index + 1 },
    { 
      header: "Name", 
      accessor: (row) => (
        <Link to={`/product/${row.productId}`} className="text-blue-600 hover:text-blue-800 font-semibold hover:underline">
          {row.productName}
        </Link>
      )
    },
    { header: "Description", accessor: (row) => row.productDescription },
    { header: "Price", accessor: (row) => row.productPrice },
    { header: "Quantity", accessor: (row) => row.quantity },
    {
      header: "Actions",
      accessor: (row) => (
        <div className="flex items-center gap-2">
          <Button
            variant="comment"
            onClick={() => {
              setCommentProduct(row);
              setShowCommentModal(true);
            }}
          >
            💬
          </Button>

          {/* Barcode — available to all authenticated users */}
          <Button
            variant="barcode"
            onClick={() => downloadBarcode(row.productId, row.productName)}
          >
            Download Barcode
          </Button>

          {/* Admin-only action buttons */}
          {isAdmin && (
            <>
              <Button
                variant="comment"
                title="View details"
                onClick={async () => {
                  try {
                    const res = await getProductById(row.productId);
                    const body = res.data;
                    if (body?.success && body?.data) {
                      setViewProduct(body.data);
                    } else {
                      toast.error(body?.message || "Could not load product");
                    }
                  } catch (e) {
                    toast.error(e?.response?.data?.message || "Could not load product");
                  }
                }}
              >
                👁️
              </Button>
              <Button variant="edit" onClick={() => handleEditClick(row)}>
                ✏️
              </Button>
              <Button variant="delete" onClick={() => handleDelete(row.productId)}>
                🗑️
              </Button>
            </>
          )}
        </div>
      ),
    },
  ];

  return (
    <>
      <h1>Products List</h1>

      {/* Admin-only top action buttons */}
      {isAdmin && (
        <div className="flex flex-wrap items-center gap-3 mb-6">
          <Button variant="add" onClick={() => setShowModal(true)}>
            <span className="mr-2">➕</span> Add Product
          </Button>
          <Button variant="upload" onClick={handleUploadClick} disabled={uploading}>
            <span className="mr-2">📁</span> {uploading ? "Uploading..." : "Upload Excel"}
          </Button>
          <Button
            variant="download"
            onClick={handleDownloadClick}
            disabled={downloading}
          >
            <span className="mr-2">📥</span> {downloading ? "Downloading..." : "Download Excel"}
          </Button>
          <Button
            variant="download"
            onClick={handleDownloadCsv}
            disabled={downloadingCsv}
          >
             <span className="mr-2">📊</span> {downloadingCsv ? "Downloading..." : "Download CSV"}
          </Button>
          <input
            type="file"
            accept=".xls,.xlsx"
            ref={fileInputRef}
            style={{ display: "none" }}
            onChange={handleFileChange}
          />
        </div>
      )}

      <DataTable columns={columns} data={products} />

      {createPortal(
        <Modal
          isOpen={showModal}
          onClose={handleModalClose}
          title={editProduct ? "Edit Product" : "Add Product"}
        >
          <ProductModalForm
            onSuccess={handleModalSuccess}
            editProduct={editProduct}
          />
        </Modal>,
        document.body
      )}

      {createPortal(
        <Modal
          isOpen={!!viewProduct}
          onClose={() => setViewProduct(null)}
          title="Product details"
        >
          {viewProduct && (
            <div style={{ lineHeight: 1.6 }}>
              <p><strong>ID:</strong> {viewProduct.productId}</p>
              <p><strong>Name:</strong> {viewProduct.productName}</p>
              <p><strong>Description:</strong> {viewProduct.productDescription}</p>
              <p><strong>Price:</strong> {viewProduct.productPrice}</p>
              <p><strong>Quantity:</strong> {viewProduct.quantity}</p>
            </div>
          )}
        </Modal>,
        document.body
      )}

      {createPortal(
        <Modal
          isOpen={showCommentModal}
          onClose={() => {
            setShowCommentModal(false);
            setCommentProduct(null);
          }}
          title="Comments"
        >
          {commentProduct && (
            <CommentBox
              moduleType="PRODUCT"
              moduleId={commentProduct.productId}
            />
          )}
        </Modal>,
        document.body
      )}
    </>
  );
}
