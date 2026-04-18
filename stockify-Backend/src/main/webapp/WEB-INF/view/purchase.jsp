<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Purchase</title>
    <!-- 1. jQuery FIRST -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- 2. DataTables JS -->
    <script src="https://cdn.datatables.net/2.3.7/js/dataTables.min.js"></script>

    <!-- 3. Bootstrap JS (optional, after jQuery) -->
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.7/css/dataTables.bootstrap5.min.css">
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.bootstrap5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.print.min.js"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/css/formValidation.min.css"/>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/formValidation.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/framework/bootstrap.min.js"></script>

    <!-- JSZip for Excel -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>

    <!-- PDFMake for PDF -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>

    <!-- Buttons JS -->
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.print.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<style>
    .table {
        margin-top: 10px;
        margin-left: 5px;
    }

    /* ===== MODAL BOX ===== */
    .modal-content {
        background: #fff;
        width: 420px;
        border-radius: 8px;
        padding: 20px 22px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.25);
        position: relative;
        animation: fadeInScale 0.25s ease;
    }

    /* ===== CLOSE BUTTON ===== */
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

    /* FIX invisible dropdown options */
    .form-select,
    .form-select option {
        color: #000;
        background-color: #fff;

    }

    #purchaseModal .form-select,
    #purchaseModal .form-select option {
        color: #000;
        background-color: #fff;
    }

    /* Button styling */

    .dt-button.btn-success {
        background-color: #198754;
        color: #fff;
        border: none;
    }

    .dt-button.btn-danger {
        background-color: #dc3545;
        color: #fff;
        border: none;
    }

    .dt-button.btn-secondary {
        background-color: #6c757d;
        color: #fff;
        border: none;
    }

    /* Space between "Show entries" and buttons */
    .dataTables_wrapper .dataTables_length {
        margin-right: 15px;
        display: inline-block;
        vertical-align: middle;
    }

    .dataTables_wrapper .dt-buttons {
        display: inline-block;
        vertical-align: middle;
    }

</style>
<div>
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
                            <h4 class="mb-0">Purchase List</h4>
                            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#purchaseModal"
                                    style="display:inline-flex; flex-direction:column; align-items:center; gap:2px;">
                                <span><i class="fa fa-plus"></i> Add Purchase</span>
                            </button>
                        </div>
                        <table id="purchase" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Quantity</th>
                                <th>Total Amount</th>
                                <th>Purchase Date</th>
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


<%--        ADD PURCHASE--%>
<div id="purchaseModal" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">


            <div class="modal-header">
                <h4 class="modal-title">Add Purchase</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>


            <form action="${pageContext.request.contextPath}/purchase/save" method="post" id="purchaseForm"
                  class="modal-form">

                <label>Product</label>
                <select id="product" name="productId" class="form-select">
                    <option value="">--Select Product--</option>
                    <%--                    <c:forEach items="${product}" var="p">--%>
                    <%--                        <option value="${p.productId}" data-price="${p.price}" data-name="${p.name}"> ${p.name}</option>--%>
                    <%--                    </c:forEach>--%>
                </select>

                <div class="form-group">
                    <label>Price</label>
                    <input id="price" name="price" type="number" placeholder="" readonly>
                </div>

                <div class="form-group">
                    <label>Quantity</label>
                    <input id="quantity" name="quantity" type="number" placeholder="Enter stock quantity" required>
                </div>

                <div class="form-group">
                    <label>Total</label>
                    <input type="number" name="total" id="total" readonly>

                </div>
                <%--                onclick="savePurchase()--%>

                <div class="modal-footer">
                    <button type="submit" class="btn primary">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#purchase').DataTable({
            ajax: {
                url: '/purchase/allpurchase',
                type: 'GET',
                dataSrc: function (json) {
                    return json.data || [];
                }
            },
            columns: [
                {data: "purchaseId"},
                {data: "product.productName"},
                {data: "quantity"},
                {data: "total"},
                {data: "createdOn"}
            ],

            columnDefs: [{
                targets: 0,
                orderable: false,
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                }
            }],
            dom: 'lBfrtip',
            buttons: [
                {
                    extend: 'excelHtml5',
                    text: '<i class="fa fa-file-excel-o"></i> Excel',
                    className: 'btn btn-success btn-sm'
                },
                {
                    extend: 'pdfHtml5',
                    text: '<i class="fa fa-file-pdf-o"></i> PDF',
                    className: 'btn btn-danger btn-sm'
                },
                {
                    extend: 'print',
                    text: '<i class="fa fa-file-print-o"></i> Print',
                    className: 'btn btn-secondary btn-sm'
                }
            ]
        });
    });

    /*Open product Modal*/
    function openAddModal() {
        $("#purchaseModal").modal("show");
    }

    /*Save Product*/
    function savePurchase() {
        $("#purchaseForm").submit();
    }

    //Get Products
    $('#purchaseModal').on('shown.bs.modal', function () {

        $.ajax({
            url: '${pageContext.request.contextPath}/purchase/get-products',
            type: 'GET',
            success: function (products) {

                console.log(products); // verify data

                let $product = $('#product');
                $product.empty();
                $product.append('<option value="">--Select Product--</option>');

                $.each(products, function (i, p) {
                    $product.append(
                        '<option value="' + p.productId + '" ' +
                        'data-price="' + p.productPrice + '" ' +
                        'data-name="' + p.productName + '">' +
                        p.productName +
                        '</option>'
                    );
                });
            },
            error: function (xhr) {
                console.error(xhr.status, xhr.responseText);
                alert('Failed to load products');
            }
        });

    });

    // When product is selected
    $('#product').on('change', function () {
        let selected = $(this).find(':selected');

        let price = selected.data('price') || 0;

        $('#price').val(price);
        calculateTotal();
    });

    // When quantity changes
    $('#quantity').on('input', function () {
        calculateTotal();
    });

    function calculateTotal() {
        let price = parseFloat($('#price').val()) || 0;
        let quantity = parseInt($('#quantity').val()) || 0;

        $('#total').val(price * quantity);
    }

    /*Form reset*/
    $('#purchaseModal').on('show.bs.modal', function () {
        $('#purchaseForm')[0].reset();
        $('#price').val('');
        $('#total').val('');
    });

    //Form validation
    $('#purchaseForm')
        .formValidation({
            framework: 'bootstrap',
            excluded: ':disabled',
            fields: {

                quantity: {
                    validators: {
                        notEmpty: {
                            message: 'Quantity is required'
                        },
                        greaterThan: {
                            value: 1,
                            message: 'Atleast 1'
                        }
                    }
                },


            }
        })
        .on('success.form.fv', function (e) {

            e.preventDefault();

            $.ajax({
                url: '/purchase/save',
                type: 'POST',
                data: $('#purchaseForm').serialize(),
                success: function () {
                    toastr.success("Purchase created successfully!");
                    $('#purchaseModal').modal('hide');

                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();

                    $('#purchase').DataTable().ajax.reload(null, false);

                    $('#purchaseForm').formValidation('resetForm', true);
                },
                error: function () {
                    toastr.error("Failed to save product");
                }
            });
        });
</script>
</body>
</html>
