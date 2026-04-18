<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sales</title>
    <!-- CSS -->
    <!-- 1. jQuery FIRST -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

    <!-- 2. DataTables JS -->
    <script src="https://cdn.datatables.net/2.3.7/js/dataTables.min.js"></script>

    <!-- 3. Bootstrap JS (optional, after jQuery) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.7/css/dataTables.dataTables.min.css">
    <script src="https://cdn.datatables.net/2.3.7/js/dataTables.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/css/formValidation.min.css"/>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/formValidation.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/formvalidation/0.6.2-dev/js/framework/bootstrap.min.js"></script>
    <!-- Buttons CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/3.1.2/css/buttons.dataTables.min.css">

    <!-- JSZip for Excel -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>

    <!-- PDFMake for PDF -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>

    <!-- Buttons JS -->
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/3.1.2/js/buttons.print.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<style>

    .table {
        margin-top: 10px;
        margin-left: 5px;
    }

    /* ===== MODAL BACKDROP ===== */
    .modal {
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

    /* Button styling */

    .btn-success {
        background-color: #198754 !important;
        color: #fff !important;
    }

    .btn-danger {
        background-color: #dc3545 !important;
        color: #fff !important;
    }

    .btn-secondary {
        background-color: #6c757d !important;
        color: #fff !important;
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

<body>
<div class="container-fluid p-0">
    <%--    <div class="row min-vh-100 g-0 m-0">--%>

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
                    <h4 class="mb-0">Sales List</h4>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#salesModal"
                            style="display:inline-flex; flex-direction:column; align-items:center; gap:2px;">
                        <span><i class="fa fa-plus"></i> Add Sales</span>
                    </button>
                </div>

                <table id="sales" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Quantity</th>
                        <th>Total Amount</th>
                        <th>Sales Date</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <%--        </div>--%>
        <footer class="bg-dark text-light py-2 text-center">
            <small>© 2026 | Inventory Management System</small>
        </footer>
    </div>

    <%--        ADD Sales--%>
    <div id="salesModal" class="modal fade" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">


                <div class="modal-header">
                    <h4 class="modal-title">Add Sales</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>


                <form action="${pageContext.request.contextPath}/sales/save" method="post" id="salesForm"
                      class="modal-form">
                    <label>Product</label>
                    <select id="product" name="productId" class="form-select">
                        <option value="">--Select Product--</option>
                        <%--                    <c:forEach items="${product}" var="p">--%>
                        <%--                        <option value="${p.productid}" data-price="${p.price}" data-name="${p.name}"> ${p.name}</option>--%>
                        <%--                    </c:forEach>--%>
                    </select>

                    <div class="form-group">
                        <label>Price</label>
                        <input id="price" name="price" type="number" placeholder="" readonly>
                    </div>

                    <div class="form-group">
                        <label>Quantity</label>
                        <input id="quantity" name="quantity" type="number" placeholder="Enter stock quantity">
                    </div>

                    <div class="form-group">
                        <label>Total</label>
                        <input type="number" name="total" id="total" readonly>

                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn primary">Save</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            $('#sales').DataTable({
                ajax: {
                    url: '/sales/allsales',
                    type: 'GET',
                    dataSrc: function (json) {
                        return json.data || [];
                    }
                },
                columns: [
                    {data: "salesId"},                      //col 0
                    {data: "product.productName"},          //col 1
                    {data: "quantity"},                     //col 2
                    {data: "total"},                        //col 3
                    {data: "createdOn"},                    //col 4
                ],

                columnDefs: [{
                    targets: 0,
                    orderable: false,
                    render: function (data, type, row, meta) {
                        return meta.row + meta.settings._iDisplayStart + 1;
                    }
                }],
                dom: '<"d-flex justify-content-between align-items-center mb-3"<"d-flex align-items-center"lB><"d-flex align-items-center"f>>rtip',
                buttons: [
                    {
                        extend: 'excel',
                        text: '<i class="fa fa-file-excel-o"></i> Excel',
                        className: 'btn btn-success btn-sm me-1',
                        title: 'Sales List',
                        titleAttr: 'Download Excel'
                    },
                    {
                        extend: 'pdf',
                        text: '<i class="fa fa-file-pdf-o"></i> PDF',
                        className: 'btn btn-danger btn-sm me-1',
                        title: 'Sales List',
                        titleAttr: 'Download PDF'
                    },

                    {
                        extend: 'print',
                        text: '<i class="fa fa-file-print-o"></i> Print',
                        className: 'btn btn-secondary btn-sm',
                        title: 'Purchase List',
                        titleAttr: 'Print Sales List'
                    }
                ]
            });
        });


        /*Open product Modal*/
        function openAddModal() {
            document.getElementById("salesModal").style.display = "flex";
        }

        // /*Save Sales*/
        // function saveSales() {
        //     $("#salesForm").submit();
        // }

        //Get Sales Products

        $('#salesModal').on('shown.bs.modal', function () {

            $.ajax({
                url: '${pageContext.request.contextPath}/sales/get-products',
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
        $('#salesModal').on('show.bs.modal', function () {
            $('#salesForm')[0].reset();
            $('#price').val('');
            $('#total').val('');
        });

        //Form Validation
        $('#salesForm')
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
                    url: '/sales/save',
                    type: 'POST',
                    data: $('#salesForm').serialize(),
                    success: function () {
                        toastr.success("Sales created successfully!");
                        $('#salesModal').modal('hide');

                        $('body').removeClass('modal-open');
                        $('.modal-backdrop').remove();

                        $('#sales').DataTable().ajax.reload(null, false);

                        $('#salesForm').formValidation('resetForm', true);
                    },
                    error: function () {
                        toastr.error("Failed to save product");
                    }
                });
            });
    </script>
</body>
</html>

