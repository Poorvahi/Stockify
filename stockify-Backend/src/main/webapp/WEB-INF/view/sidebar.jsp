<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Sidebar</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          rel="stylesheet">

    <style>
        .sidebar {
            width: 220px;
            min-height: 100vh;
            background-color: #212529;
        }

        .sidebar a {
            color: #adb5bd;
            text-decoration: none;
            padding: 15px;
            display: block;
            border-radius: 6px;
            font-size: 19px;
        }

        .sidebar a:hover,
        .sidebar a.active {
            background-color: #343a40;
            color: #fff;
        }

        .sidebar i {
            width: 20px;
        }
    </style>
</head>

<body>

<div class="d-flex">

    <!-- Sidebar -->
    <div class="sidebar p-3">
        <a href="/index">Dashboard</a>

        <a href="/product">Products</a>

        <a href="/purchase">Purchase</a>

        <a href="/sales">Sales</a>

        <hr class="text-secondary">
        <a href="/logout">
            <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>
    </div>

</div>

</body>
</html>