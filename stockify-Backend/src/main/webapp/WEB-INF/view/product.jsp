<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Products</title>

    <!-- 1. jQuery FIRST -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <%--    for the swal--%>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


    <!-- 2. DataTables JS -->
    <script src="https://cdn.datatables.net/2.3.7/js/dataTables.min.js"></script>

    <!-- 3. Bootstrap JS (optional, after jQuery) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.7/css/dataTables.dataTables.min.css">

    <!-- 4. Form Validation (CSS)  -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/css/formValidation.min.css"/>

    <!-- 5. Toastr  -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

    <!-- 6. Form Validation (JS)  -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/formValidation.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/framework/bootstrap.min.js"></script>
    <script src="/js/apiService.js"></script>

</head>
<style>

    .table {
        margin-top: 10px;
        margin-left: 5px;
    }

    .modal {
        /*display: none;*/
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1050;
        align-items: center;
        justify-content: center;
    }

    .modal-content {
        background: #fff;
        width: 420px;
        border-radius: 8px;
        padding: 20px 22px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
        position: relative;
        animation: fadeInScale 0.25s ease;
    }

    .close-btn {
        position: absolute;
        top: 10px;
        right: 12px;
        font-size: 22px;
        cursor: pointer;
        color: #666;
    }

    .close-btn:hover {
        color: #000;
    }

    /* ===== FORM ===== */
    .modal-form {
        margin-top: 15px;
    }

    .form-group {
        margin-bottom: 12px;
    }

    .form-group label {
        font-weight: 500;
        font-size: 14px;
        margin-bottom: 4px;
        display: block;
    }

    .form-group input {
        width: 100%;
        padding: 8px 10px;
        border-radius: 4px;
        border: 1px solid #ccc;
        outline: none;
    }

    .form-group input:focus {
        border-color: #0d6efd;
    }

    /*===== ACTION BUTTONS =====*/
    .modal-actions {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 15px;
    }

    .btn {
        padding: 6px 14px;
        border-radius: 4px;
        border: none;
        cursor: pointer;
        font-size: 14px;
    }

    .btn.primary {
        background: #0d6efd;
        color: #fff;
    }

    .btn.danger {
        background: #dc3545;
        color: #fff;
    }

    .btn:hover {
        opacity: 0.9;
    }

    #toast-container .toast-success {
        background-color: #51a351;
        color: #fff;
    }

    #toast-container .toast-error {
        background-color: #bd362f;
        color: #fff;
    }

    #toast-container .toast-warning {
        background-color: #f89406;
        color: #fff;
    }

    #toast-container .toast-info {
        background-color: #2f96b4;
        color: #fff;
    }

    /*!* ===== ANIMATION ===== *!*/
    @keyframes fadeInScale {
        from {
            opacity: 0;
            transform: scale(0.9);
        }
        to {
            opacity: 1;
            transform: scale(1);
        }
    }

    /* ===== Editable Table Styling ===== */
    #editableProducts {
        width: 100%;
        border-collapse: collapse;
        margin-top: 15px;
        font-family: Arial, sans-serif;
        font-size: 14px;
    }

    #editableProducts th,
    #editableProducts td {
        border: 1px solid #dee2e6;
        padding: 8px 12px;
        text-align: left;
        vertical-align: middle;
    }

    #editableProducts th {
        background-color: #f8f9fa;
        font-weight: 600;
    }

    #editableProducts tbody tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    #editableProducts tbody tr:hover {
        background-color: #e9ecef;
    }

    #editableProducts input {
        width: 100%;
        padding: 6px 8px;
        font-size: 14px;
        outline: none;
    }

    #editableProducts input:focus {
        border-color: #0d6efd;
        box-shadow: 0 0 3px rgba(13, 110, 253, 0.25);
    }

    /* Optional: Responsive scroll for large tables in modal */
    .modal-body {
        max-height: 60vh;
        overflow-y: auto;
    }

</style>
<body>
<div class="container-fluid p-0">
    <div class="row min-vh-100 g-0 m-0">

        <div class="col d-flex flex-column">
            <!-- HEADER -->
            <div>
                <jsp:include page="header.jsp"/>
            </div>
        </div>
        <div class="row">
            <!-- SIDEBAR -->
            <div class="col-md-2 bg-dark text-white p-0">
                <jsp:include page="sidebar.jsp"/>
            </div>
            <div class="col-md-10">
                <!-- PAGE CONTENT -->
                <div class="flex-grow-1 p-4">
                    <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
                        <h4 class="mb-0">Product List</h4>
                        <div>
                            <button class="btn btn-warning me-2" onclick="openUploadModal()">
                                <i class="fa fa-upload"></i> Upload Excel
                            </button>

                            <button class="btn btn-success me-2" onclick="downloadProductsExcel()">
                                <i class="fa fa-download"></i> Download Excel
                            </button>

                            <button class="btn btn-primary"
                                    data-bs-toggle="modal"
                                    data-bs-target="#productModal">
                                + Add Product
                            </button>
                        </div>
                    </div>
                    <table id="prod" class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <footer class="bg-dark text-light py-2 text-center">
        <small>© 2026 | Inventory Management System</small>
    </footer>
