import axios from "axios";

const API_URL = axios.create({
  baseURL: "http://localhost:8080",
});

// Automatically attach JWT to all requests
API_URL.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log("Token attached to request:", token.substring(0, 20) + "...");
  } else {
    console.log("No token found in localStorage");
  }
  return config;
});

// Handle response errors
API_URL.interceptors.response.use(
  (response) => response,
  (error) => {
    // Only 401 means the session/token is invalid. 403 is "not allowed" — keep user logged in.
    if (error.response?.status === 401) {
      console.log("Unauthorized - Invalid or expired token");
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
      if (window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  },
);

//Logout
export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("username");
  localStorage.removeItem("role");
  window.location.href = "/login";
};

// GET PRODUCTS
export const getProduct = () => API_URL.get("/product/all");

export const getProductById = (id) => API_URL.get(`/product/id/${id}`);

// ADD PRODUCTS
export const createProduct = (data) => API_URL.post("/product/save", data);

// UPDATE PRODUCTS
export const updateProduct = (id, data) =>
  API_URL.post("/product/save", { ...data, productId: id });

// DELETE PRODUCTS
export const deleteProduct = (id) => API_URL.post(`/product/delete/${id}`);

// PRODUCT EXCEL UPLOAD
export const uploadExcel = (file) => {
  const formData = new FormData();
  formData.append("file", file);

  return API_URL.post("/product/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

// PRODUCT EXCEL DOWNLOAD
export const downloadExcel = () =>
  API_URL.get("/product/download", {
    responseType: "blob",
  });

// PRODUCT CSV DOWNLOAD
export const downloadCsv = () =>
  API_URL.get("/product/download/csv", {
    responseType: "blob",
  });

/** Ensures response is a PDF blob; throws a clear Error if the server returned JSON/HTML */
export const getPdfOrThrow = async (axiosResponse) => {
  const ct = (axiosResponse.headers["content-type"] || "").toLowerCase();
  if (ct.includes("application/pdf") || ct.includes("application/octet-stream")) {
    return axiosResponse.data;
  }
  const text = await axiosResponse.data.text();
  try {
    const j = JSON.parse(text);
    throw new Error(j.message || "Download failed");
  } catch (err) {
    if (err instanceof SyntaxError) {
      throw new Error(text.slice(0, 200) || "Server did not return a PDF");
    }
    throw err;
  }
};

// GET PURCHASE
export const getPurchase = () => API_URL.get("/purchase/allpurchase");

// ADD PURCHASE
export const createPurchase = (data) => API_URL.post("/purchase/save", data);

// GET SALES
export const getSales = () => API_URL.get("/sales/allsales");

// ADD SALES
export const createSales = (data) => API_URL.post("/sales/save", data);

// GET WAREHOUSE
export const getWarehouse = () => API_URL.get("/warehouse/all");

// ADD WAREHOUSE
export const createWarehouse = (data) => API_URL.post("/warehouse/save", data);

// UPDATE WAREHOUSE
export const updateWarehouse = (id, data) =>
  API_URL.post("/warehouse/save", { ...data, warehouseId: id });

// DELETE WAREHOUSE
export const deleteWarehouse = (id) => API_URL.post(`/warehouse/delete/${id}`);

// AUTH
export const loginUser = (data) => API_URL.post("/auth/login", data);
export const registerUser = (data) => API_URL.post("/auth/register", data);
export const forgotPassword = (data) =>
  API_URL.post("/auth/forgot-password", data);
export const verifyOtp = (data) => API_URL.post("/auth/verify-otp", data);
export const resetPassword = (data) => API_URL.post("/auth/reset-password", data);
export const getDashboardAnalytics = () => API_URL.get("/dashboard/analytics");

//GET COMMENTS
export const getComments = () => API_URL.get("/comments");

// ADD COMMENT
export const createComment = (data) => API_URL.post("/comment/save", data);

// GET NOTIFICATIONS
export const getNotifications = (user) =>
  API_URL.get(`/api/notifications/${user}`);

// CLEAR NOTIFICATIONS
export const clearNotifications = (user) =>
  API_URL.delete(`/api/notifications/${user}`);
export const getWarehouseStockOverview = () => API_URL.get("/warehouse/stock-overview");
export const getStockMovements = () => API_URL.get("/warehouse/movements");

//Jasper Barcode Download 
export const downloadBarcode = async (productId, productName) => {
  try {
    const res = await API_URL.get(`/product/barcode/${productId}`, {
      responseType: "blob",
    });

    const blob = new Blob([res.data], { type: "application/pdf" });
    const url = window.URL.createObjectURL(blob);

    // Get filename from backend 
    const contentDisposition = res.headers["content-disposition"];
    let fileName = `barcode_${productName || productId}.pdf`;

    if (contentDisposition) {
      const match = contentDisposition.match(/filename="?(.+)"?/);
      if (match?.[1]) fileName = match[1];
    }

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();

    window.URL.revokeObjectURL(url);
  } catch (err) {
    console.error("Error downloading barcode:", err);
  }
};

export const downloadSalesReport = async (salesId) => {
  const res = await API_URL.get(`/report/sales/${salesId}`, { responseType: "blob" });
  return getPdfOrThrow(res);
};

export const downloadPurchaseReport = async (purchaseId) => {
  const res = await API_URL.get(`/report/purchase/${purchaseId}`, { responseType: "blob" });
  return getPdfOrThrow(res);
};

export default API_URL;
