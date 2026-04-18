<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/2.3.6/css/dataTables.dataTables.min.css">
</head>

<style>
    .dashboard-cards {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 20px;
        margin-bottom: 30px;
    }

    .card {
        background: #fff;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    .card h3 {
        font-size: 16px;
        color: #666;
    }

    .card-number {
        font-size: 32px;
        font-weight: bold;
        margin-top: 10px;
    }

    header, footer {
        padding: 0px;
        margin: 0px;
    }
</style>

<body>
<div class="container-fluid p-0 m-0">

    <!-- Header -->
    <div class="row g-0">
        <div class="col-12">
            <jsp:include page="header.jsp"/>
        </div>
    </div>

    <!-- Main Body -->
    <div class="row g-0 p-0 m-0">
        <div class="col-md-2  p-0 m-0">
            <jsp:include page="sidebar.jsp"/>
        </div>

        <!-- Content -->
        <div class="col-md-10 p-4 m-0">
            <div class="dashboard-cards">
                <div class="card">
                    <h3>Total Products</h3>
                    <p class="card-number">${productCount}</p>
                    <span>Products in the stock</span>
                </div>
                <div class="card">
                    <h3>Total Purchases</h3>
                    <p class="card-number">${purchaseCount}</p>
                    <span>Purchases made from stock</span>
                </div>
                <div class="card">
                    <h3>Total Sales</h3>
                    <p class="card-number">${salesCount}</p>
                    <span>Sales of stock</span>
                </div>
                <div class="card">
                    <h3>Low Stock</h3>
                    <p class="card-number">${lowStockCount}</p>
                    <span>Restock Quantity</span>
                </div>
            </div>
        </div>

    </div>
</div>

<footer class="bg-dark text-light py-2 text-center">
    <small>© 2026 | Inventory Management System</small>
</footer>
</body>
</body>
</html>