</div>
</div>


<%--        ADD PRODUCT--%>
<div id="productModal" class="modal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <div class="modal-header">
                <h4 class="modal-title">Add Product</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <form action="${pageContext.request.contextPath}/product/save" method="post" id="productForm"
                  class="modal-form">

                <div class="form-group">
                    <label>Product Name</label>
                    <input id="name" name="productName" type="text" placeholder="Enter product name">
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <input id="description" name="productDescription" type="text" placeholder="Enter Description">
                </div>

                <div class="form-group">
                    <label>Price</label>
                    <input id="price" name="productPrice" type="number" placeholder="Enter price">
                </div>

                <div class="form-group">
                    <label>Stock</label>
                    <input id="quantity" name="quantity" type="number" placeholder="Enter stock quantity">
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn primary" id="saveonClick">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%--        EDIT PRODUCT--%>
<div id="editProductModal" class="modal">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">

            <div class="modal-header">
                <h4 class="modal-title">Edit Product Details</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <form action="${pageContext.request.contextPath}/product/save" method="POST" id="editProductForm"
                  class="modal-form">

                <div class="form-group">
                    <label>Product ID</label>
                    <input type="hidden" id="editId" name="productId" type="number" placeholder="Enter id">
                </div>

                <div class="form-group">
                    <label>Product Name</label>
                    <input id="editName" name="productName" type="text" placeholder="Enter product name">
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <input id="editDescription" name="productDescription" type="text" placeholder="Enter Description">
                </div>

                <div class="form-group">
                    <label>Price</label>
                    <input id="editPrice" name="productPrice" type="number" placeholder="Enter price">
                </div>

                <div class="form-group">
                    <label>Stock</label>
                    <input id="editQuantity" name="quantity" type="number" placeholder="Enter stock quantity">
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%--        UPLOAD EXCEL--%>
<!-- ✅ add fade class so Bootstrap modal works properly -->
<div id="uploadModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Upload Product Excel</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="file" id="excelfile" accept=".xlsx" class="form-control">
                <div style="margin-top:10px; font-size:14px;">
                    <span>Click here to </span>
                    <a href="#" onclick="downloadProductsExcel()"
                       style="color:#0d6efd; font-weight:600; text-decoration:underline;">
                        Download All Products
                    </a>
                    <span> Excel File</span>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-success" onclick="uploadExcel()">Upload</button>
            </div>
        </div>
    </div>
</div>

<%--JavaScript--%>
<script>
    $(document).ready(function () {
        $('#prod').DataTable({
            ajax: {
                url: '/product/all',
                type: 'GET',
                dataSrc: 'data'
            },
            columns: [
                {data: "productId"},    //col 0
                {data: "productName"},         //col 1
                {data: "productDescription"},  //col 2
                {data: "productPrice"},        //col 3
                {data: "quantity"},     //col 4
                {data: "productId"}     //col 5
            ],

            columnDefs: [{
                targets: 0,
                orderable: false,
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                }
            },
                {
                    targets: 5,
                    orderable: false,
                    render: function (data, type, row, meta) {

                        return ''
                            + '<button class="btn btn-sm btn-warning me-1" onclick="editProduct(' + row.productId + ')">'
                            + '    <i class="fa fa-pencil"></i>'
                            + '</button> '
                            + '<button class="btn btn-sm btn-info me-1" onclick="downloadBarcode(' + row.productId + ')">'
                            + '    <i class="fa fa-barcode"></i>'
                            + '</button> '
                            + '<button class="btn btn-sm btn-danger me-1" onclick="deleteProduct(' + row.productId + ')"">'
                            + '    <i class="fa fa-trash"></i>'
                            + '</button>';

                    }
                }]
        });
    });

    // PRODUCT MODAL RESET
    $('#productModal').on('hidden.bs.modal', function () {

        // Reset form
        $('#productForm')[0].reset();

        // Reset validation
        $('#productForm').formValidation('resetForm', true);
    });

    /*Open product Modal*/
    function openAddModal() {
        $("#productModal").modal("show");
    }

    function openEditModal() {
        $("#editProductModal").modal("show");
    }

    /*Save Product*/
    // $("#saveonClick").on("click",function (){
    //     $("#productForm").submit();
    // });

    /*Edit Product*///
    $('#editProductForm').off('submit').on('submit', function (e) {
        e.preventDefault();

        $('#editProductForm').on('submit', function (e) {
            e.preventDefault();

            $.ajax({
                url: '/product/save',
                type: 'POST',
                data: $('#editProductForm').serialize(),

                success: function (response) {

                    if (response.success) {
                        toastr.success(response.message);

                        $('#editProductModal').modal('hide');
                        // $('body').removeClass('modal-open');
                        // $('.modal-backdrop').remove();

                        $('#prod').DataTable().ajax.reload(null, false);

                    } else {
                        toastr.error(response.message);
                    }
                },

                error: function () {
                    toastr.error("Server error while updating");
                }
            });
        });
    });


    function editProduct(id) {
        $.ajax({
            url: '/product/id/' + id,
            type: 'GET',
            success: function (response) {

                const product = response.data;

                $('#editId').val(product.productId);
                $('#editName').val(product.productName);
                $('#editDescription').val(product.productDescription);
                $('#editPrice').val(product.productPrice);
                $('#editQuantity').val(product.quantity);

                $("#editProductModal").modal("show");
            }
        });
    }

    // EDIT PRODUCT MODAL RESET
    $('#editProductModal').on('hidden.bs.modal', function () {

        $('#editProductForm')[0].reset();

        // If you add validation later for edit form
        if ($('#editProductForm').data('formValidation')) {
            $('#editProductForm').formValidation('resetForm', true);
        }
    });

    /*Save Edited Product*/
    function saveEditedProduct() {
        $("#editProductForm").submit();
    }

    /*Delete Product*/
    function deleteProduct(id) {
        Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Yes, delete it!"
        }).then(function (e) {
            if (e.isConfirmed) {
                $.ajax({
                    url: '/product/delete/' + id,
                    type: "POST",
                    success: function (response) {

                        if (response.success) {
                            toastr.success(response.message || "Product deleted successfully!");
                            $('#prod').DataTable().ajax.reload(null, false);
                        } else {
                            toastr.error(response.message || "Product failed to Delete");
                        }

                    },
                    error: function () {
                        toastr.error("Server error while deleting");
                    }
                });
            }
        });
    }

    /*Form validation*/
    $(document).ready(function () {
        // FORM VALIDATION + AJAX
        $('#productForm').formValidation({
            framework: 'bootstrap',
            excluded: ':disabled',
            fields: {

                productName: {
                    validators: {
                        notEmpty: {
                            message: 'Product name is required'
                        }
                    }
                },
                productPrice: {
                    validators: {
                        notEmpty: {
                            message: 'Price is required'
                        },
                        numeric: {
                            message: 'Price must be a number'
                        },
                        greaterThan: {
                            value: 0,
                            message: 'Price must be greater than 0'
                        }
                    }
                },

                quantity: {
                    validators: {
                        notEmpty: {
                            message: 'Stock is required'
                        },
                        integer: {
                            message: 'Stock must be a number'
                        },
                        greaterThan: {
                            value: 0,
                            message: 'Stock must be greater than 0'
                        }
                    }
                }
            }
        })
            .on('success.form.fv', function (e) {
                e.preventDefault();

                $.ajax({
                    url: '/product/save',
                    type: 'POST',
                    data: $('#productForm').serialize(),
                    success: function (response) {

                        if (response.success) {
                            toastr.success(response.message);

                            $('#productModal').modal('hide');
                            $('body').removeClass('modal-open');
                            $('.modal-backdrop').remove();

                            $('#prod').DataTable().ajax.reload(null, false);
                            $('#productForm')[0].reset();
                        } else {
                            toastr.error(response.message);
                        }
                    }
                });
            });

    });

    /* Download Excel*/
    function downloadProductsExcel() {
        fetch('product/download', {method: 'GET'})
            .then(response => {
                if (!response.ok) throw new Error('Unable to download');
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                document.body.appendChild(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url)
            })
            .catch(error => {
                console.error("Error downloading excel:" + error);
                Swal.fire('Error', 'Failed to download excel file', 'error')
            });
    }

    /*Open Upload product Modal*/
    function openUploadModal() {
        $("#uploadModal").modal("show");
    }

    /*Upload Excel*/
    function uploadExcel() {
        let fileInput = document.getElementById('excelfile');
        let file = fileInput.files[0];

        if (!file) {
            Swal.fire('ERROR', 'Please select a file', 'error');
            return;
        }

        let formatData = new FormData();
        formatData.append("file", file);

        fetch('/product/upload', {method: 'POST', body: formatData})
            .then(res => res.json())
            .then(data => {
                if (!data.status) {
                    let errorHtml = "<ul style='text-align:left'>";
                    data.data.forEach(error => {
                        errorHtml += "<li>" + error + "</li>";
                    });
                    errorHtml += "</ul>";

                    Swal.fire({
                        title: 'Validation Errors',
                        html: errorHtml,
                        icon: 'error',
                    });
                    return;
                }

                Swal.fire('Success', 'File uploaded successfully', 'success');

                $('#uploadModal').modal('hide');
                $('body').removeClass('modal-open');
                $('.modal-backdrop').remove();

                document.getElementById('excelfile').value = '';

                $('#prod').DataTable().ajax.reload(null, false);
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'Upload failed', 'error');
            });
    }

    /* Download Product Barcode PDF */
    function downloadBarcode(productId) {
        window.ApiService.downloadProductBarcode(productId)
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = 'barcode_' + productId + '.pdf';
                document.body.appendChild(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url);
            })
            .catch(error => {
                console.error('Error downloading barcode:', error);
                Swal.fire('Error', 'Failed to download barcode', 'error');
            });
    }
</script>
</body>
</html>
